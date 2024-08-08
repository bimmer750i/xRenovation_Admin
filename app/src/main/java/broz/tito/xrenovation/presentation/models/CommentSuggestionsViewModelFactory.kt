package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.AddCommentUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedCommentUseCase
import broz.tito.xrenovation.domain.GetCommentSuggestionsUseCase
import broz.tito.xrenovation.domain.GetHouseUseCase
import javax.inject.Inject

class CommentSuggestionsViewModelFactory @Inject constructor(
    private val sharedPrefsModel: SharedPrefsModel,
    private val commentSuggestionsUseCase: GetCommentSuggestionsUseCase,
    private val deleteSuggestedCommentUseCase: DeleteSuggestedCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getHouseUseCase: GetHouseUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentSuggestionsViewModel(sharedPrefsModel, commentSuggestionsUseCase, deleteSuggestedCommentUseCase, addCommentUseCase,getHouseUseCase) as T
    }
}