package com.eesl.myapplication

import android.os.Bundle
import com.eesl.myapplication.base.PermissionsActivity
import com.eesl.myapplication.base.startCameraActivityWithPermissionCheck
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PermissionsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchCamera.setOnClickListener {
            startCameraActivityWithPermissionCheck()
        }

    }

}