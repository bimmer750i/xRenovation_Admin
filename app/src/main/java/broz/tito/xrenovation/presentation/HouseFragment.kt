package broz.tito.xrenovation.presentation

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.FragmentHouseBinding
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.data.auth.entities.*
import broz.tito.xrenovation.presentation.adapters.CommentsRecyclerViewAdapter
import broz.tito.xrenovation.presentation.adapters.PhotoRecyclerViewAdapter
import broz.tito.xrenovation.presentation.interfaces.SnackBarAble
import broz.tito.xrenovation.presentation.models.HouseFragmentViewModel
import broz.tito.xrenovation.presentation.models.HouseFragmentViewModelFactory
import com.google.android.material.chip.Chip
import javax.inject.Inject


class HouseFragment : Fragment(),SnackBarAble {

    private val TAG = "HouseFragment"

    private lateinit var binding : FragmentHouseBinding

    private val args : HouseFragmentArgs by navArgs()

    private lateinit var adapter : PhotoRecyclerViewAdapter

    private lateinit var commentsAdapter : CommentsRecyclerViewAdapter

    private var houseId : String? = null

    private var house : House? = null

    @Inject
    lateinit var houseFragmentViewModelFactory: HouseFragmentViewModelFactory

    private lateinit var viewModel: HouseFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,houseFragmentViewModelFactory)[HouseFragmentViewModel::class.java]
        adapter = PhotoRecyclerViewAdapter(PhotoRecyclerViewAdapter.DISPLAY_PHOTO_VIEWHOLDER,{})
        commentsAdapter = CommentsRecyclerViewAdapter { houseId, commentId ->
            viewModel.deleteComment(requireContext(), houseId, commentId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHouseBinding.inflate(layoutInflater)
        binding.recyclerviewHousePhoto.adapter = adapter
        binding.recyclerviewHousePhoto.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.recyclerviewComments.adapter = commentsAdapter
        binding.recyclerviewComments.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        args.let {
            binding.textViewHouseAddress.text = it.house.address
            binding.editTextConstructionYear.text = it.house.year
            binding.textViewNumberOfFlatsNumber.text = it.house.flats
            binding.textViewNumberOfFloorsNumber.text = it.house.floors
            binding.textViewDescriptionText.text = it.house.description
            it.house.links.forEach {
                addChip(it)
            }
            adapter.list = it.house.photos
            houseId = it.houseId
            house = it.house
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonComment.setOnClickListener {
            viewModel.getAccountInfo(requireContext())
        }
        binding.imageViewSuggestHouseCorrection.setOnClickListener {
            viewModel.getAdmin(requireContext())
        }
        viewModel.getAccountInfoResult.observe(viewLifecycleOwner) {
            when (it) {
                is PendingGetAccountInfoResult -> {

                }
                is SuccessGetAccountInfoResult -> {
                    it.user.emailVerified?.let {verified ->
                        if (verified) {
                            viewModel.addComment(requireContext(),houseId!!,binding.editTextComment.text.toString(),it.user.displayName,it.user.localId,it.user.photoUrl)
                        }
                        else {
                            showSnackBarShort(this,binding.root,getString(R.string.email_not_verified))
                        }
                    }
                }
                is FailureGetAccountInfoResult -> {
                    when (it.errorMessage) {
                        "INVALID_ID_TOKEN" -> {
                            viewModel.refreshToken(requireContext())
                        }
                        "USER_NOT_FOUND" -> {
                            (requireActivity().application as App).loggedStatus = LoggedStatus.LOGGED_OUT
                        }
                        "USER_DISABLED" -> {
                            (requireActivity().application as App).loggedStatus = LoggedStatus.LOGGED_OUT
                        }
                        else -> {
                            showSnackBarShort(this,binding.root,getString(R.string.get_account_info_error))
                        }
                    }
                }
            }
        }
        viewModel.refreshTokenResult.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessRefreshTokenResult -> {
                    viewModel.getAccountInfo(requireContext())
                }
                is FailureRefreshTokenResult -> {
                    when(it.errorMessage) {
                        "TOKEN_EXPIRED" -> {
                            showSnackBarShort(this,binding.root,getString(R.string.user_not_found))
                            //findNavController().navigate(R.id.action_accountInfoFragment_to_accountFragment)
                        }
                        "USER_DISABLED" -> {
                            showSnackBarShort(this,binding.root,getString(R.string.user_not_found))
                            //findNavController().navigate(R.id.action_accountInfoFragment_to_accountFragment)
                        }
                        "USER_NOT_FOUND" -> {
                            showSnackBarShort(this,binding.root,getString(R.string.user_not_found))
                            //findNavController().navigate(R.id.action_accountInfoFragment_to_accountFragment)
                        }
                        "MISSING_REFRESH_TOKEN" -> {
                            showSnackBarShort(this,binding.root,getString(R.string.user_not_logged_in))
                            //findNavController().navigate(R.id.action_accountInfoFragment_to_accountFragment)
                        }
                        else -> {
                            showSnackBarShort(this,binding.root,getString(R.string.get_account_info_error))
                        }
                    }
                }
            }
        }
        viewModel.addCommentResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PendingAddCommentResult -> {

                }
                is SuccessAddCommentResult -> {
                    viewModel.getComments(houseId!!)
                }
                is FailureAddCommentResult -> {

                }
            }
        })
        viewModel.getCommentsResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PendingGetCommentsResult -> {

                }
                is SuccessGetCommentsResult -> {
                    if (it.commentsList.size > 0) {
                        binding.recyclerviewComments.visibility = View.VISIBLE
                        commentsAdapter.commentItems = it.commentsList
                    }
                }
                is FailureGetCommentsResult -> {

                }
            }
        })
        viewModel.getAdminResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PendingGetAdminResult -> {
                    Log.d(TAG, "getAdminResult: pending")
                }
                is SuccessGetAdminResult -> {
                    Log.d(TAG, "getAdminResult: success")
                    it.admin.isAdmin?.let {
                        val popupMenu = PopupMenu(requireContext(),binding.imageViewSuggestHouseCorrection)
                        popupMenu.menuInflater.inflate(R.menu.house_fragment_menu,popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener {
                            when(it.itemId) {
                                R.id.edit_house -> {
                                    val directions = HouseFragmentDirections.actionHouseFragmentToEditHouseFragment(houseId!!,house!!)
                                    findNavController().navigate(directions)
                                }
                                R.id.delete_house -> {
                                    showYesNoAlertDialog(getString(R.string.delete_house)) {
                                        viewModel.deleteHouse(requireContext(),houseId!!)
                                    }
                                }
                            }
                            true }
                        popupMenu.show()
                    }
                    if (it.admin.isAdmin != true || it.admin.isAdmin == null) {
                        showSnackBarShort(this,binding.root,"GTFO OUTTA HERE !!!")
                    }

                }
                is FailureGetAdminResult -> {
                    Log.d(TAG, "getAdminResult: failure !!!")
                    showSnackBarShort(this,binding.root,"GTFO OUTTA HERE !!!")
                }
            }
        })
        viewModel.deleteHouseResult.observe(viewLifecycleOwner) {
            when(it) {
                is SuccessDeleteHouseResult -> {
                    viewModel.deletePoint(requireContext(),houseId!!)
                    viewModel.deleteHousePhoto(house!!.photos)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getComments(houseId!!)
    }

    private fun addChip(text : String) {
        val chip = Chip(requireContext())
        chip.tag = text
        chip.text = text.removePrefix("https://").take(13) + ".."
        chip.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(chip.tag.toString()))
            startActivity(browserIntent)
        }
        binding.chipGroupLinks.addView(chip)
    }

    private fun showYesNoAlertDialog(title : String, clickListener : View.OnClickListener) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(R.string.yes) { dialogInterface, num ->
                clickListener.onClick(view)
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, num ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

}