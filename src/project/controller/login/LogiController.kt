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
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.example.project.view.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class LogiController(context: Context) {

    private  var sessionManager = SessionManager(context)

    fun firebaseLogin(email: EditText, password: EditText, warning: TextView, context: Context): Intent? {
        if(email.text.isEmpty()|| password.text.isEmpty()){
            clearInput(email, password)
            warning.visibility = TextView.VISIBLE
            warning.text = "*Debe rellenar los campos"
        }else{
            val result = SharedApp.oSessionFirebase.loginFireBase(email.text.toString(), password.text.toString())
            return if(result["state"] == true){
                saveLogin(result, context, email)
            }else{
                clearInput(email, password)
                warning.visibility = TextView.VISIBLE
                warning.text = result["message"].toString()
                null
            }
        }
        return null
    }

    private fun saveLogin(result: MutableMap<String, Any>, context: Context, email: EditText): Intent {
        val id = getUserIdByEmailAsync(email.text.toString()).toString()
        val userData = getUser(id)
        Toast.makeText(context, result["message"].toString(), Toast.LENGTH_SHORT).show()
        sessionManager.saveUser(id, userData["name"].toString(), userData["email"].toString(), userData["photo"].toString(), "GOOGLE")
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    private fun getUserIdByEmailAsync(email: String): String? = runBlocking {
        val deferred = async(Dispatchers.IO) {
            SharedApp.oUserRepositoryImp.getUserIdByEmail(email)
        }
        deferred.await()
    }

    private fun getUser(id: String): Map<String, Any> = runBlocking {
        val deferred = async(Dispatchers.IO) {
            SharedApp.oUserRepositoryImp.select(id)
        }
        deferred.await()
    }

    private fun clearInput(email: EditText, password: EditText){
        email.setText("")
        password.setText("")
    }
}