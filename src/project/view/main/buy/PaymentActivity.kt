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

package com.example.project.view.main.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.view.main.MainActivity

class PaymentActivity : AppCompatActivity() {
    private var currentStep = 1
    private lateinit var btnBack: Button
    private lateinit var step1Text: TextView
    private lateinit var step2Text: TextView
    private lateinit var step3Text: TextView
    private lateinit var step1 : LinearLayout
    private lateinit var step2 : LinearLayout
    private lateinit var step3 : LinearLayout
    private var oCustomStatusBarIcons = CustomStatusBarIcons()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents(){
        oCustomStatusBarIcons.setIconsDark(this)
        setContentView(R.layout.activity_payment)
        btnBack = findViewById(R.id.btn_backPayment)
        step1 = findViewById(R.id.step1)
        step2 = findViewById(R.id.step2)
        step3 = findViewById(R.id.step3)
        step1Text = findViewById(R.id.step1Text)
        step2Text = findViewById(R.id.step2Text)
        step3Text = findViewById(R.id.step3Text)
        replaceFragment(RegistrationFragment())
        goBack()
    }

    private fun goBack(){
        btnBack.setOnClickListener {
            SharedApp.ticketList.clear()
            SharedApp.seatSelectedList.clear()
            SharedApp.finalCallApiList.clear()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun updateProgress(step: Int) {
        val layoutParams = step1.layoutParams
        layoutParams.width = 120
        layoutParams.height = 90
        when (step) {
            1 -> {
                step1.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step1.layoutParams = layoutParams
                step1Text.visibility = View.GONE
            }
            2 -> {
                step1.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step2.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step1.layoutParams = layoutParams
                step2.layoutParams = layoutParams
                step1Text.visibility = View.GONE
                step2Text.visibility = View.GONE
            }
            3 -> {
                step1.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step2.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step3.background = ContextCompat.getDrawable(this, R.drawable.checkgreen)
                step1.layoutParams = layoutParams
                step2.layoutParams = layoutParams
                step3.layoutParams = layoutParams
                step1Text.visibility = View.GONE
                step2Text.visibility = View.GONE
                step3Text.visibility = View.GONE
            }
        }
        currentStep = step
    }
}