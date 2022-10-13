package org.listenbrainz.android.data.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.listenbrainz.android.data.repository.*

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class BlogRepositoryModule {
    @Binds
    abstract fun bindsBlogRepository(repository: BlogRepositoryImpl?): BlogRepository?
}