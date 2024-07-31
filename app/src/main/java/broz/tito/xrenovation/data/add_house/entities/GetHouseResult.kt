package broz.tito.xrenovation.data.add_house.entities

open class GetHouseResult

class PendingGetHouseResult : GetHouseResult()

class SuccessGetHouseResult(val houseId: String, val house : House) : GetHouseResult()

class FailureGetHouseResult(val errorMessage : String) : GetHouseResult()




open class EditHouseResult

class PendingEditHouseResult : EditHouseResult()

class SuccessEditHouseResult(val house : House) : EditHouseResult()

class FailureEditHouseResult(val errorMessage : String) : EditHouseResult()