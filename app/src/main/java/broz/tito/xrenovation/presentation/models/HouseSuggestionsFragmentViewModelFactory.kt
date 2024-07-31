package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.domain.GetHouseSuggestionsUseCase
import javax.inject.Inject

class HouseSuggestionsFragmentViewModelFactory @Inject constructor(val getHouseSuggestionsUseCase: GetHouseSuggestionsUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HouseSuggestionsFragmentViewModel(getHouseSuggestionsUseCase) as T
    }
}