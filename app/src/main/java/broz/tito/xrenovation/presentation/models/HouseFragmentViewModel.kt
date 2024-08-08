package broz.tito.xrenovation.presentation.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.data.auth.entities.GetAccountInfoResult
import broz.tito.xrenovation.data.auth.entities.RefreshTokenResult
import broz.tito.xrenovation.data.auth.entities.SuccessRefreshTokenResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HouseFragmentViewModel @Inject constructor(val addCommentUseCase: AddCommentUseCase,
                                                 val getCommentsUseCase: GetCommentsUseCase,
                                                 val getAccountInfoUseCase: GetAccountInfoUseCase,
                                                 val refreshTokenUseCase: RefreshTokenUseCase,
                                                 val saveAuthResponseUseCase: SaveAuthResponseUseCase,
                                                 val sharedPrefsModel: SharedPrefsModel,
                                                 val getAdminUseCase: GetAdminUseCase,
                                                 val deleteHouseUseCase: DeleteHouseUseCase,
                                                 val deletePointUseCase: DeletePointUseCase,
                                                 val deleteHousePhotoUseCase: DeleteHousePhotoUseCase,
                                                 val deleteCommentUseCase: DeleteCommentUseCase) : ViewModel() {

    private val _getAccountInfoResult = MutableLiveData<GetAccountInfoResult>()
    val getAccountInfoResult : LiveData<GetAccountInfoResult> = _getAccountInfoResult

    private val _refreshTokenResult = MutableLiveData<RefreshTokenResult>()
    val refreshTokenResult : LiveData<RefreshTokenResult> = _refreshTokenResult

    private val _addCommentResult  = MutableLiveData<AddCommentResult>()
    val addCommentResult : LiveData<AddCommentResult> = _addCommentResult

    private val _getCommentsResult = MutableLiveData<GetCommentsResult>()
    val getCommentsResult : LiveData<GetCommentsResult> = _getCommentsResult

    private val _getAdminResult = MutableLiveData<GetAdminResult>()
    val getAdminResult : LiveData<GetAdminResult> = _getAdminResult

    private val _deleteHouseResult = MutableLiveData<DeleteHouseResult>()
    val deleteHouseResult : LiveData<DeleteHouseResult> = _deleteHouseResult

    private val _deletePointResult = MutableLiveData<DeletePointResult>()
    val deletePointResult : LiveData<DeletePointResult> = _deletePointResult

    private val _deleteCommentResult = MutableLiveData<DeleteSuggestedCommentResult>()
    val deleteCommentResult : LiveData<DeleteSuggestedCommentResult> = _deleteCommentResult

    fun getAccountInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            getAccountInfoUseCase(sharedPrefsModel.getIdToken(context)).onEach {
                _getAccountInfoResult.postValue(it)
            }.collect()
        }
    }

    fun refreshToken(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshTokenUseCase(sharedPrefsModel.getRefreshToken(context)).onEach {
                if (it is SuccessRefreshTokenResult) {
                    saveAuthResponseUseCase(context,it.response.idToken,null,it.response.refreshToken,null)
                }
                _refreshTokenResult.postValue(it)
            }.collect()
        }
    }

    fun addComment(context : Context,houseId : String, text : String, displayName : String?, localId : String?, photoUrl : String?) {
        viewModelScope.launch {
            addCommentUseCase(sharedPrefsModel.getLocalId(context),houseId, Comment(houseId,0L,displayName!!,localId!!,photoUrl!!,0,false,"",text),sharedPrefsModel.getIdToken(context)).onEach {
                _addCommentResult.postValue(it)
            }.collect()
        }
    }

    fun getComments(houseId: String) {
        viewModelScope.launch {
            getCommentsUseCase(houseId).onEach {
                _getCommentsResult.postValue(it)
            }.collect()
        }
    }

    fun getAdmin(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            getAdminUseCase(sharedPrefsModel.getLocalId(context)).onEach {
                _getAdminResult.postValue(it)
            }.collect()
        }
    }

    fun deleteHouse(context: Context,houseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHouseUseCase(houseId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteHouseResult.postValue(it)
            }.collect()
        }
    }

    fun deletePoint(context: Context,houseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase(houseId,sharedPrefsModel.getIdToken(context)).onEach {
                _deletePointResult.postValue(it)
            }.collect()
        }
    }

    fun deleteHousePhoto(photoList : ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHousePhotoUseCase(photoList).collect()
        }
    }

    fun deleteComment(context: Context,houseId: String, commentId : String) {
        viewModelScope.launch {
            deleteCommentUseCase(houseId,commentId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteCommentResult.postValue(it)
            }.collect()
        }
    }



}