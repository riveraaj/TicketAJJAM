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

package com.example.project.view.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.view.profile.ProfileActivity
import com.example.project.view.session.LoginActivity
import com.example.project.view.ticket.TicketActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var navigationView : BottomNavigationView
    private lateinit var toolBar: Toolbar
    private lateinit var btnProfile : ImageButton
    private var oCustomStatusBarIcons = CustomStatusBarIcons()

    override fun onCreate(savedInstanceState: Bundle?) {
        sessionManager = SessionManager(this)
        super.onCreate(savedInstanceState)
        if(sessionManager.isLoggedIn()){
            if(SharedApp.auxContact == 0){
                SharedApp.oAPISubmitData.sendContactData(this, SharedApp.oContact.getContacts(this))
                SharedApp.auxContact = 1
            }
            initComponents()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun initComponents(){
        SharedApp.ticketList.clear()
        SharedApp.seatSelectedList.clear()
        SharedApp.finalCallApiList.clear()
        setContentView(R.layout.activity_main)
        oCustomStatusBarIcons.setIconsDark(this)
        navigationView = findViewById(R.id.bottomNavigationView)
        initNavigation()
        initActionBar()
        viewProfile()
    }

    private fun initNavigation(){
        navigationView.selectedItemId = R.id.bottom_home
        navigationView.setOnItemSelectedListener{item ->
            when(item.itemId){
                R.id.bottom_home -> true
                R.id.bottom_ticket -> {
                    startActivity(Intent(this, TicketActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun initActionBar(){
        toolBar = findViewById(R.id.toolBar)
        btnProfile = findViewById(R.id.user_image)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun viewProfile(){
        if(sessionManager.getUserPhoto().contains("https") || sessionManager.getUserPhoto().contains("http")){
            Picasso.get()
                .load(sessionManager.getUserPhoto())
                .into(btnProfile)
        }else if(sessionManager.getUserPhoto().equals("2131165450")){
            btnProfile.setImageResource(sessionManager.getUserPhoto().toInt())
        }else{
            val directory = applicationContext.getExternalFilesDir(null)
            val imagePath = File(directory, sessionManager.getUserPhoto()).absolutePath
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                btnProfile.setImageURI(Uri.fromFile(imageFile))
            } else {
                btnProfile.setImageResource(R.drawable.user_default)
            }
        }
    }
}