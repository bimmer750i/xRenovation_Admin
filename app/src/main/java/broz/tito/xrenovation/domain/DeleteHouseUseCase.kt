package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteHouseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteHouseUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(houseId : String, accessToken : String) : Flow<DeleteHouseResult> {
        return repository.deleteHouse(houseId, accessToken)
    }

}