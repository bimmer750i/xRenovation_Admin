package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.GetCommentsResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.GetCommentSuggestionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentSuggestionsViewModel @Inject constructor(
    private val sharedPrefsModel: SharedPrefsModel,
    private val commentSuggestionsUseCase: GetCommentSuggestionsUseCase) :ViewModel()
{
        private val _getCommentSuggestions = MutableLiveData<GetCommentsResult>()
    val getCommentSuggestions : LiveData<GetCommentsResult> = _getCommentSuggestions

    fun getCommentSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            commentSuggestionsUseCase().onEach {
                _getCommentSuggestions.postValue(it)
            }.collect()
        }
    }






}