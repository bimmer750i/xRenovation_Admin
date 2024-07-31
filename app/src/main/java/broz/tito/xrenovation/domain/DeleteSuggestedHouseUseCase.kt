package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteSuggestedHouseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteSuggestedHouseUseCase @Inject constructor(private val repository: AddHouseRepository) {

    operator fun invoke(suggestedHouseId : String, accessToken: String) : Flow<DeleteSuggestedHouseResult> {
        return repository.deleteSuggestedHouse(suggestedHouseId, accessToken)
    }

}