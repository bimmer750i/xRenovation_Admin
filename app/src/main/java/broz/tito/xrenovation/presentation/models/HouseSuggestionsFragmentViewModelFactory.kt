package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.DeleteHousePhotoUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedHouseUseCase
import broz.tito.xrenovation.domain.DeleteSuggestedPointUseCase
import broz.tito.xrenovation.domain.GetHouseSuggestionsUseCase
import javax.inject.Inject

class HouseSuggestionsFragmentViewModelFactory @Inject constructor(val sharedPrefsModel: SharedPrefsModel,
                                                                   val getHouseSuggestionsUseCase: GetHouseSuggestionsUseCase,
                                                                   val deleteHousePhotoUseCase: DeleteHousePhotoUseCase,
                                                                   val deleteSuggestedHouseUseCase: DeleteSuggestedHouseUseCase,
                                                                   val deleteSuggestedPointUseCase : DeleteSuggestedPointUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HouseSuggestionsFragmentViewModel(sharedPrefsModel, getHouseSuggestionsUseCase, deleteHousePhotoUseCase, deleteSuggestedHouseUseCase, deleteSuggestedPointUseCase) as T
    }
}