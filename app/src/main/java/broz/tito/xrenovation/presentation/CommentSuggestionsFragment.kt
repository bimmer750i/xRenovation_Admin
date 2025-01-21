package broz.tito.xrenovation.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.FragmentCommentSuggestionsBinding
import broz.tito.xrenovation.data.add_house.entities.FailureAddCommentResult
import broz.tito.xrenovation.data.add_house.entities.FailureDeleteSuggestedCommentResult
import broz.tito.xrenovation.data.add_house.entities.SuccessAddCommentResult
import broz.tito.xrenovation.data.add_house.entities.SuccessDeleteSuggestedCommentResult
import broz.tito.xrenovation.data.add_house.entities.SuccessGetCommentsResult
import broz.tito.xrenovation.data.add_house.entities.SuccessGetHouseResult
import broz.tito.xrenovation.presentation.adapters.CommentSuggestionsRecyclerViewAdapter
import broz.tito.xrenovation.presentation.interfaces.SnackBarAble
import broz.tito.xrenovation.presentation.models.CommentSuggestionsViewModel
import broz.tito.xrenovation.presentation.models.CommentSuggestionsViewModelFactory
import javax.inject.Inject


class CommentSuggestionsFragment : Fragment(),SnackBarAble {

    private val TAG = "CommentSuggestionsFragment"

    private lateinit var binding: FragmentCommentSuggestionsBinding

    private lateinit var adapter: CommentSuggestionsRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory: CommentSuggestionsViewModelFactory

    private lateinit var viewModel: CommentSuggestionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[CommentSuggestionsViewModel::class.java]
        adapter = CommentSuggestionsRecyclerViewAdapter({houseId, commentId,comment ->
            viewModel.deleteSuggestedComment(requireContext(),commentId)
            viewModel.addComment(requireContext(),comment)
        },{commentId ->
            viewModel.deleteSuggestedComment(requireContext(),commentId)
        }, {houseId ->
            viewModel.getHouse(houseId)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentSuggestionsBinding.inflate(layoutInflater)
        binding.recyclerviewCommentSuggestions.adapter = adapter
        binding.recyclerviewCommentSuggestions.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCommentSuggestions.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SuccessGetCommentsResult -> {
                    adapter.commentItems = it.commentsList
                }
            }
        })
        viewModel.getHouseResult.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessGetHouseResult -> {
                    val directions = CommentSuggestionsFragmentDirections.actionCommentSuggestionsFragmentToHouseFragment(it.house,it.houseId)
                    findNavController().navigate(directions)
                    viewModel.clearState()
                }
            }
        }
        viewModel.deleteSuggestedComment.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessDeleteSuggestedCommentResult -> viewModel.getCommentSuggestions()
                is FailureDeleteSuggestedCommentResult -> showSnackBarShort(this,binding.root,getString(R.string.failure_delete_suggested_comment))
            }
        }
        viewModel.addCommentResult.observe(viewLifecycleOwner) {
            when (it) {
                is SuccessAddCommentResult -> {viewModel.getCommentSuggestions();showSnackBarShort(this,binding.root,getString(R.string.success_add_suggested_comment))}
                is FailureAddCommentResult -> showSnackBarShort(this,binding.root,getString(R.string.failure_add_suggested_comment))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCommentSuggestions()
    }

}