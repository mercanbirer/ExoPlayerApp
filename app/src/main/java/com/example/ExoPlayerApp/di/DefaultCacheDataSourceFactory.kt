package com.example.ExoPlayerApp.di

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

class DefaultCacheDataSourceFactory(private val cacheDataSource: CacheDataSource) :
    DataSource.Factory {
    override fun createDataSource(): DataSource {
        return cacheDataSource
    }
}
