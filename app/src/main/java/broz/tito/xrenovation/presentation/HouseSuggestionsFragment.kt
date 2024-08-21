package broz.tito.xrenovation.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.FragmentHouseSuggestionsBinding
import broz.tito.xrenovation.data.add_house.entities.SuccessGetHouseSuggestionsResult
import broz.tito.xrenovation.presentation.adapters.HouseSuggestionsRecyclerViewAdapter
import broz.tito.xrenovation.presentation.models.CorrectionsFragmentViewModelFactory
import broz.tito.xrenovation.presentation.models.HouseSuggestionsFragmentViewModel
import broz.tito.xrenovation.presentation.models.HouseSuggestionsFragmentViewModelFactory
import javax.inject.Inject


class HouseSuggestionsFragment : Fragment() {

    private lateinit var binding : FragmentHouseSuggestionsBinding

    @Inject
    lateinit var viewModelFactory: HouseSuggestionsFragmentViewModelFactory

    private lateinit var viewModel: HouseSuggestionsFragmentViewModel

    private lateinit var houseSuggestionsRecyclerViewAdapter: HouseSuggestionsRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[HouseSuggestionsFragmentViewModel::class.java]
        houseSuggestionsRecyclerViewAdapter = HouseSuggestionsRecyclerViewAdapter({houseId, house ->
            val directions = HouseSuggestionsFragmentDirections.actionHouseSuggestionsFragmentToEditSuggestedHouseFragment(houseId, house)
            findNavController().navigate(directions)

        }, { suggestedHouseId,urlList ->
                viewModel.deleteSuggestedHouse(requireContext(),suggestedHouseId)
                viewModel.deleteSuggestedPoint(requireContext(),suggestedHouseId)
                viewModel.deleteSuggestedHousePhoto(urlList)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHouseSuggestionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCorrections.adapter = houseSuggestionsRecyclerViewAdapter
        binding.recyclerViewCorrections.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        viewModel.getHouseSuggestionsResult.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessGetHouseSuggestionsResult -> {
                    houseSuggestionsRecyclerViewAdapter.list = it.houseSuggestionsList
                }
            }
        }
        viewModel.getHouseSuggestionsResult()
    }

}