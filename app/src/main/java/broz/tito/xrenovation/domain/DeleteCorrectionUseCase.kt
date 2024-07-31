package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteCorrectionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCorrectionUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(correctionId : String, accessToken : String) : Flow<DeleteCorrectionResult> {
        return repository.deleteCorrection(correctionId, accessToken)
    }

}