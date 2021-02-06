package uz.star.testforanymobile.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import uz.star.testforanymobile.data.local.LocalStorage

/**
 * Created by Farhod Tohirov on 03-Feb-21
 **/

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MapKitFactory.setApiKey("522fb9ba-acc3-4c2a-ad64-371448cace44")
        LocalStorage.init(this)
    }

    companion object{
        lateinit var instance: App
    }
}