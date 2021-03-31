package zigzaggroup.schain.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import zigzaggroup.schain.mobile.data.ItemApi
import zigzaggroup.schain.mobile.main.DefaultMainRepository
import zigzaggroup.schain.mobile.main.MainRepository
import zigzaggroup.schain.mobile.ui.NetworkConnectionInterceptor
import zigzaggroup.schain.mobile.utils.Constants.Companion.BASE_URL
import zigzaggroup.schain.mobile.utils.DispatcherProvider
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

    @Singleton
    @Provides
    fun provideMainRepository(api: ItemApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}