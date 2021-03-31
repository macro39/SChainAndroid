package zigzaggroup.schain.mobile

import android.app.Application
import com.crazylegend.kotlinextensions.misc.disableNightMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SchainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        disableNightMode()  // TODO remove after dark theme optimization
        Timber.plant(Timber.DebugTree())
    }

}