package zigzaggroup.schain.mobile.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.data.models.ItemHistory

interface ItemApi {

    @GET("api/client/item/{id}")
    suspend fun getItem(@Path("id") id: String): Response<Item>

    @GET("api/client/item/history/{id}")
    suspend fun getItemHistory(@Path("id") id: String): Response<ItemHistory>

}