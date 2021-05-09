package zigzaggroup.schain.mobile.utils

import android.content.SharedPreferences
import com.crazylegend.kotlinextensions.sharedprefs.putString
import com.google.gson.Gson
import zigzaggroup.schain.mobile.data.models.LoggedUser
import zigzaggroup.schain.mobile.di.qualifiers.EncryptedPrefs
import zigzaggroup.schain.mobile.di.qualifiers.LOGGED_USER_PREF_KEY
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsProvider @Inject constructor(
    @EncryptedPrefs
    private val defaultPrefs: SharedPreferences
) {

    val loggedUser: LoggedUser?
        get() = run {
            val json = defaultPrefs.getString(LOGGED_USER_PREF_KEY, null)

            val gson = Gson()
            return gson.fromJson(json, LoggedUser::class.java)
        }

    fun setLoggedUser(user: LoggedUser?) {
        val gson = Gson()
        val json = gson.toJson(user)
        defaultPrefs.putString(LOGGED_USER_PREF_KEY, json)
    }

}