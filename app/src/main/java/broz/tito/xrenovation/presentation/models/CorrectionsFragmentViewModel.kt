package broz.tito.xrenovation.presentation.models

import android.content.Context
import androidx.lifecycle.*
import broz.tito.xrenovation.data.add_house.entities.DeleteCorrectionResult
import broz.tito.xrenovation.data.add_house.entities.GetCorrectionsResult
import broz.tito.xrenovation.data.add_house.entities.GetHouseResult
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.DeleteCorrectionUseCase
import broz.tito.xrenovation.domain.GetCorrectionsUseCase
import broz.tito.xrenovation.domain.GetHouseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CorrectionsFragmentViewModel @Inject constructor(val getCorrectionsUseCase: GetCorrectionsUseCase,
                                                       val sharedPrefsModel: SharedPrefsModel,
                                                       val getHouseUseCase: GetHouseUseCase,
                                                       val deleteCorrectionUseCase: DeleteCorrectionUseCase) : ViewModel() {

    private val _getCorrectionsResult = MutableLiveData<GetCorrectionsResult>()
    val getCorrectionsResult : LiveData<GetCorrectionsResult> = _getCorrectionsResult

    private val _getHouseResult = MutableLiveData<GetHouseResult>()
    val getHouseResult : LiveData<GetHouseResult> = _getHouseResult

    private val _deleteCorrectionResult = MutableLiveData<DeleteCorrectionResult>()
    val deleteCorrectionResult : LiveData<DeleteCorrectionResult> = _deleteCorrectionResult

    fun getCorrections() {
        viewModelScope.launch(Dispatchers.IO) {
            getCorrectionsUseCase().onEach {
                _getCorrectionsResult.postValue(it)
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

    fun deleteCorrection(context: Context, correctionId : String) {
        viewModelScope.launch {
            deleteCorrectionUseCase(correctionId,sharedPrefsModel.getIdToken(context)).onEach {
                _deleteCorrectionResult.postValue(it)
            }.collect()
        }
    }

    fun resetState() {
        _getHouseResult.postValue(GetHouseResult())
    }

}