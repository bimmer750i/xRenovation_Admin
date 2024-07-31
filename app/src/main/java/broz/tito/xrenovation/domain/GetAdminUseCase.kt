package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.GetAdminResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdminUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke(localId : String) : Flow<GetAdminResult> {
        return repository.getAdmin(localId)
    }

}