package broz.tito.xrenovation.domain

import broz.tito.xrenovation.data.add_house.entities.Comment
import broz.tito.xrenovation.data.add_house.entities.GetCommentsResult
import broz.tito.xrenovation.data.add_house.entities.RawSuccessGetCommentsResult
import broz.tito.xrenovation.data.add_house.entities.SuccessGetCommentsResult
import broz.tito.xrenovation.presentation.entities.DisplayComment
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCommentSuggestionsUseCase @Inject constructor(private val repository: AddHouseRepository) {

    operator fun invoke() : Flow<GetCommentsResult> {
        return repository.getCommentSuggestions().map {
            val result = ArrayList<DisplayComment>()
            if (it is RawSuccessGetCommentsResult) {
                it.jsonObject.entrySet().forEach {
                    result.add(
                        DisplayComment(it.key,
                            Gson().fromJson(it.value.toString(), Comment::class.java))
                    )
                }
            }
            SuccessGetCommentsResult(result) }
    }


}