package broz.tito.xrenovation.presentation.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.DeleteHousePhotoResult
import broz.tito.xrenovation.data.add_house.entities.DeletePointResult
import broz.tito.xrenovation.data.add_house.entities.DeleteSuggestedHouseResult
import broz.tito.xrenovation.data.add_house.entities.GetHouseSuggestionsResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.DeleteHousePhotoUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedHouseUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedPointUseCase
import broz.tito.xrenovation.domain.GetHouseSuggestionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HouseSuggestionsFragmentViewModel @Inject constructor(
    val sharedPrefsModel: SharedPrefsModel,
    val getHouseSuggestionsUseCase: GetHouseSuggestionsUseCase,
    val deleteHousePhotoUseCase: DeleteHousePhotoUseCase,
    val deleteSuggestedHouseUseCase: DeleteSuggestedHouseUseCase,
    val deleteSuggestedPointUseCase : DeleteSuggestedPointUseCase
) : ViewModel() {

    private val _getHouseSuggestionsResult = MutableLiveData<GetHouseSuggestionsResult>()
    val getHouseSuggestionsResult : LiveData<GetHouseSuggestionsResult> = _getHouseSuggestionsResult

    private val _deleteSuggestedHouseResult = MutableLiveData<DeleteSuggestedHouseResult>()
    val deleteSuggestedHouseResult : LiveData<DeleteSuggestedHouseResult> = _deleteSuggestedHouseResult

    private val _deleteSuggestedPointResult = MutableLiveData<DeletePointResult>()
    val deleteSuggestedPointResult : LiveData<DeletePointResult> = _deleteSuggestedPointResult

    private val _deleteSuggestedHousePhotoResult = MutableLiveData<DeleteHousePhotoResult>()
    val deleteSuggestedHousePhotoResult : LiveData<DeleteHousePhotoResult> = _deleteSuggestedHousePhotoResult

    fun getHouseSuggestionsResult() {
        viewModelScope.launch(Dispatchers.IO) {
            getHouseSuggestionsUseCase().onEach {
                _getHouseSuggestionsResult.postValue(it)
            }.collect()
        }
    }

    fun deleteSuggestedHouse(context: Context, suggestedHouseId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSuggestedHouseUseCase(suggestedHouseId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteSuggestedHouseResult.postValue(it)
            }.collect()
        }
    }

    fun deleteSuggestedPoint(context: Context, suggestedHouseId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSuggestedPointUseCase(suggestedHouseId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteSuggestedPointResult.postValue(it)
            }.collect()
        }
    }

    fun deleteSuggestedHousePhoto(urlList : ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHousePhotoUseCase(urlList).onEach {
                _deleteSuggestedHousePhotoResult.postValue(it)
            }.collect()
        }
    }

}