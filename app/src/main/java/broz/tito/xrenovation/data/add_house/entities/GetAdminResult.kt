package broz.tito.xrenovation.data.add_house.entities

open class GetAdminResult

class PendingGetAdminResult : GetAdminResult()

class SuccessGetAdminResult(val admin: Admin) : GetAdminResult()

class FailureGetAdminResult(val errorMessage : String) : GetAdminResult()