package uz.star.testforanymobile.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import uz.star.testforanymobile.app.App
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun getApp(): Context = App.instance
}