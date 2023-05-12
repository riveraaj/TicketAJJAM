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

package com.example.project.controller.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.example.project.view.session.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class ProfileController(context: Context) {

    private  var sessionManager = SessionManager(context)

    fun logout(context: Context): Intent {
        sessionManager.clearSession()
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(context, "Cerrando sesión", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    fun viewProfile(btnUpdateImage: ImageButton, context: Context){
        if(sessionManager.getUserPhoto().contains("https") || sessionManager.getUserPhoto().contains("http")){
            Picasso.get()
                .load(sessionManager.getUserPhoto())
                .into(btnUpdateImage)
        }else if(sessionManager.getUserPhoto() == "2131165450"){
            btnUpdateImage.setImageResource(sessionManager.getUserPhoto().toInt())
        }else{
            val directory = context.getExternalFilesDir(null)
            val imagePath = File(directory, sessionManager.getUserPhoto()).absolutePath
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                btnUpdateImage.setImageURI(Uri.fromFile(imageFile))
            } else {
                btnUpdateImage.setImageResource(R.drawable.user_default)
            }
        }
    }

    fun showBottomDialog(context: Context, txtName: TextView){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_layout)
        initElementsDialog(dialog, txtName)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun initElementsDialog(dialog : Dialog, txtName: TextView){
        val txtNameUpdating = dialog.findViewById(R.id.nameUpdating) as TextView
        val txtLastName = dialog.findViewById(R.id.lastNameUpdating) as TextView
        val btnCloseDialog = dialog.findViewById(R.id.btnCloseDialogUpdate) as ImageButton
        val btnSaveName = dialog.findViewById(R.id.btn_save_name_updating) as Button
        btnCloseDialog.setOnClickListener{
            dialog.dismiss()
        }
        btnSaveName.setOnClickListener{
            val id = sessionManager.getUserId()
            SharedApp.oUserRepositoryImp.update(id, mapOf("name" to (txtNameUpdating.text.toString() + " " + txtLastName.text.toString())))
            sessionManager.saveUser(
                sessionManager.getUserId(),
                (txtNameUpdating.text.toString() + " " + txtLastName.text.toString()),
                sessionManager.getUserEmail(),
                sessionManager.getUserPhoto(),
                sessionManager.getProviderType()
            )
            txtName.text = (txtNameUpdating.text.toString() + " " + txtLastName.text.toString())
            dialog.dismiss()
        }
    }

    fun generateRandomFileName(extension: String): String {
        return "${UUID.randomUUID()}.$extension"
    }

    fun writeOnTexts(txtName: TextView, txtEmail: TextView){
        if(sessionManager.getUserName().isEmpty()){
            txtName.text = "Edite su nombre"
        }else{
            txtName.text = sessionManager.getUserName()
        }
        txtEmail.text = sessionManager.getUserEmail()
    }
}