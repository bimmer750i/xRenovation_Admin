package broz.tito.xrenovation.data.add_house.entities

import com.google.gson.annotations.SerializedName

data class Admin (

    @SerializedName("localId" ) var localId : String?  = null,
    @SerializedName("isAdmin" ) var isAdmin : Boolean? = null

)