package uz.star.testforanymobile.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import uz.star.testforanymobile.ui.screen.bookmark.BookMarkRepository
import uz.star.testforanymobile.ui.screen.bookmark.BookMarkRepositoryImpl
import uz.star.testforanymobile.ui.screen.map.MapRepository
import uz.star.testforanymobile.ui.screen.map.MapRepositoryImpl
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun getMapFragmentRepository(repo: MapRepositoryImpl): MapRepository

    @Binds
    @Singleton
    fun getBookMarkRepository(repo: BookMarkRepositoryImpl): BookMarkRepository

}