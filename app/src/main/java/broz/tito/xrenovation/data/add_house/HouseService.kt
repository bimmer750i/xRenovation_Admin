package broz.tito.xrenovation.data.add_house

import broz.tito.xrenovation.data.add_house.entities.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface HouseService {

    @GET("points.json")
    suspend fun getPoints() : Response<JsonObject>

    @GET("houses/{houseId}.json")
    suspend fun getHouse(@Path("houseId") houseId : String) : Response<House>

    @GET("comments/comments{houseId}.json")
    suspend fun getComments(@Path("houseId") houseId : String) : Response<JsonElement>

    @GET("adlist/{localId}.json")
    suspend fun getAdmin(@Path("localId") localId : String) : Response<Admin>

    @GET("timePosted/{localId}.json")
    suspend fun getLastTimePosted(@Path("localId") localId : String) : Response<LastTimePosted>

    @GET("corrections.json")
    suspend fun getCorrections() : Response<JsonElement>

    @GET("houses-suggestions.json")
    suspend fun getHouseSuggestions() : Response<JsonElement>

    @POST("comments/comments{houseId}.json")
    suspend fun addComment(@Path("houseId") houseId : String, @Body comment: Comment, @Query("auth") accessToken : String) : Response<AddCommentResponse>

    @POST("corrections.json")
    suspend fun addHouseCorrection(@Body houseCorrection: HouseCorrection, @Query("auth") accessToken : String) : Response<AddHouseCorrectionResponse>

    @POST("houses.json")
    suspend fun addHouse(@Body body : House, @Query("auth") accessToken : String) : Response<AddHouseResponse>

    @PUT("points/{houseId}.json")
    suspend fun addPoint(@Body body : HousePoint, @Path("houseId") houseId : String, @Query("auth") accessToken : String) : Response<AddHousePointResponse>

    @PUT("timePosted/{localId}.json")
    suspend fun addLastTimePosted(@Path("localId") localId : String, @Body body : LastTimePosted, @Query("auth") accessToken : String) : Response<LastTimePosted>

    @PUT("houses/{houseId}.json")
    suspend fun editHouse(@Path("houseId") houseId : String, @Body body : House, @Query("auth") accessToken : String) : Response<House>

    @PUT("points/{houseId}.json")
    suspend fun editPoint(@Body body : HousePoint, @Path("houseId") houseId : String, @Query("auth") accessToken : String) : Response<AddHousePointResponse>

    @DELETE("houses/{houseId}.json")
    suspend fun deleteHouse(@Path("houseId") houseId : String, @Query("auth") accessToken : String) : Response<JsonElement>

    @DELETE("points/{houseId}.json")
    suspend fun deletePoint(@Path("houseId") houseId : String, @Query("auth") accessToken : String) : Response<JsonElement>

    @DELETE("corrections/{correctionId}.json")
    suspend fun deleteCorrection(@Path("correctionId") correctionId : String, @Query("auth") accessToken : String) : Response<JsonElement>

    @DELETE("houses-suggestions/{suggestedHouseId}.json")
    suspend fun deleteSuggestedHouse(@Path("suggestedHouseId") suggestedHouseId : String, @Query("auth") accessToken : String) : Response<JsonElement>
}