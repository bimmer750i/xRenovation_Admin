package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeletePointResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteSuggestedPointUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(suggestedHouseId: String, accessToken: String) : Flow<DeletePointResult> {
        return repository.deleteSuggestedPoint(suggestedHouseId,accessToken)
    }


}