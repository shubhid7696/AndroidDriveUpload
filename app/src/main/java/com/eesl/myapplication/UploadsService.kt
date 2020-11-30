package com.eesl.myapplication

import android.R
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Tasks.call
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class UploadsService : Service() {

    val CHANNEL_ID = "AppNotifChannel#er23"
    lateinit var driveService : Drive

    private val executors = Executors.newSingleThreadExecutor()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val filePathUri = intent!!.getStringExtra("image_path")
        var filePath = ""

        createNotificationChannel()

//        Log.e("###################","${MainApplication.googleCreds.selectedAccount.name.toString()} \n " +
//                MainApplication.googleCreds.token.toString()
//        )

        driveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            MainApplication.googleCreds
        ).setApplicationName("Drive application")
            .build()



        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Uploading...")
                .setContentText("Uploading image to Google drive ")
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)
        //do heavy work on a background thread
        //stopSelf();
        filePath = RealPathUtil.getRealPath(this,filePathUri!!.toUri())!!

        if(filePath.isNullOrBlank()){
            Toast.makeText(this,"No File found at this path",Toast.LENGTH_SHORT).show()
        } else {

            GlobalScope.launch {
                    val fileMetaData = File()
                    fileMetaData.setName("Image.jpeg")

                    val filePath = java.io.File(filePath)
                    val mediaContent = FileContent("image/jpeg", filePath)

                    var uploadFIle: File? = null
                    try {
                        uploadFIle =
                            driveService.files().create(fileMetaData, mediaContent).execute()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(
                            "#### UploadsService",
                            "ERROR  ${e.message}"
                        )
                    }

                    if (uploadFIle == null) {
                        Log.e(
                            "#### UploadsService",
                            "ERROR  "
                        )
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@UploadsService,
                                "Error in file ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@UploadsService,
                                "File uploaded success ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                withContext(Dispatchers.Main) {
                    this@UploadsService.stopSelf(1)
                }
            }
        }
        return START_NOT_STICKY

    //return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Uploading Status",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


  /*  fun uploadImageFile(path : String) : Task<String> {
        return call(executors, {



        })
    }*/

}