package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteSuggestedCommentResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(private val repository: AddHouseRepository) {

    operator fun invoke(houseId: String,commentId : String,accessToken: String) : Flow<DeleteSuggestedCommentResult> {
        return repository.deleteComment(houseId, commentId, accessToken)
    }


}