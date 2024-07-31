package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.EditHouseResult
import broz.tito.xrenovation.data.add_house.entities.House
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditHouseUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(houseId : String, house : House, accessToken : String) : Flow<EditHouseResult> {
        return repository.editHouse(houseId,house,accessToken)
    }

}