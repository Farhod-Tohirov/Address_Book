package uz.star.testforanymobile.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import uz.star.testforanymobile.data.room.AppDatabase
import uz.star.testforanymobile.data.room.dao.PlaceModelDao
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

@Module
@InstallIn(ApplicationComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun getDataBase(): AppDatabase = AppDatabase.getDatabase()

    @Provides
    @Singleton
    fun getPlaceModelDao(appDatabase: AppDatabase): PlaceModelDao = appDatabase.placeModelDao()

}