package com.example.erlabapp.util

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import javax.inject.Inject
import javax.inject.Provider

class ExoUtil @Inject constructor(
    private val exoPlayer: SimpleExoPlayer,
    val factory: ProgressiveMediaSource.Factory,
    private val mediaItemBuilder: MediaItem.Builder
) : Player.EventListener {
     var simpleExoPlayer: SimpleExoPlayer? = null

    // private var mPlayerStateListener: PlayerStateListener? = null
    private var mPlayerView: PlayerView? = null

    private var mShouldAutoPlay: Boolean = true
    private var mUrl: String? = null

    private fun mediaItem(uri: Uri?): MediaItem {
        return mediaItemBuilder.setUri(uri).build()
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return factory.createMediaSource(mediaItem(uri))
    }

    private fun preparePlayer(url: String) {
        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        simpleExoPlayer = exoPlayer
        simpleExoPlayer?.setMediaSource(mediaSource)
        simpleExoPlayer?.prepare()
        Log.i("EXOPLAYER", "Exo has been prepared")
    }

    private fun initializePlayer() {
        preparePlayer(mUrl!!)
        mPlayerView?.player = simpleExoPlayer
        simpleExoPlayer?.seekTo(0)
        simpleExoPlayer?.playWhenReady = mShouldAutoPlay
        simpleExoPlayer?.addListener(this@ExoUtil)
        Log.i("EXOPLAYER", "Exo has been initialized")
    }

    private fun releasePlayer() {
        simpleExoPlayer?.stop()
        simpleExoPlayer?.release()
        simpleExoPlayer?.removeListener(this)
        simpleExoPlayer = null
    }

    fun setPlayerView(playerView: PlayerView) {
        mPlayerView = playerView
        Log.i("EXOPLAYER", "playerview has been set")
    }

    fun setUrl(url: String) {
        mUrl = url
        Log.i("EXOPLAYER", "url has been prepared")
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Log.e("EXOPLAYER", error.toString())
    }

    fun onStart() {
        initializePlayer()
        mPlayerView?.onResume()
    }

    fun onResume() {
        initializePlayer()
        mPlayerView?.onResume()
    }

    fun onPause() {
        releasePlayer()
        mPlayerView?.onPause()
    }

    fun onStop() {
        releasePlayer()
        mPlayerView?.onPause()
    }
}
