package broz.tito.xrenovation.domain

import android.util.Log
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import broz.tito.xrenovation.presentation.entities.DisplayHouseSuggestion
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHouseSuggestionsUseCase @Inject constructor(val repository: AddHouseRepository) {

    operator fun invoke() : Flow<GetHouseSuggestionsResult> {
        return repository.getHouseSuggestions().map {
            val result = ArrayList<DisplayHouseSuggestion>()
            if (it is RawSuccessGetHouseSuggestionsResult) {
                it.jsonObject.entrySet().forEach {
                    result.add(
                        DisplayHouseSuggestion(it.key,
                            Gson().fromJson(it.value.toString(), House::class.java))
                    )

                }
            }
            SuccessGetHouseSuggestionsResult(result)
        }
    }

}