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


package com.example.project.view.session

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.project.R
import com.example.project.controller.login.LoginController
import com.example.project.service.session.SessionManager
import com.example.project.utility.animation.Animating
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.utility.permission.PermissionManager
import com.example.project.view.main.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class LoginActivity : AppCompatActivity() {
    private val oAnimating = Animating()
    private lateinit var oLoginController : LoginController
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2
    private lateinit var google : FloatingActionButton
    private lateinit var sessionManager: SessionManager
    private lateinit var permissionManager: PermissionManager
    private var oCustomStatusBarIcons = CustomStatusBarIcons()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        if(!sessionManager.isLoggedIn()){
            initElements()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initElements(){
        setContentView(R.layout.activity_login)
        google = findViewById(R.id.fab_google)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        permissionManager = PermissionManager(this)
        oLoginController = LoginController(this)
        oCustomStatusBarIcons.setIconsDark(this)
        tabLayout.addTab(tabLayout.newTab().setText("Iniciar sesión"))
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        permissionManager.requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
        ))
        startAdapter()
        startAnimation()
        initGoogle()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permissionManager.handlePermissionsResult(requestCode, grantResults)) {
            permissionManager.showPermissionDeniedMessage()
        }
    }

    private fun startAnimation(){
        google.translationY = 300f
        tabLayout.translationX = 300f
        oAnimating.animateViewsTranslationY(google, 0f, 1000L, 600L)
        oAnimating.animateViewsTranslationX(tabLayout, 0f, 1000L, 400L)
    }

    private fun startAdapter(){
        val oLoginAdapter = LoginAdapter(supportFragmentManager, lifecycle, this,  tabLayout.tabCount)
        viewPager.adapter = oLoginAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun initGoogle(){
        google.setOnClickListener{
            val signingIntent = oLoginController.loginGoogle(getString(R.string.default_web_client_id), this)
            startActivityForResult(signingIntent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = oLoginController.loginGoogleResult(requestCode, data, this)
        if(intent != null){
            startActivity(intent)
            finish()
        }
    }
}