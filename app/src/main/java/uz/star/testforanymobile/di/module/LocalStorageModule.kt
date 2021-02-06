package uz.star.testforanymobile.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import uz.star.testforanymobile.data.local.LocalStorage
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

@Module
@InstallIn(ApplicationComponent::class)
class LocalStorageModule {
    @Provides
    @Singleton
    fun getLocalStorage(): LocalStorage = LocalStorage.instance
}