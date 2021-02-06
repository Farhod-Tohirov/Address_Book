package uz.star.testforanymobile.data.local

import android.content.Context
import android.content.SharedPreferences
import uz.star.testforanymobile.utils.DoublePreference

class LocalStorage private constructor(context: Context) {
    companion object {
        @Volatile
        lateinit var instance: LocalStorage; private set

        fun init(context: Context) {
            synchronized(this) {
                instance = LocalStorage(context)
            }
        }
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var currentLat by DoublePreference(pref, 0.0)
    var currentLong by DoublePreference(pref, 0.0)
}