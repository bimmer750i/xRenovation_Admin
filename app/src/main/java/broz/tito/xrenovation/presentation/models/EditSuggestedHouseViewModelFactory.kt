package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import javax.inject.Inject

class EditSuggestedHouseViewModelFactory @Inject constructor(val getAccountInfoUseCase: GetAccountInfoUseCase,
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
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditSuggestedHouseViewModel(getAccountInfoUseCase,
            refreshTokenUseCase,
            saveAuthResponseUseCase,
            sharedPrefsModel,
            suggestAddressUseCase,
            searchPointUseCase,
            loadPhotosToFireBaseUseCase,
            addHouseUseCase, addHousePointUseCase, editHouseUseCase, deleteHousePhotoUseCase, deleteSuggestedHouseUseCase) as T
    }
}