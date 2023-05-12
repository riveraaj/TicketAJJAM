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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.project.R
import com.example.project.controller.login.SignupController
import com.example.project.utility.animation.Animating

class SignupTabFragment : Fragment() {
    private val oAnimating = Animating()
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnRegister: Button
    private lateinit var txtWarning: TextView
    private lateinit var passwordConfirm: EditText
    private val oSignupController = SignupController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false) as ViewGroup
        email = view.findViewById(R.id.emailRegister)
        password = view.findViewById(R.id.passwordRegister)
        passwordConfirm = view.findViewById(R.id.passwordConfirm)
        btnRegister = view.findViewById(R.id.btnRegister)
        txtWarning = view.findViewById(R.id.txtWarningRegister)
        txtWarning.visibility = TextView.INVISIBLE
        startAnimation()
        register()
        return view
    }

    private fun startAnimation(){
        email.translationX = 800f
        password.translationX = 800f
        passwordConfirm.translationX = 800f
        btnRegister.translationX = 800f
        oAnimating.animateViewsTranslationX(email, 0f, 1000L, 200L)
        oAnimating.animateViewsTranslationX(password, 0f, 1000L, 400L)
        oAnimating.animateViewsTranslationX(passwordConfirm, 0f, 1000L, 600L)
        oAnimating.animateViewsTranslationX(btnRegister, 0f, 1000L, 800L)
    }

    private fun register(){
        btnRegister.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            oSignupController.register(email, password, passwordConfirm, txtWarning, requireContext(), viewPager)
        }
    }
}