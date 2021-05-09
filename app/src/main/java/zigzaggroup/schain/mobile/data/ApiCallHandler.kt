package zigzaggroup.schain.mobile.data

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Response
import timber.log.Timber
import zigzaggroup.schain.mobile.data.models.*
import zigzaggroup.schain.mobile.ui.MainActivity
import zigzaggroup.schain.mobile.utils.PrefsProvider
import zigzaggroup.schain.mobile.utils.toToken
import javax.inject.Inject


@ActivityScoped
class ApiCallHandler @Inject constructor(
    private val api: ItemApi,
    @ActivityContext
    private val context: Context
) {

    @Inject
    lateinit var prefsProvider: PrefsProvider

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return try {
            val result = response.body()
            Timber.d(response.toString())

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

                var errorMessage = error.message.split("Error: ").last()

                if (response.code() == 401 && errorMessage == "Authentication token has expired") {
                    prefsProvider.setLoggedUser(null)
                    errorMessage += " , please log in again!"
                    (context as MainActivity).reinitializeApp()
                }

                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }

    suspend fun login(login: Credentials): Resource<LoggedUser> {
        return handleResponse(api.login(login))
    }

    suspend fun getItem(id: String): Resource<Item> {
        return handleResponse(
            api.getItem(
                id,
                prefsProvider.loggedUser?.token?.toToken() ?: ""
            )
        )
    }

    suspend fun getItemHistory(id: String): Resource<ItemHistory> {
        return handleResponse(
            api.getItemHistory(
                id,
                prefsProvider.loggedUser?.token?.toToken() ?: ""
            )
        )
    }

    suspend fun getScanHistory(): Resource<ScanHistoryBase> {
        val user = prefsProvider.loggedUser

        return if (user != null) {
            handleResponse(api.getScanHistory(user.token.toToken()))
        } else {
            Resource.Error("Log in to perform this operation")
        }
    }
}