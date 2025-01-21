package broz.tito.xrenovation.presentation

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.AlertDialogAddUrlBinding
import broz.tito.xrenovation.admin.databinding.FragmentEditHouseBinding
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.presentation.adapters.PhotoItemTouchHelperCallback
import broz.tito.xrenovation.presentation.adapters.PhotoRecyclerViewAdapter
import broz.tito.xrenovation.presentation.interfaces.SnackBarAble
import broz.tito.xrenovation.presentation.models.EditHouseViewModel
import broz.tito.xrenovation.presentation.models.EditHouseViewModelFactory
import com.google.android.material.chip.Chip
import com.yandex.mapkit.geometry.Point
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class EditHouseFragment : Fragment(),SnackBarAble {

    @Inject
    lateinit var viewModelFactory : EditHouseViewModelFactory

    private lateinit var viewModel : EditHouseViewModel

    private val TAG = "EditHouseFragment"

    private lateinit var binding : FragmentEditHouseBinding

    private var photoList : ArrayList<String> = ArrayList()

    private var new_photos_list : ArrayList<String> = ArrayList()

    private var urlList : ArrayList<String> = ArrayList()

    private val args : HouseFragmentArgs by navArgs()

    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var recyclerViewAdapter : PhotoRecyclerViewAdapter

    private var house : House? = null

    private var houseId : String? = null

    private var editedHouse : House? = house

    private var housePoint : Point? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[EditHouseViewModel::class.java]
        args.let {
            house = it.house
            houseId = it.houseId
            photoList = it.house.photos
            urlList = it.house.links
            housePoint = Point(it.house.point.latitude,it.house.point.longitude)
        }
        Log.d(TAG, "Photo list of house: $photoList")
        recyclerViewAdapter = PhotoRecyclerViewAdapter(PhotoRecyclerViewAdapter.EDIT_PHOTO_VIEWHOLDER,{})
        recyclerViewAdapter.list = photoList
        registerForActivityResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditHouseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewChosenPhoto.adapter = recyclerViewAdapter
        binding.recyclerviewChosenPhoto.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val itemTouchHelperCallback = PhotoItemTouchHelperCallback()
        val photoItemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        photoItemTouchHelper.attachToRecyclerView(binding.recyclerviewChosenPhoto)
        binding.imageViewAddressLocation.setOnClickListener {
            if (housePoint != null) {
                val directions = EditHouseFragmentDirections.actionEditHouseFragmentToFindHouseOnMapFragment(LatLon(housePoint!!.latitude,housePoint!!.longitude))
                findNavController().navigate(directions)
            }
            else {
                findNavController().navigate(R.id.action_editHouseFragment_to_findHouseOnMapFragment)
            }
        }
        binding.imageViewAddButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.chipAddUrl.setOnClickListener {
            showAddUrlAlertDialog("",false,null)
        }
        binding.buttonEditHouse.setOnClickListener {
            editHouse()
        }
        if (house != null) {
            binding.autoCompleteTextView.setText(house!!.address,false)
            binding.editTextNumberOfFloors.setText(house!!.floors)
            binding.editTextNumberOfFlats.setText(house!!.flats)
            binding.editTextConstructionYear.setText(house!!.year)
            binding.editTextDescription.setText(house!!.description)
        }
        urlList.forEach {
            addChip(it)
        }
        viewModel.loadPhotosResult.observe(viewLifecycleOwner) {
            when (it)  {
                is PendingLoadPhotosResult -> {
                }
                is SuccessLoadPhotosResult -> {
                    photoList.removeIf { !it.startsWith("https") }
                    it.urlList.forEach {
                        photoList.add(it)
                    }
                    Log.d(TAG, "PHOTO LIST TO PATCH : $photoList")
                    viewModel.editHouse(requireContext(),houseId!!,House(LatLon(housePoint!!.latitude,housePoint!!.longitude),binding.autoCompleteTextView.text.toString(),binding.editTextNumberOfFloors.text.toString(),binding.editTextNumberOfFlats.text.toString(),binding.editTextConstructionYear.text.toString(),binding.editTextDescription.text.toString(),photoList,urlList))
                }
                is FailureLoadPhotosResult -> {
                    showSnackBarShort(this,binding.root,"Oops ! Failure loading photos!")
                    Log.d(TAG, "failureLoadPhotosResult: ${it.errorMessage}")
                }

            }
        }
        viewModel.editHouseResult.observe(viewLifecycleOwner) {
            when (it) {
                is PendingEditHouseResult -> {

                }
                is SuccessEditHouseResult -> {
                    viewModel.editHousePoint(requireContext(),houseId!!, HousePoint(houseId!!,
                        LatLon(housePoint?.latitude!!,housePoint?.longitude!!)
                    )
                    )
                }
                is FailureEditHouseResult -> {

                }
            }
        }
        viewModel.editHouseResult.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessEditHouseResult -> showSnackBarShort(this,binding.root,getString(R.string.success_add_suggested_house))
                is FailureEditHouseResult -> showSnackBarShort(this,binding.root,getString(R.string.failure_add_suggested_house))
            }
        }
        parentFragmentManager.setFragmentResultListener(FindHouseOnMapFragment.MAP_RESULT,viewLifecycleOwner) { requestKey, bundle ->
            if (requestKey == FindHouseOnMapFragment.MAP_RESULT) {
                var text : String? = ""
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(FindHouseOnMapFragment.ADDRESS,SearchPointAddress::class.java)?.toShortAddress()
                } else {
                    (bundle.getSerializable(FindHouseOnMapFragment.ADDRESS) as SearchPointAddress).toShortAddress()
                }
                binding.autoCompleteTextView.setText(text,false)
                val latitude = bundle.getDouble(FindHouseOnMapFragment.LATITUDE)
                val longitude = bundle.getDouble(FindHouseOnMapFragment.LONGITUDE)
                housePoint = Point(latitude,longitude)
            }
        }
    }

    private fun registerForActivityResult() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uri ->
            if (uri.size > 0) {
                if (new_photos_list.size == 0) {
                    requireActivity().externalCacheDir?.deleteRecursively()
                }
                val startNumber = photoList.size + new_photos_list.size
                for (i in uri.indices) {
                    val uri = uri.get(i)
                    val file = File(requireActivity().externalCacheDir,"house-picture-${i+startNumber}.jpg")
                    val outputStream = FileOutputStream(file)
                    val inputStream = requireActivity().contentResolver.openInputStream(uri)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream?.close()
                    photoList.add(file.absolutePath)
                }
                recyclerViewAdapter.list = photoList
                Log.d(TAG, "new_photos_list : ${photoList}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun showYesNoAlertDialog(title : String, clickListener : OnClickListener) {

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
        Log.d(TAG, "url_list : $urlList")
    }

    private fun showAddUrlAlertDialog(text : String, isURLChip : Boolean, chip: Chip?) {
        val binding = AlertDialogAddUrlBinding.inflate(layoutInflater)
        if (isURLChip) {
            binding.editTextAddLink.setText(text)
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_url)
            .setView(binding.root)
            .setPositiveButton(R.string.add) { dialogInterface, num ->
                if (!isURLChip) {
                    val typedText = binding.editTextAddLink.text.toString()
                    if (!urlList.contains(typedText)) {
                        urlList.add(typedText)
                        addChip(urlList.last())
                    }
                }
                else {
                    val typedText = binding.editTextAddLink.text.toString()
                    if (typedText != text) {
                        val position = urlList.indexOf(text)
                        if (position > -1) {
                            urlList[position] = typedText
                            chip?.tag = typedText
                            chip?.text = typedText.take(15)
                        }
                    }
                }
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, num ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
        Log.d(TAG, "url_list : $urlList")
    }

    // TODO FIX ARRAY COPY
    private fun editHouse() {
        val newPhotoList = arrayListOf<String>()
        newPhotoList.addAll(recyclerViewAdapter.list)
        newPhotoList.removeIf { it.startsWith("https")}
        Log.d(TAG, "editHouse: photos to be loaded: $newPhotoList")
        viewModel.loadPhotosToFireBase(requireContext(),UUID.randomUUID().toString().take(10),newPhotoList)
    }


    private fun addChip(text : String) {
        val chip = Chip(requireContext())
        chip.tag = text
        chip.text = text.take(15)
        chip.isCloseIconVisible = true
        chip.setOnClickListener {
            showAddUrlAlertDialog(chip.tag.toString(),true,chip)
        }
        chip.setOnCloseIconClickListener {
            showYesNoAlertDialog(getString(R.string.delete_url)) {
                binding.chipGroupLinks.removeView(chip)
                urlList.removeIf { it == chip.tag }
            }
        }
        binding.chipGroupLinks.addView(chip)
    }

}