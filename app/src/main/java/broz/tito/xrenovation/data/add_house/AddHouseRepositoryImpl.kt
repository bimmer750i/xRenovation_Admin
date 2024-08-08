package broz.tito.xrenovation.data.add_house

import android.content.Context
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.data.get_houses.entities.GetPointResult
import broz.tito.xrenovation.domain.AddHouseRepository
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddHouseRepositoryImpl @Inject constructor(val model: HouseModel) : AddHouseRepository {

    override fun suggestAddress(context: Context, address: String): Flow<SuggestAddressResult> {
        return model.suggestAddress(context, address)
    }

    override fun searchPoint(point: Point): Flow<SearchPointResult> {
        return model.searchPoint(point)
    }

    override fun loadPhotosToFireBase(localId: String,path : String, list: ArrayList<String>): Flow<LoadPhotosResult> {
        return model.loadPhotosToFireBase(localId,path, list)
    }

    override fun addHouse(localId: String,name: String, house: House,accessToken : String): Flow<AddHouseResult> {
        return model.addHouse(localId,name,house,accessToken)
    }

    override fun addHousePoint(housePoint: HousePoint,houseId: String,accessToken : String): Flow<AddHousePointResult> {
        return model.addHousePoint(housePoint,houseId,accessToken)
    }

    override fun getPoints(): Flow<GetPointResult> {
        return model.getPoints()
    }

    override fun getHouse(houseId: String): Flow<GetHouseResult> {
        return model.getHouse(houseId)
    }

    override fun addComment(localId : String,houseId: String, comment: Comment,accessToken : String): Flow<AddCommentResult> {
        return model.addComment(localId,houseId, comment,accessToken)
    }

    override fun getComments(houseId: String): Flow<GetCommentsResult> {
        return model.getComments(houseId)
    }

    override fun addHouseCorrection(localId : String, houseCorrection: HouseCorrection,accessToken : String): Flow<AddHouseCorrectionResult> {
        return model.addHouseCorrection(localId,houseCorrection,accessToken)
    }

    override fun getAdmin(localId: String): Flow<GetAdminResult> {
        return model.getAdmin(localId)
    }

    override fun editHouse(houseId: String,house : House,accessToken: String): Flow<EditHouseResult> {
        return model.editHouse(houseId,house,accessToken)
    }

    override fun deleteHouse(
        houseId: String,
        accessToken: String
    ): Flow<DeleteHouseResult> {
        return model.deleteHouse(houseId, accessToken)
    }

    override fun deletePoint(houseId: String, accessToken: String): Flow<DeletePointResult> {
        return model.deletePoint(houseId, accessToken)
    }

    override fun deleteHousePhoto(photoList: ArrayList<String>): Flow<DeleteHousePhotoResult> {
        return model.deleteHousePhoto(photoList)
    }

    override fun getCorrections(): Flow<GetCorrectionsResult> {
        return model.getCorrections()
    }

    override fun deleteCorrection(
        correctionId: String,
        accessToken: String
    ): Flow<DeleteCorrectionResult> {
        return model.deleteCorrection(correctionId, accessToken)
    }

    override fun getHouseSuggestions(): Flow<GetHouseSuggestionsResult> {
        return model.getHouseSuggestions()
    }

    override fun deleteSuggestedHouse(suggestedHouseId : String, accessToken: String): Flow<DeleteSuggestedHouseResult> {
        return model.deleteSuggestedHouse(suggestedHouseId, accessToken)
    }

    override fun deleteSuggestedPoint(suggestedHouseId: String, accessToken: String): Flow<DeletePointResult> {
        return model.deleteSuggestedPoint(suggestedHouseId, accessToken)
    }

    override fun getCommentSuggestions(): Flow<GetCommentsResult> {
        return model.getCommentSuggestions()
    }

    override fun deleteSuggestedComment(
        suggestedCommentId: String,
        accessToken: String
    ): Flow<DeleteSuggestedCommentResult> {
        return model.deleteSuggestedComment(suggestedCommentId, accessToken)
    }

    override fun deleteComment(
        houseId: String,
        commentId: String,
        accessToken: String
    ): Flow<DeleteSuggestedCommentResult> {
        return model.deleteComment(houseId, commentId, accessToken)
    }
}