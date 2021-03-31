package zigzaggroup.schain.mobile.main

import com.google.gson.Gson
import retrofit2.Response
import zigzaggroup.schain.mobile.data.ItemApi
import zigzaggroup.schain.mobile.data.models.Error
import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.data.models.ItemHistory
import zigzaggroup.schain.mobile.utils.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: ItemApi
) : MainRepository {

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return try {
            val result = response.body()

            if (response.code() == 200) {
                if (result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error("Empty response")
                }
            } else {
                val errBody = response.errorBody()!!.string()

                val gson = Gson()
                val error = gson.fromJson(errBody, Error::class.java)

                Resource.Error(error.message.split("Error: ").last())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }

    override suspend fun getItem(id: String): Resource<Item> {
        return handleResponse(api.getItem(id))
    }

    override suspend fun getItemHistory(id: String): Resource<ItemHistory> {
        return handleResponse(api.getItemHistory(id))
    }
}