package zigzaggroup.schain.mobile.data

import retrofit2.Response
import retrofit2.http.*
import zigzaggroup.schain.mobile.data.models.*

interface ItemApi {

    @POST("auth/login")
    suspend fun login(@Body login: Credentials): Response<LoggedUser>

    @GET("api/client/item/{id}")
    suspend fun getItem(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Item>

    @GET("api/client/item/history/{id}")
    suspend fun getItemHistory(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ItemHistory>

    @GET("/api/client/item/scanned")
    suspend fun getScanHistory(@Header("Authorization") token: String): Response<ScanHistoryBase>
}