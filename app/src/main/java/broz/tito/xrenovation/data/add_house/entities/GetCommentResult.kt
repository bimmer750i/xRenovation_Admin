package broz.tito.xrenovation.data.add_house.entities

import broz.tito.xrenovation.presentation.entities.DisplayComment
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import broz.tito.xrenovation.presentation.entities.DisplayHouseSuggestion
import com.google.gson.JsonObject
import org.json.JSONObject

open class GetCommentsResult

class PendingGetCommentsResult : GetCommentsResult()

class RawSuccessGetCommentsResult(val jsonObject: JsonObject) : GetCommentsResult()

class SuccessGetCommentsResult(val commentsList : ArrayList<DisplayComment>) : GetCommentsResult()

class FailureGetCommentsResult(val errorMessage : String) : GetCommentsResult()



open class GetCorrectionsResult

class PendingGetCorrectionsResult : GetCorrectionsResult()

class RawSuccessGetCorrectionsResult(val jsonObject: JsonObject) : GetCorrectionsResult()

class SuccessGetCorrectionsResult(val correctionsList : ArrayList<DisplayCorrection>) : GetCorrectionsResult()

class FailureGetCorrectionsResult(val errorMessage : String) : GetCorrectionsResult()


open class GetHouseSuggestionsResult

class PendingGetHouseSuggestionsResult : GetHouseSuggestionsResult()

class RawSuccessGetHouseSuggestionsResult(val jsonObject: JsonObject) : GetHouseSuggestionsResult()

class SuccessGetHouseSuggestionsResult(val houseSuggestionsList : ArrayList<DisplayHouseSuggestion>) : GetHouseSuggestionsResult()

class FailureGetHouseSuggestionsResult(val errorMessage: String) : GetHouseSuggestionsResult()