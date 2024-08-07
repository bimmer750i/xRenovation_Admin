package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.DeleteSuggestedCommentResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteSuggestedCommentUseCase @Inject constructor(private val repository: AddHouseRepository) {

    operator fun invoke(suggestedCommentId: String,accessToken: String) : Flow<DeleteSuggestedCommentResult> {
        return repository.deleteSuggestedComment(suggestedCommentId, accessToken)
    }

}