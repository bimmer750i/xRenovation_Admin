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
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.AlertDialogAddUrlBinding
import broz.tito.xrenovation.admin.databinding.FragmentEditSuggestedHouseBinding
import broz.tito.xrenovation.data.add_house.entities.House
import broz.tito.xrenovation.data.add_house.entities.LatLon
import broz.tito.xrenovation.data.add_house.entities.SearchPointAddress
import broz.tito.xrenovation.presentation.adapters.PhotoItemTouchHelperCallback
import broz.tito.xrenovation.presentation.adapters.PhotoRecyclerViewAdapter
import broz.tito.xrenovation.presentation.models.EditSuggestedHouseViewModel
import broz.tito.xrenovation.presentation.models.EditSuggestedHouseViewModelFactory
import com.google.android.material.chip.Chip
import com.yandex.mapkit.geometry.Point
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class EditSuggestedHouseFragment : Fragment() {

    private val TAG = "EditSuggestedHouseFragment"

    @Inject
    lateinit var viewModelFactory: EditSuggestedHouseViewModelFactory

    private lateinit var viewModel: EditSuggestedHouseViewModel

    private lateinit var binding: FragmentEditSuggestedHouseBinding

    private var photoList : ArrayList<String> = ArrayList()

    private var new_photos_list : ArrayList<String> = ArrayList()

    private var urlList : ArrayList<String> = ArrayList()

    private val args : EditSuggestedHouseFragmentArgs by navArgs()

    private lateinit var houseId : String

    private lateinit var house: House

    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var recyclerViewAdapter : PhotoRecyclerViewAdapter

    private var housePoint : Point? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[EditSuggestedHouseViewModel::class.java]
        args.let {
            houseId = it.houseId
            house = it.house
            photoList = it.house.photos
            urlList = it.house.links
            housePoint = Point(it.house.point.latitude,it.house.point.longitude)
        }
        recyclerViewAdapter = PhotoRecyclerViewAdapter(PhotoRecyclerViewAdapter.EDIT_PHOTO_VIEWHOLDER,{})
        recyclerViewAdapter.list = photoList
        registerForActivityResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditSuggestedHouseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewSuggestedHouseChosenPhoto.adapter = recyclerViewAdapter
        binding.recyclerviewSuggestedHouseChosenPhoto.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        val itemTouchHelperCallback = PhotoItemTouchHelperCallback()
        val photoItemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        photoItemTouchHelper.attachToRecyclerView(binding.recyclerviewSuggestedHouseChosenPhoto)
        binding.imageViewSuggestedHouseAddressLocation.setOnClickListener {
            if (housePoint != null) {
                val directions = EditSuggestedHouseFragmentDirections.actionEditSuggestedHouseFragmentToFindHouseOnMapFragment(
                    LatLon(housePoint!!.latitude,housePoint!!.longitude)
                )
                findNavController().navigate(directions)
            }
            else {
                findNavController().navigate(R.id.action_editSuggestedHouseFragment_to_findHouseOnMapFragment)
            }
        }
        binding.imageViewSuggestedHouseAddButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        if (house != null) {
            binding.autoCompleteTextViewSuggestedHouse.setText(house!!.address,false)
            binding.editTextSuggestedHouseNumberOfFloors.setText(house!!.floors)
            binding.editTextSuggestedHouseNumberOfFlats.setText(house!!.flats)
            binding.editTextSuggestedHouseConstructionYear.setText(house!!.year)
            binding.editTextSuggestedHouseDescription.setText(house!!.description)
        }
        urlList.forEach {
            addChip(it)
        }
        parentFragmentManager.setFragmentResultListener(FindHouseOnMapFragment.MAP_RESULT,viewLifecycleOwner) { requestKey, bundle ->
            if (requestKey == FindHouseOnMapFragment.MAP_RESULT) {
                var text : String? = ""
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(FindHouseOnMapFragment.ADDRESS, SearchPointAddress::class.java)?.toShortAddress()
                } else {
                    (bundle.getSerializable(FindHouseOnMapFragment.ADDRESS) as SearchPointAddress).toShortAddress()
                }
                binding.autoCompleteTextViewSuggestedHouse.setText(text,false)
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
                binding.chipGroupSuggestedHouseLinks.removeView(chip)
                urlList.removeIf { it == chip.tag }
            }
        }
        binding.chipGroupSuggestedHouseLinks.addView(chip)
    }

}