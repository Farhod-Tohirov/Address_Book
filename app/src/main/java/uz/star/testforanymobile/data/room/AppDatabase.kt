package uz.star.testforanymobile.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.star.testforanymobile.app.App
import uz.star.testforanymobile.data.room.dao.PlaceModelDao
import uz.star.testforanymobile.data.room.entity.PlaceModel

@Database(entities = [PlaceModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeModelDao(): PlaceModelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.instance.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}