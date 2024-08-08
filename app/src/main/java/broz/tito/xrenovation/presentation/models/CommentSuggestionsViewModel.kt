package broz.tito.xrenovation.presentation.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.AddCommentResult
import broz.tito.xrenovation.data.add_house.entities.Comment
import broz.tito.xrenovation.data.add_house.entities.DeleteSuggestedCommentResult
import broz.tito.xrenovation.data.add_house.entities.GetCommentsResult
import broz.tito.xrenovation.data.add_house.entities.GetHouseResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.AddCommentUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedCommentUseCase
import broz.tito.xrenovation.domain.GetCommentSuggestionsUseCase
import broz.tito.xrenovation.domain.GetHouseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentSuggestionsViewModel @Inject constructor(
    private val sharedPrefsModel: SharedPrefsModel,
    private val commentSuggestionsUseCase: GetCommentSuggestionsUseCase,
    private val deleteSuggestedCommentUseCase: DeleteSuggestedCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getHouseUseCase: GetHouseUseCase) :ViewModel()
{
        private val _getCommentSuggestions = MutableLiveData<GetCommentsResult>()
    val getCommentSuggestions : LiveData<GetCommentsResult> = _getCommentSuggestions

    private val _deleteSuggestedComment = MutableLiveData<DeleteSuggestedCommentResult>()
    val deleteSuggestedComment : LiveData<DeleteSuggestedCommentResult> = _deleteSuggestedComment

    private val _addCommentResult  = MutableLiveData<AddCommentResult>()
    val addCommentResult : LiveData<AddCommentResult> = _addCommentResult

    private val _getHouseResult = MutableLiveData<GetHouseResult>()
    val getHouseResult : LiveData<GetHouseResult> = _getHouseResult

    fun getCommentSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            commentSuggestionsUseCase().onEach {
                _getCommentSuggestions.postValue(it)
            }.collect()
        }
    }

    fun deleteSuggestedComment(context: Context,suggestedCommentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSuggestedCommentUseCase(suggestedCommentId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteSuggestedComment.postValue(it)
            }.collect()
        }
    }

    fun addComment(context : Context,comment: Comment) {
        viewModelScope.launch {
            addCommentUseCase(sharedPrefsModel.getLocalId(context),comment.houseId, comment,sharedPrefsModel.getIdToken(context)).onEach {
                _addCommentResult.postValue(it)
            }.collect()
        }
    }

    fun getHouse(houseId : String) {
        viewModelScope.launch {
            getHouseUseCase(houseId).onEach {
                _getHouseResult.postValue(it)
            }.collect()
        }
    }

    fun clearState() {
        _deleteSuggestedComment.postValue(DeleteSuggestedCommentResult())
        _addCommentResult.postValue(AddCommentResult())
        _getHouseResult.postValue(GetHouseResult())
    }

}