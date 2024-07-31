package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteHousePhotoResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteHousePhotoUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(photoList : ArrayList<String>) : Flow<DeleteHousePhotoResult> {
        return repository.deleteHousePhoto(photoList)
    }

}