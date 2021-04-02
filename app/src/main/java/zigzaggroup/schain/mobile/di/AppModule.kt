package zigzaggroup.schain.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import zigzaggroup.schain.mobile.data.ItemApi
import zigzaggroup.schain.mobile.utils.Constants.Companion.BASE_URL
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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(ItemApi::class.java)
    }
}