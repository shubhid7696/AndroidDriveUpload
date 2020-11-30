package com.eesl.myapplication.base
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.eesl.myapplication.CameraActivity
import permissions.dispatcher.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@RuntimePermissions
open class PermissionsActivity : AppCompatActivity(){
    val CAMERA_INTENT = 2332
    val GALLERY_INTENT = 2333
    val PROFILE_PICTURE = 2334
    val ID_PROOOF = 2335
    var picType = 0

    var photoFile : File? = null
    var idProofFile : File? = null

    lateinit var permissionReceived: AppEventListener

    fun getPic(){
        //showCameraStorageWithPermissionCheck()
    }

    fun getGalleryPic() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_INTENT)
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        //Toast.makeText(this, "Show Camera", Toast.LENGTH_SHORT).show()
        /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_INTENT)
            }
        }*/
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_INTENT)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun startCameraActivity() {

        startActivity(Intent(this,CameraActivity::class.java))

        /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                *//*photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_INTENT)
                }*//*
            }
        }*/

    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        Toast.makeText(this, "Show Rationale for Camera", Toast.LENGTH_SHORT).show()
        //showRationaleDialog(com.safebag_user.R.string.camera_req_btn, request)
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForStorage(request: PermissionRequest) {
        Toast.makeText(this, "Show Rationale for Storage", Toast.LENGTH_SHORT).show()
        //showRationaleDialog(com.safebag_user.R.string.storage_req_btn, request)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        Toast.makeText(this, "Permission camera denied", Toast.LENGTH_SHORT).show()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageDenied() {
        Toast.makeText(this, "Permission camera denied", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        Toast.makeText(this, "Permission camera never ask again", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageNeverAskAgain() {
        Toast.makeText(this, "Permission Storage never ask again", Toast.LENGTH_SHORT).show()
    }

    fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setPositiveButton("button_allow") { _, _ -> request.proceed() }
            .setNegativeButton("button_deny") { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_INTENT) {
                //CropImage.activity(Uri.fromFile(photoFile)).start(this)

            } else if(requestCode == GALLERY_INTENT){
                val contentUri : Uri = data?.data!!
                //val timeStamp = System.currentTimeMillis().toString()
                //val fileName = timeStamp+"."+getFileExt(contentUri)
               // CropImage.activity(contentUri).start(this)
            }
          /*  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {            //  GETTING IMAGE AFTER ITS CROPPED
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.getUri()
                    photoFile = File(resultUri.getPath()!!)
                    if(photoFile != null && permissionRecieved != null){
                        permissionRecieved.eventReceived(CAMERA_INTENT, null)
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.getError()
                }
            }*/
        }
    }


    lateinit var currentPhotoPath: String

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}
