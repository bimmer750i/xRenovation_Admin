package broz.tito.xrenovation.domain

import android.content.Context
import broz.tito.xrenovation.data.add_house.entities.*
import broz.tito.xrenovation.data.get_houses.entities.GetPointResult
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.Flow

interface AddHouseRepository {

    fun suggestAddress(context : Context, address: String) : Flow<SuggestAddressResult>

    fun searchPoint(point: Point) : Flow<SearchPointResult>

    fun loadPhotosToFireBase(localId: String,path : String, list : ArrayList<String>) : Flow<LoadPhotosResult>

    fun addHouse(localId: String,name : String, house: House,accessToken : String) : Flow<AddHouseResult>

    fun addHousePoint(housePoint: HousePoint, houseId : String, accessToken : String) : Flow<AddHousePointResult>

    fun getPoints() : Flow<GetPointResult>

    fun getHouse(houseId : String) : Flow<GetHouseResult>

    fun addComment(localId : String,houseId: String,comment: Comment,accessToken : String) : Flow<AddCommentResult>

    fun getComments(houseId: String) : Flow<GetCommentsResult>

    fun addHouseCorrection(localId : String,houseCorrection: HouseCorrection,accessToken : String) : Flow<AddHouseCorrectionResult>

    fun getAdmin(localId: String) : Flow<GetAdminResult>

    fun editHouse(houseId: String,house : House,accessToken: String) : Flow<EditHouseResult>

    fun deleteHouse(houseId: String,accessToken: String) : Flow<DeleteHouseResult>

    fun deletePoint(houseId: String,accessToken: String) : Flow<DeletePointResult>

    fun deleteHousePhoto(photoList : ArrayList<String>) : Flow<DeleteHousePhotoResult>

    fun getCorrections() : Flow<GetCorrectionsResult>

    fun deleteCorrection(correctionId : String, accessToken: String) : Flow<DeleteCorrectionResult>

    fun getHouseSuggestions() : Flow<GetHouseSuggestionsResult>

    fun deleteSuggestedHouse(suggestedHouseId : String, accessToken: String) : Flow<DeleteSuggestedHouseResult>

    fun deleteSuggestedPoint(suggestedHouseId : String, accessToken: String) : Flow<DeletePointResult>

    fun getCommentSuggestions() : Flow<GetCommentsResult>

    fun deleteSuggestedComment(suggestedCommentId: String,accessToken: String) : Flow<DeleteSuggestedCommentResult>

}