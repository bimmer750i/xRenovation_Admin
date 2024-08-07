package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.GetCommentSuggestionsUseCase
import javax.inject.Inject

class CommentSuggestionsViewModelFactory @Inject constructor(
    private val sharedPrefsModel: SharedPrefsModel,
    private val commentSuggestionsUseCase: GetCommentSuggestionsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentSuggestionsViewModel(sharedPrefsModel, commentSuggestionsUseCase) as T
    }
}