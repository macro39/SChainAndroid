package zigzaggroup.schain.mobile.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import zigzaggroup.schain.mobile.BuildConfig
import zigzaggroup.schain.mobile.data.ItemApi
import zigzaggroup.schain.mobile.di.qualifiers.EncryptedPrefs
import zigzaggroup.schain.mobile.utils.Constants.SHARED_PREFS
import zigzaggroup.schain.mobile.utils.NetworkConnectionInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideItemApi(@ApplicationContext context: Context): ItemApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context))

        Timber.d(BuildConfig.API_BASE_URL)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(ItemApi::class.java)
    }

    @Provides
    @EncryptedPrefs
    @Singleton
    fun encryptedDefaultSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, 0)
}