package broz.tito.xrenovation.data.add_house.entities

open class DeleteHouseResult

class PendingDeleteHouseResult : DeleteHouseResult()

class SuccessDeleteHouseResult : DeleteHouseResult()

class FailureDeleteHouseResult(val errorMessage : String) : DeleteHouseResult()



open class DeletePointResult

class PendingDeletePointResult : DeletePointResult()

class SuccessDeletePointResult : DeletePointResult()

class FailureDeletePointResult(val errorMessage : String) : DeletePointResult()



open class DeleteHousePhotoResult

class PendingDeleteHousePhotoResult : DeleteHousePhotoResult()

class SuccessDeleteHousePhotoResult : DeleteHousePhotoResult()

class FailureDeleteHousePhotoResult(val errorMessage : String) : DeleteHousePhotoResult()



open class DeleteCorrectionResult

class PendingDeleteCorrectionResult : DeleteCorrectionResult()

class SuccessDeleteCorrectionResult : DeleteCorrectionResult()

class FailureDeleteCorrectionResult(val errorMessage : String) : DeleteCorrectionResult()