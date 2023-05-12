/*
 * Copyright [2023] [Jonathan Rivera Vasquez]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.project.view.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.project.R
import com.example.project.controller.profile.ProfileController
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.utility.permission.PermissionManager
import com.example.project.view.main.MainActivity
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {
    private var image_uri: Uri? = null
    private val IMAGE_CAPTURE_CODE = 1001
    private lateinit var txtName : TextView
    private lateinit var txtEmail : TextView
    private lateinit var btnBack : ImageButton
    private lateinit var btnUpdateName: Button
    private lateinit var btnUpdateImage: ImageButton
    private lateinit var btnLogout : ConstraintLayout
    private lateinit var sessionManager: SessionManager
    private lateinit var permissionManager: PermissionManager
    private var oCustomStatusBarIcons = CustomStatusBarIcons()
    private lateinit var oProfileController: ProfileController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents(){
        setContentView(R.layout.activity_profile)
        oCustomStatusBarIcons.setIconsDark(this)
        txtName = findViewById(R.id.txt_update_name)
        txtEmail = findViewById(R.id.txt_update_email)
        btnBack = findViewById(R.id.btn_back)
        btnUpdateImage = findViewById(R.id.btn_update_photo)
        btnUpdateName = findViewById(R.id.btn_updateing_name)
        btnLogout = findViewById(R.id.container_logout)
        oProfileController = ProfileController(this)
        permissionManager = PermissionManager(this)
        sessionManager = SessionManager(this)
        oProfileController.viewProfile(btnUpdateImage, this)
        oProfileController.writeOnTexts(txtName, txtEmail)
        updateName()
        updatePhoto()
        logout()
        goBack()
    }

    private fun logout(){
        btnLogout.setOnClickListener {
            val intent = oProfileController.logout(this)
            startActivity(intent)
            finish()
        }
    }

    private fun goBack(){
        btnBack.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updatePhoto(){
        btnUpdateImage.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }else{
                permissionManager.requestPermissions(arrayOf(Manifest.permission.CAMERA))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permissionManager.handlePermissionsResult(requestCode, grantResults)) {
            permissionManager.showPermissionDeniedMessage()
        }
    }

    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            val directory = applicationContext.getExternalFilesDir(null)
            val extension = contentResolver.getType(image_uri!!)?.split("/")?.last()
            val fileName = oProfileController.generateRandomFileName(extension ?: "jpg")
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            contentResolver.openInputStream(image_uri!!)?.copyTo(outputStream)
            outputStream.close()
            sessionManager.saveUser(sessionManager.getUserId(), sessionManager.getUserName(), sessionManager.getUserName(), image_uri.toString(), sessionManager.getProviderType())
            btnUpdateImage.setImageURI(image_uri)
            val id = sessionManager.getUserId()
            SharedApp.oUserRepositoryImp.update(id, mapOf("photo" to (image_uri.toString())))
        }
    }

    private fun updateName(){
        btnUpdateName.setOnClickListener{
            oProfileController.showBottomDialog(this, txtName)
        }
    }
}