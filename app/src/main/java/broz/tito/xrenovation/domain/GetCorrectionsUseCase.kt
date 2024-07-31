package broz.tito.xrenovation.domain

import android.util.Log
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.presentation.entities.DisplayComment
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCorrectionsUseCase @Inject constructor(private val repository: AddHouseRepository) {

    private val TAG = "GetCorrectionUseCase"

    operator fun invoke() : Flow<GetCorrectionsResult> {
        return repository.getCorrections().map {
            val result = ArrayList<DisplayCorrection>()
            if (it is RawSuccessGetCorrectionsResult) {
                it.jsonObject.entrySet().forEach {
                    result.add(
                        DisplayCorrection(it.key,
                        Gson().fromJson(it.value.toString(), HouseCorrection::class.java))
                    )
                    Log.d(TAG, "${it.key} === ${it.value}")
                }
            }
            Log.d(TAG, "result: $result")
            SuccessGetCorrectionsResult(result)
        }
    }

}