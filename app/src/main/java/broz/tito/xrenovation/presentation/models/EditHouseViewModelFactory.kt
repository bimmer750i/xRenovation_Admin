package broz.tito.xrenovation.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import broz.tito.xrenovation.data.sharedprefs.SharedPrefsModel
import broz.tito.xrenovation.domain.*
import javax.inject.Inject

class EditHouseViewModelFactory @Inject constructor(
    val getAccountInfoUseCase: GetAccountInfoUseCase,
    val refreshTokenUseCase: RefreshTokenUseCase,
    val saveAuthResponseUseCase: SaveAuthResponseUseCase,
    val sharedPrefsModel: SharedPrefsModel,
    val suggestAddressUseCase: SuggestAddressUseCase,
    val searchPointUseCase: SearchPointUseCase,
    val loadPhotosToFireBaseUseCase: LoadPhotosToFireBaseUseCase,
    val addHouseUseCase: AddHouseUseCase,
    val addHousePointUseCase: AddHousePointUseCase,
    val editHouseUseCase: EditHouseUseCase,
    val deleteHousePhotoUseCase: DeleteHousePhotoUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditHouseViewModel(getAccountInfoUseCase,refreshTokenUseCase, saveAuthResponseUseCase, sharedPrefsModel, suggestAddressUseCase,searchPointUseCase,loadPhotosToFireBaseUseCase,addHouseUseCase,addHousePointUseCase,editHouseUseCase,deleteHousePhotoUseCase) as T
    }
}