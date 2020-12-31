package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.SoundtrackMetadataDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.repository.SoundtrackRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.utils.PlaybackStatus
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class SoundtrackPlayerService():
    Service(),
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener {

    @Inject
    lateinit var soundtrackRepository: SoundtrackRepository

    private var player: MediaPlayer? = null
    private var currentSoundtrackMetadata: SoundtrackMetadataDao? = null
    private var currentSoundtrackMetadataIndex: Int? = null
    private var currentSoundtrackResumePosition: Int? = null

    private fun initializePlayer() {
        player = MediaPlayer()
        player!!.setOnCompletionListener(this)
        player!!.setOnErrorListener(this)
        player!!.setOnPreparedListener(this)
        preparePlayer()
    }

    private fun preparePlayer() {
        player!!.reset()

        player!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        try {
            player!!.setDataSource(WarcraftInfoConstant.TARANZHI_API_URI + currentSoundtrackMetadata!!.url)
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        }
        player!!.prepareAsync()
    }

    private fun startPlayer() {
        if (player != null) {
            player!!.start()
        }
    }

    private fun stopPlayer() {
        if (player != null) {
            player!!.stop()
        }
    }

    private fun pausePlayer() {
        if (player != null && player!!.isPlaying) {
            player!!.pause()
            currentSoundtrackResumePosition = player!!.currentPosition
            if (mediaSession != null) {
                buildNotification(PlaybackStatus.PAUSED)
            }
        }
    }

    private fun resumePlayer() {
        if (player != null && !player!!.isPlaying && currentSoundtrackResumePosition != null) {
            player!!.seekTo(currentSoundtrackResumePosition!!)
            player!!.start()
            if (mediaSession != null) {
                val playbackStateCompat: PlaybackStateCompat = PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SEEK_TO
                    )
                    .setState(PlaybackStateCompat.STATE_PLAYING, player!!.currentPosition.toLong(), 1f)
                    .build()
                mediaSession!!.setPlaybackState(playbackStateCompat)
                buildNotification(PlaybackStatus.PLAYING)
            }
        }
    }

    private fun playerSeekTo(position: Int) {
        if (player != null) {
            player!!.seekTo(position)
            if (mediaSession != null) {
                val playbackStateCompat: PlaybackStateCompat = PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SEEK_TO
                    )
                    .setState(PlaybackStateCompat.STATE_PLAYING, player!!.currentPosition.toLong(), 1f)
                    .build()
                mediaSession!!.setPlaybackState(playbackStateCompat)
                buildNotification(PlaybackStatus.PLAYING)
            }
        }
    }

    private fun playerSkipToNext() {
        val soundtrackMetadataList = soundtrackRepository.getSoundtrackMetadataList()
        currentSoundtrackMetadataIndex = if (currentSoundtrackMetadataIndex == soundtrackMetadataList.size - 1) {
            0
        } else {
            currentSoundtrackMetadataIndex!! + 1
        }
        currentSoundtrackMetadata = soundtrackRepository.getSoundtrackMetadataByIndex(
            currentSoundtrackMetadataIndex!!
        )

        stopPlayer()
        preparePlayer()
        if (mediaSession != null) {
            buildNotification(PlaybackStatus.PLAYING)
        }
    }

    private fun playerSkipToPrevious() {
        val soundtrackMetadataList = soundtrackRepository.getSoundtrackMetadataList()
        currentSoundtrackMetadataIndex =
            if (currentSoundtrackMetadataIndex == 0) {
                soundtrackMetadataList.size - 1
            } else {
                currentSoundtrackMetadataIndex!! - 1
            }
        currentSoundtrackMetadata = soundtrackRepository.getSoundtrackMetadataByIndex(
            currentSoundtrackMetadataIndex!!
        )

        stopPlayer()
        preparePlayer()
        if (mediaSession != null) {
            buildNotification(PlaybackStatus.PLAYING)
        }
    }

    private val binder = SoundtrackPlayerServiceBinder()

    override fun onCreate() {
        super.onCreate()
        registerReceiverSoundtrack()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playerSkipToNext()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        startPlayer()
        val playbackStateCompat: PlaybackStateCompat = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SEEK_TO
            )
            .setState(PlaybackStateCompat.STATE_PLAYING, mp!!.currentPosition.toLong(), 1f)
            .build()
        mediaSession!!.setPlaybackState(playbackStateCompat)
        updateMetadata()
        buildNotification(PlaybackStatus.PLAYING)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    inner class SoundtrackPlayerServiceBinder : Binder() {
        fun getService(): SoundtrackPlayerService = this@SoundtrackPlayerService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isPlaybackActionAndHandled = handleIncomingActions(intent)
        if (isPlaybackActionAndHandled) {
            return super.onStartCommand(intent, flags, startId)
        }

        currentSoundtrackMetadataIndex = soundtrackRepository.getSoundtrackIndex()
        if (currentSoundtrackMetadataIndex == null) {
            stopSelf()
        }
        currentSoundtrackMetadata = soundtrackRepository.getSoundtrackMetadataByIndex(currentSoundtrackMetadataIndex!!)
        if (currentSoundtrackMetadata == null) {
            stopSelf()
        }

        initializePlayer()
        if (mediaSession == null) {
            initializeMediaSession()
            buildNotification(PlaybackStatus.PLAYING)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            stopPlayer()
            player!!.release()
        }
        if (mediaSession != null) {
            mediaSession!!.release()
        }
        unregisterReceiver(playNewSoundtrack)
        unregisterReceiver(pauseSoundtrack)
        unregisterReceiver(resumeSoundtrack)
        unregisterReceiver(playNextSoundtrack)
        unregisterReceiver(playPreviousSoundtrack)
    }

    private val playNewSoundtrack: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            currentSoundtrackMetadataIndex = soundtrackRepository.getSoundtrackIndex()
            if (currentSoundtrackMetadataIndex == null) {
                stopSelf()
            }
            currentSoundtrackMetadata = soundtrackRepository.getSoundtrackMetadataByIndex(currentSoundtrackMetadataIndex!!)
            if (currentSoundtrackMetadata == null) {
                stopSelf()
            }

            if (player != null) {
                stopPlayer()
                preparePlayer()
            }
        }
    }

    private val pauseSoundtrack: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (player != null) {
                pausePlayer()
            }
        }
    }

    private val resumeSoundtrack: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (player != null) {
                resumePlayer()
            }
        }
    }

    private val playPreviousSoundtrack: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (player != null) {
                playerSkipToPrevious()
            }
        }
    }

    private val playNextSoundtrack: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (player != null) {
                playerSkipToNext()
            }
        }
    }

    private fun registerReceiverSoundtrack() {
        val filterPlayNewSoundtrack = IntentFilter(WarcraftInfoConstant.BROADCAST_PLAY_NEW_SOUNDTRACK)
        val filterPreviousSoundtrack = IntentFilter(WarcraftInfoConstant.BROADCAST_PREVIOUS_SOUNDTRACK)
        val filterPauseSoundtrack = IntentFilter(WarcraftInfoConstant.BROADCAST_PAUSE_SOUNDTRACK)
        val filterResumeSoundtrack = IntentFilter(WarcraftInfoConstant.BROADCAST_RESUME_SOUNDTRACK)
        val filterNextSoundtrack = IntentFilter(WarcraftInfoConstant.BROADCAST_NEXT_SOUNDTRACK)
        registerReceiver(playNewSoundtrack, filterPlayNewSoundtrack)
        registerReceiver(playPreviousSoundtrack, filterPreviousSoundtrack)
        registerReceiver(pauseSoundtrack, filterPauseSoundtrack)
        registerReceiver(resumeSoundtrack, filterResumeSoundtrack)
        registerReceiver(playNextSoundtrack, filterNextSoundtrack)
    }

    private var mediaSession: MediaSessionCompat? = null
    private var transportControls: MediaControllerCompat.TransportControls? = null

    private fun initializeMediaSession() {
        if (mediaSession != null) {
            return
        }
        mediaSession = MediaSessionCompat(applicationContext, "SoundtrackPlayerServiceMediaSession")
        transportControls = mediaSession!!.controller.transportControls
        mediaSession!!.isActive = true
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        updateMetadata()

        mediaSession!!.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
                resumePlayer()
            }

            override fun onPause() {
                super.onPause()
                pausePlayer()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                playerSkipToNext()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                playerSkipToPrevious()
            }

            override fun onStop() {
                super.onStop()
                removeNotification()
                stopSelf()
            }

            override fun onSeekTo(position: Long) {
                super.onSeekTo(position)
                playerSeekTo(position.toInt())
            }


        })
    }

    private fun updateMetadata() {
        mediaSession!!.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSoundtrackMetadata!!.artists)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentSoundtrackMetadata!!.album)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSoundtrackMetadata!!.title)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, player!!.duration.toLong())
                .build()
        )
        Picasso.get().load(WarcraftInfoConstant.TARANZHI_API_URI + currentSoundtrackMetadata!!.cover)
            .into(object: com.squareup.picasso.Target {
                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    mediaSession!!.setMetadata(
                        MediaMetadataCompat.Builder()
                            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSoundtrackMetadata!!.artists)
                            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentSoundtrackMetadata!!.album)
                            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSoundtrackMetadata!!.title)
                            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, player!!.duration.toLong())
                            .build()
                    )
                }
            })
    }

    private fun playbackActionPendingIntentGenerator(actionNumber: Int): PendingIntent? {
        val playbackAction = Intent(this, SoundtrackPlayerService::class.java)
        when (actionNumber) {
            0 -> {
                playbackAction.action = WarcraftInfoConstant.ACTION_PLAY
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            1 -> {
                playbackAction.action = WarcraftInfoConstant.ACTION_PAUSE
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            2 -> {
                playbackAction.action = WarcraftInfoConstant.ACTION_NEXT
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            3 -> {
                playbackAction.action = WarcraftInfoConstant.ACTION_PREVIOUS
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
        }
        return null
    }

    private fun handleIncomingActions(playbackAction: Intent?): Boolean {
        if (playbackAction == null || playbackAction.action == null) return false
        val actionString = playbackAction.action
        if (actionString.equals(WarcraftInfoConstant.ACTION_PLAY, ignoreCase = true)) {
            transportControls!!.play()
            return true
        } else if (actionString.equals(WarcraftInfoConstant.ACTION_PAUSE, ignoreCase = true)) {
            transportControls!!.pause()
            return true
        } else if (actionString.equals(WarcraftInfoConstant.ACTION_NEXT, ignoreCase = true)) {
            transportControls!!.skipToNext()
            return true
        } else if (actionString.equals(WarcraftInfoConstant.ACTION_PREVIOUS, ignoreCase = true)) {
            transportControls!!.skipToPrevious()
            return true
        }
        return false
    }

    private fun buildNotification(playbackStatus: PlaybackStatus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.name_notification_channel_soundtrack_service)
            val descriptionText = getString(R.string.description_notification_soundtrack_service)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                WarcraftInfoConstant.SOUNDTRACK_NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        var middleNotificationActionIcon = android.R.drawable.ic_media_pause
        var middleNotificationActionPendingIntent: PendingIntent? = null

        if (playbackStatus === PlaybackStatus.PLAYING) {
            middleNotificationActionIcon = android.R.drawable.ic_media_pause
            middleNotificationActionPendingIntent = playbackActionPendingIntentGenerator(1)
        } else if (playbackStatus === PlaybackStatus.PAUSED) {
            middleNotificationActionIcon = android.R.drawable.ic_media_play
            middleNotificationActionPendingIntent = playbackActionPendingIntentGenerator(0)
        }

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            WarcraftInfoConstant.SOUNDTRACK_NOTIFICATION_CHANNEL_ID
        ).apply {
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession!!.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            setSmallIcon(android.R.drawable.stat_sys_headset)
            setContentText(currentSoundtrackMetadata!!.artists)
            setContentTitle(currentSoundtrackMetadata!!.album)
            setContentInfo(currentSoundtrackMetadata!!.title)
            addAction(android.R.drawable.ic_media_previous, "Previous", playbackActionPendingIntentGenerator(3))
            addAction(middleNotificationActionIcon, "Pause", middleNotificationActionPendingIntent)
            addAction(android.R.drawable.ic_media_next, "Next", playbackActionPendingIntentGenerator(2))
        }

        NotificationManagerCompat
            .from(this)
            .notify(WarcraftInfoConstant.SOUNDTRACK_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun removeNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(WarcraftInfoConstant.SOUNDTRACK_NOTIFICATION_ID)
    }
}