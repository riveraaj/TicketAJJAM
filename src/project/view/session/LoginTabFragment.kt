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
import com.example.project.R
import com.example.project.controller.login.LogiController
import com.example.project.service.session.SessionManager
import com.example.project.utility.animation.Animating

class LoginTabFragment : Fragment() {
    private val oAnimating = Animating()
    private lateinit var email: EditText
    private lateinit var btnLogin: Button
    private lateinit var warning: TextView
    private lateinit var password: EditText
    private lateinit var sessionManager: SessionManager
    private lateinit var oLogiController: LogiController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login_tab, container, false)
        email = view.findViewById(R.id.emailLogin)
        password = view.findViewById(R.id.passwordLogin)
        warning = view.findViewById(R.id.txtWarning)
        btnLogin = view.findViewById(R.id.btnLogin)
        warning.visibility = TextView.INVISIBLE
        sessionManager = SessionManager(requireContext())
        oLogiController = LogiController(requireContext())
        startAnimation()
        firebaseLogin()
        return view
    }

    private fun startAnimation(){
        email.translationX = 800f
        password.translationX = 800f
        btnLogin.translationX = 800f
        oAnimating.animateViewsTranslationX(email, 0f, 1000L, 200L)
        oAnimating.animateViewsTranslationX(password, 0f, 1000L, 400L)
        oAnimating.animateViewsTranslationX(btnLogin, 0f, 1000L, 600L)
    }

    private fun firebaseLogin(){
        btnLogin.setOnClickListener {
            val intent = oLogiController.firebaseLogin(email, password, warning, requireContext())
            if(intent != null){
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}