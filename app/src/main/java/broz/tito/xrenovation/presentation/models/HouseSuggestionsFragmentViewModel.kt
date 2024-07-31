package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import broz.tito.xrenovation.data.add_house.entities.GetHouseSuggestionsResult
import broz.tito.xrenovation.domain.GetHouseSuggestionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HouseSuggestionsFragmentViewModel @Inject constructor(val getHouseSuggestionsUseCase: GetHouseSuggestionsUseCase) : ViewModel() {

    private val _getHouseSuggestionsResult = MutableLiveData<GetHouseSuggestionsResult>()
    val getHouseSuggestionsResult : LiveData<GetHouseSuggestionsResult> = _getHouseSuggestionsResult

    fun getHouseSuggestionsResult() {
        viewModelScope.launch(Dispatchers.IO) {
            getHouseSuggestionsUseCase().onEach {
                _getHouseSuggestionsResult.postValue(it)
            }.collect()
        }
    }

}