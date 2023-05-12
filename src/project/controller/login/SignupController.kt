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

package com.example.project.controller.login

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.model.User

class SignupController {

    fun register(email: EditText, password : EditText, passwordConfirm: EditText, txtWarning: TextView, context: Context, viewPager: ViewPager2){
        if(email.text.isEmpty() || password.text.isEmpty() || passwordConfirm.text.isEmpty()){
            clearInput(email, password, passwordConfirm)
            txtWarning.visibility = View.VISIBLE
            txtWarning.text = "*Debe rellenar los campos"
        }else if (password.text.toString() != passwordConfirm.text.toString()){
            clearInput(email, password, passwordConfirm)
            txtWarning.visibility = View.VISIBLE
            txtWarning.text = "*Las contraseñas no coinciden"
        }else{
            saveRegister(email, password, passwordConfirm, txtWarning, context, viewPager)
        }
    }

    private fun saveRegister(email: EditText, password : EditText, passwordConfirm: EditText, txtWarning: TextView, context: Context, viewPager: ViewPager2){
        SharedApp.oUserRepositoryImp.isEmailAlreadyRegistered(email.text.toString())
            .addOnSuccessListener { isRegister ->
                if(!isRegister){
                    val result = SharedApp.oSessionFirebase.registerFireBase(email.text.toString(), password.text.toString())
                    if(result["state"] == true){
                        val newUser = User("", email.text.toString(), getImage())
                        SharedApp.oUserRepositoryImp.insert(newUser)
                        Toast.makeText(context, result["message"].toString(), Toast.LENGTH_SHORT).show()
                        clearInput(email, password, passwordConfirm)
                        txtWarning.visibility = TextView.INVISIBLE
                        viewPager.currentItem = 0
                    }else{
                        clearInput(email, password, passwordConfirm)
                        txtWarning.visibility = TextView.VISIBLE
                        txtWarning.text = result["message"].toString()
                    }
                }else{
                    clearInput(email, password, passwordConfirm)
                    txtWarning.visibility = TextView.VISIBLE
                    txtWarning.text = "*Esta cuenta ya está registrada"
                }
            }
    }

    private fun getImage(): String {
        return R.drawable.user_default.toString()
    }

    private fun clearInput(email: EditText, password : EditText, passwordConfirm: EditText){
        email.setText("")
        password.setText("")
        passwordConfirm.setText("")
    }
}