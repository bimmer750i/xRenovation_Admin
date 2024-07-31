package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.DeleteCorrectionUseCase
import broz.tito.xrenovation.domain.GetCorrectionsUseCase
import broz.tito.xrenovation.domain.GetHouseUseCase
import javax.inject.Inject

class CorrectionsFragmentViewModelFactory @Inject constructor(val getCorrectionsUseCase: GetCorrectionsUseCase,
                                                              val sharedPrefsModel: SharedPrefsModel,
                                                              val getHouseUseCase: GetHouseUseCase,
                                                              val deleteCorrectionUseCase: DeleteCorrectionUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CorrectionsFragmentViewModel(getCorrectionsUseCase,sharedPrefsModel,getHouseUseCase,deleteCorrectionUseCase) as T
    }
}