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


package com.example.project.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.project.R
import com.example.project.utility.animation.Animating
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.view.session.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var appName: TextView
    private lateinit var splash: ImageView
    private var oCustomStatusBarIcons = CustomStatusBarIcons()
    private lateinit var oLottieAnimationView: LottieAnimationView
    private var oAnimating = Animating()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initElements()
        startAnimation()
    }

    private fun initElements(){
        setContentView(R.layout.activity_splash)
        logo = findViewById(R.id.logoSplash)
        appName = findViewById(R.id.nameSplash)
        splash = findViewById(R.id.backgroundSplash)
        oLottieAnimationView = findViewById(R.id.animation_view)
    }

    private fun startAnimation(){
        splash.animate().translationY(-4000f).setDuration(1000L).setStartDelay(2500L).withEndAction {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
        oAnimating.animateViewsTranslationY(logo, 4000f, 1000L, 2500L)
        oAnimating.animateViewsTranslationY(appName, 4000f, 1000L, 2500L)
        oAnimating.animateViewsTranslationY(oLottieAnimationView, 4000f, 1000L, 2500L)
    }
}