package com.example.ExoPlayerApp.di

import android.content.Context
import com.example.ExoPlayerApp.util.Constants
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExoModule {
    /**
     * Provides the user agent for the device
     *
     * @param context represents an instance of [Context]
     * @return an instance of user agent
     */
    @Singleton
    @Provides
    @Named("userAgent")
    fun provideUserAgent(@ApplicationContext context: Context): String {
        return Util.getUserAgent(context, Constants.EXO_PLAYER_USER_AGENT)
    }

    @Singleton
    @Provides
    fun provideMediaItem(): MediaItem.Builder {
        return MediaItem.Builder()
    }

    /**
     * Provides an instance of [DefaultBandwidthMeter]
     *
     * @return an instance of [DefaultBandwidthMeter]
     */
    @Singleton
    @Provides
    fun provideDefaultBandwidthMeter(@ApplicationContext context: Context): DefaultBandwidthMeter {
        return DefaultBandwidthMeter.Builder(context).build()
    }

    @Singleton
    @Provides
    fun provideTrackSelectionFactory(): AdaptiveTrackSelection.Factory {
        return AdaptiveTrackSelection.Factory()
    }

    /**
     * Provides an instance of [DefaultTrackSelector]
     *
     * @param factory represents an instance of [TrackSelection.Factory]
     * @return an instance of [DefaultTrackSelector]
     */
    @Reusable
    @Provides
    fun provideDefaultTrackSelector(factory: AdaptiveTrackSelection.Factory): DefaultTrackSelector {
        return DefaultTrackSelector(factory)
    }


    /**
     * Provides an instance of [DefaultRenderersFactory]
     *
     * @param context represents an instance of [Context]
     * @return an instance of [DefaultRenderersFactory]
     */
    @Reusable
    @Provides
    fun provideDefaultRenderersFactory(@ApplicationContext context: Context): DefaultRenderersFactory {
        return DefaultRenderersFactory(context)
    }

    /**
     * Provides an instance of [LoadControl]
     *
     * @return an instance of [LoadControl]
     */
    @Reusable
    @Provides
    fun provideLoadControl(): LoadControl {
        return DefaultLoadControl()
    }

    /**
     * Provides an instance of [AudioAttributes]
     * This is used to interrupt audio in case of getting
     * call or playing a music.
     *
     * @return an instance of [AudioAttributes]
     */
    @Singleton
    @Provides
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder().apply {
            setUsage(C.USAGE_MEDIA)
            setContentType(C.CONTENT_TYPE_SPEECH)
        }.build()
    }

    /**
     * Provides an instance of [SimpleExoPlayer]
     *
     * @param context          represents an instance of [Context]
     * @param renderersFactory represents an instance of [DefaultRenderersFactory]
     * @param trackSelector    represents an instance of [DefaultTrackSelector]
     * @param loadControl      represents an instance of [DefaultLoadControl]
     * @param audioAttributes  represents an instance of [AudioAttributes]
     * @return an instance of [SimpleExoPlayer]
     */
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context,
    ): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context).build()
    }

    @Reusable
    @Provides
    fun provideDataSourceFactory(cacheDataSource: CacheDataSource): DataSource.Factory {
        return DefaultCacheDataSourceFactory(cacheDataSource)
    }

    @Reusable
    @Provides
    fun provideCacheDataSource(
        simpleCache: SimpleCache,
        factory: DefaultDataSourceFactory,
        cacheDataSink: CacheDataSink
    ): CacheDataSource {
        return CacheDataSource(
            simpleCache, factory.createDataSource(), FileDataSource(), cacheDataSink,
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }

    @Reusable
    @Provides
    fun provideCacheDataSink(simpleCache: SimpleCache): CacheDataSink {
        return CacheDataSink(simpleCache, Constants.EXO_PLAYER_VIDEO_CACHE_DURATION.toLong())
    }

    @Singleton
    @Provides
    fun provideSimpleCache(
        @ApplicationContext context: Context,
        evictor: LeastRecentlyUsedCacheEvictor,
        databaseProvider: DatabaseProvider
    ): SimpleCache {
        return SimpleCache(File(context.cacheDir, "media_cache"), evictor, databaseProvider)
    }

    /**
     * Provides an instance of [DefaultDataSourceFactory]
     *
     * @param context        represents an instance of [Context]
     * @param bandwidthMeter represents an instance of [DefaultBandwidthMeter]
     * @param factory        represents an instance of [HttpDataSource.Factory]
     * @return an instance of [DefaultDataSourceFactory]
     */
    @Reusable
    @Provides
    fun provideDefaultDataSourceFactory(
        @ApplicationContext context: Context,
        bandwidthMeter: DefaultBandwidthMeter,
        factory: HttpDataSource.Factory,
    ): DefaultDataSourceFactory {
        return DefaultDataSourceFactory(context, bandwidthMeter, factory)
    }

    @Singleton
    @Provides
    fun provideHttpDataSourceFactory(
        @Named("userAgent")
        userAgent: String,
        bandwidthMeter: DefaultBandwidthMeter
    ): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
    }

    /**
     * Provides an instance of [LeastRecentlyUsedCacheEvictor]
     *
     * @return an instance of [LeastRecentlyUsedCacheEvictor]
     */
    @Reusable
    @Provides
    fun provideLeastRecentlyUsedCacheEvictor(): LeastRecentlyUsedCacheEvictor {
        return LeastRecentlyUsedCacheEvictor(Constants.EXO_PLAYER_VIDEO_CACHE_DURATION.toLong())
    }


    /**
     * Returns an instance of [ProgressiveMediaSource.Factory]
     *
     * @param factory represents an instance of [DataSource.Factory]
     * @return an instance of [ProgressiveMediaSource.Factory]
     */
    @Reusable
    @Provides
    fun provideProgressiveMediaSourceFactory(factory: DataSource.Factory): ProgressiveMediaSource.Factory {
        return ProgressiveMediaSource.Factory(factory)
    }

    @Provides
    @Reusable
    fun provideHlsMediaSourceFactory(factory: DataSource.Factory): HlsMediaSource.Factory {
        return HlsMediaSource.Factory(factory)
    }

    /**
     * Provides an instance of [ExoDatabaseProvider]
     *
     * @param context represents an instance of [Context]
     * @return an instance of [ExoDatabaseProvider]
     */
    @Singleton
    @Provides
    fun provideDatabaseProvider(@ApplicationContext context: Context): DatabaseProvider {
        return ExoDatabaseProvider(context)
    }

}

