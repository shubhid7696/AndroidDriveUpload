package com.eesl.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_image.*
import java.util.*


class ImageActivity : AppCompatActivity() {

    lateinit var googleSignIn : GoogleSignInClient
    lateinit var signInOptions: GoogleSignInOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        loadingNormalImage(clickedImage, Constants.path)

        uploadImage.setOnClickListener {
            requestSignIn()
        }

    }

    private fun requestSignIn() {
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
//            .requestId()
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()

        googleSignIn = GoogleSignIn.getClient(this, signInOptions)

        startActivityForResult(googleSignIn.signInIntent, 4044)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            4044 -> {
                if (resultCode == RESULT_OK) {
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                            .addOnSuccessListener {
                                MainApplication.googleCreds = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE))
                                MainApplication.googleCreds.selectedAccount = it.account

                                startUploadService()

                            }
                        .addOnFailureListener {
                            it.printStackTrace()
                            Toast.makeText(this,"SIGN IN FAILED",Toast.LENGTH_SHORT).show()
                        }
                            .addOnCanceledListener {
                                Toast.makeText(this, "SIGN IN CANCELED", Toast.LENGTH_SHORT).show()
                            }
                }
            }
            else -> {
                Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startUploadService() {
        val serviceIntent = Intent(this, UploadsService::class.java)
        serviceIntent.putExtra("image_path", Constants.path)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

}