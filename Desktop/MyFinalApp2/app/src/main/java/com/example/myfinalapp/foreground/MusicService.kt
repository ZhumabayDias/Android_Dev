package com.example.myfinalapp

import android.app.*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.io.File
import java.io.FileOutputStream

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val CHANNEL_ID = "MusicServiceChannel"

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

        val file = copyMusicFileFromAssets("music.mp3")
        if (file != null) {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
        }

        startForeground(1, createNotification())
        return START_STICKY
    }

    private fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun stopMusic() {
        mediaPlayer.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val playIntent = Intent(this, MusicService::class.java).apply { action = "START" }
        val pauseIntent = Intent(this, MusicService::class.java).apply { action = "PAUSE" }
        val stopIntent = Intent(this, MusicService::class.java).apply { action = "STOP" }

        val playPending = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pausePending = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val stopPending = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationChannel = NotificationChannel(
            CHANNEL_ID, "Music Service", NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(notificationChannel)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Now Playing: music.mp3")
            .setSmallIcon(R.drawable.ic_music_note)
            .addAction(R.drawable.ic_play, "Play", playPending)
            .addAction(R.drawable.ic_pause, "Pause", pausePending)
            .addAction(R.drawable.ic_stop, "Stop", stopPending)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun copyMusicFileFromAssets(fileName: String): File? {
        val file = File(cacheDir, fileName)
        if (!file.exists()) {
            try {
                assets.open(fileName).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
        return file
    }
}