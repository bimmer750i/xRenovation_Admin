package broz.tito.xrenovation.domain

import android.content.Context
import broz.tito.xrenovation.data.add_house.entities.DeletePointResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePointUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(houseId : String, accessToken : String) : Flow<DeletePointResult> {
        return repository.deletePoint(houseId,accessToken)
    }

}