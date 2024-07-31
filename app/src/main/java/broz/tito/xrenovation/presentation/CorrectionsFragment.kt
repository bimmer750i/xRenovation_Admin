package broz.tito.xrenovation.presentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.FragmentCorrectionsBinding
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.presentation.adapters.CorrectionsRecyclerViewAdapter
import broz.tito.xrenovation.presentation.models.CorrectionsFragmentViewModel
import broz.tito.xrenovation.presentation.models.CorrectionsFragmentViewModelFactory
import javax.inject.Inject

class CorrectionsFragment : Fragment() {

    val TAG = "CorrectionsFragment"


    private lateinit var viewModel: CorrectionsFragmentViewModel

    @Inject
    lateinit var viewModelFactory: CorrectionsFragmentViewModelFactory

    private lateinit var binding : FragmentCorrectionsBinding

    private lateinit var correctionsRecyclerViewAdapter : CorrectionsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[CorrectionsFragmentViewModel::class.java]
        correctionsRecyclerViewAdapter = CorrectionsRecyclerViewAdapter({
              viewModel.getHouse(it)
        }) {
            viewModel.deleteCorrection(requireContext(),it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCorrectionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCorrections.adapter = correctionsRecyclerViewAdapter
        binding.recyclerViewCorrections.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        viewModel.getCorrectionsResult.observe(viewLifecycleOwner) {
            when (it) {
                is PendingGetCorrectionsResult -> {

                }
                is SuccessGetCorrectionsResult -> {
                    correctionsRecyclerViewAdapter.list = it.correctionsList
                }
                is FailureGetCorrectionsResult -> {

                }
            }
        }
        viewModel.getHouseResult.observe(viewLifecycleOwner) {
            when (it) {
                is PendingGetHouseResult -> {

                }
                is SuccessGetHouseResult -> {
                    val directions = CorrectionsFragmentDirections.actionCorrectionsFragmentToEditHouseFragment(it.houseId,it.house)
                    findNavController().navigate(directions)
                    viewModel.resetState()
                }
                is FailureGetHouseResult -> {

                }
            }
        }
        viewModel.getCorrections()
    }

}