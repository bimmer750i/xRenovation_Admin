package broz.tito.xrenovation.data.add_house.entities

open class DeleteSuggestedCommentResult

class PendingDeleteSuggestedCommentResult : DeleteSuggestedCommentResult()
class SuccessDeleteSuggestedCommentResult : DeleteSuggestedCommentResult()
class FailureDeleteSuggestedCommentResult(val errorMessage : String) : DeleteSuggestedCommentResult()