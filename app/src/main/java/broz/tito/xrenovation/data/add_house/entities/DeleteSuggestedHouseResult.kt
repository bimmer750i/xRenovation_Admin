package broz.tito.xrenovation.data.add_house.entities

open class DeleteSuggestedHouseResult

class PendingDeleteSuggestedHouseResult : DeleteSuggestedHouseResult()

class SuccessDeleteSuggestedHouseResult : DeleteSuggestedHouseResult()

class FailureDeleteSuggestedHouseResult(val errorMessage : String) : DeleteSuggestedHouseResult()