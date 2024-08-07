package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.AddCommentUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedCommentUseCase
import broz.tito.xrenovation.domain.GetCommentSuggestionsUseCase
import javax.inject.Inject

class CommentSuggestionsViewModelFactory @Inject constructor(
    private val sharedPrefsModel: SharedPrefsModel,
    private val commentSuggestionsUseCase: GetCommentSuggestionsUseCase,
    private val deleteSuggestedCommentUseCase: DeleteSuggestedCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentSuggestionsViewModel(sharedPrefsModel, commentSuggestionsUseCase, deleteSuggestedCommentUseCase, addCommentUseCase) as T
    }
}