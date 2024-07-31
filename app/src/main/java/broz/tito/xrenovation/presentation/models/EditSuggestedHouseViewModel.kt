package broz.tito.xrenovation.presentation.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.AddHousePointResult
import broz.tito.xrenovation.data.add_house.entities.AddHouseResult
import broz.tito.xrenovation.data.add_house.entities.EditHouseResult
import broz.tito.xrenovation.data.add_house.entities.LoadPhotosResult
import broz.tito.xrenovation.data.add_house.entities.SearchPointResult
import broz.tito.xrenovation.data.add_house.entities.SuggestAddressResult
import broz.tito.xrenovation.data.auth.entities.GetAccountInfoResult
import broz.tito.xrenovation.data.auth.entities.RefreshTokenResult
import broz.tito.xrenovation.data.auth.entities.SuccessRefreshTokenResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.AddHousePointUseCase
import broz.tito.xrenovation.domain.AddHouseUseCase
import broz.tito.xrenovation.domain.DeleteHousePhotoUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedHouseUseCase
import broz.tito.xrenovation.domain.EditHouseUseCase
import broz.tito.xrenovation.domain.GetAccountInfoUseCase
import broz.tito.xrenovation.domain.LoadPhotosToFireBaseUseCase
import broz.tito.xrenovation.domain.RefreshTokenUseCase
import broz.tito.xrenovation.domain.SaveAuthResponseUseCase
import broz.tito.xrenovation.domain.SearchPointUseCase
import broz.tito.xrenovation.domain.SuggestAddressUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSuggestedHouseViewModel @Inject constructor(val getAccountInfoUseCase: GetAccountInfoUseCase,
                                                      val refreshTokenUseCase: RefreshTokenUseCase,
                                                      val saveAuthResponseUseCase: SaveAuthResponseUseCase,
                                                      val sharedPrefsModel: SharedPrefsModel,
                                                      val suggestAddressUseCase: SuggestAddressUseCase,
                                                      val searchPointUseCase: SearchPointUseCase,
                                                      private val loadPhotosToFireBaseUseCase: LoadPhotosToFireBaseUseCase,
                                                      val addHouseUseCase: AddHouseUseCase,
                                                      val addHousePointUseCase: AddHousePointUseCase,
                                                      val editHouseUseCase: EditHouseUseCase,
                                                      val deleteHousePhotoUseCase: DeleteHousePhotoUseCase,
                                                      val deleteSuggestedHouseUseCase: DeleteSuggestedHouseUseCase
) : ViewModel() {

    private val _getAccountInfoResult = MutableLiveData<GetAccountInfoResult>()
    val getAccountInfoResult : LiveData<GetAccountInfoResult> = _getAccountInfoResult

    private val _refreshTokenResult = MutableLiveData<RefreshTokenResult>()
    val refreshTokenResult : LiveData<RefreshTokenResult> = _refreshTokenResult

    private val _suggestAddressResult = MutableLiveData<SuggestAddressResult>()
    val suggestAddressResult : LiveData<SuggestAddressResult> = _suggestAddressResult

    private val _searchPointResult = MutableLiveData<SearchPointResult>()
    val searchPointResult : LiveData<SearchPointResult> = _searchPointResult

    private val _loadPhotosResult = MutableLiveData<LoadPhotosResult>()
    val loadPhotosResult : LiveData<LoadPhotosResult> = _loadPhotosResult

    private val _addHouseResult = MutableLiveData<AddHouseResult>()
    val addHouseResult : LiveData<AddHouseResult> = _addHouseResult

    private val _addHousePointResult = MutableLiveData<AddHousePointResult>()
    val addHousePointResult : LiveData<AddHousePointResult> = _addHousePointResult

    private val _editHouseResult = MutableLiveData<EditHouseResult>()
    val editHouseResult : LiveData<EditHouseResult> = _editHouseResult

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

    fun loadPhotosToFireBase(context: Context, path : String, list : ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            loadPhotosToFireBaseUseCase(sharedPrefsModel.getLocalId(context),path,list).onEach {
                _loadPhotosResult.postValue(it)
            }.collect()
        }
    }

}