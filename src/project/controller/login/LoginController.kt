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
import android.widget.Toast
import com.example.project.model.SharedApp
import com.example.project.model.User
import com.example.project.service.session.SessionManager
import com.example.project.view.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class LoginController(context: Context) {

    private  var sessionManager = SessionManager(context)

    fun loginGoogle(id: String, context: Context): Intent {
        val gso = SharedApp.oSessionFirebase.loginWithGoogle(id)
        val oGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        oGoogleSignInClient.signOut()
        return oGoogleSignInClient.signInIntent
    }

    fun loginGoogleResult(requestCode: Int, data: Intent?, context: Context): Intent? {
        val result = SharedApp.oSessionFirebase.loginWithGoogleResponse(requestCode, data)
        if(result["state"] == true){
            return saveLoginGoogle(result, context)
        }else{
            Toast.makeText(context,  result["message"].toString(), Toast.LENGTH_SHORT).show()
        }
        return null
    }

    private fun saveLoginGoogle(result:  MutableMap<String, Any>, context: Context): Intent {
        val id = getUserIdByEmailAsync(result["email"].toString()).toString()
        SharedApp.oUserRepositoryImp.isEmailAlreadyRegistered(result["email"].toString())
            .addOnSuccessListener {isRegister ->
                if(!isRegister){
                    val newUser = User(result["name"].toString(), result["email"].toString(), result["photo"].toString())
                    SharedApp.oUserRepositoryImp.insert(newUser)
                }else{
                    SharedApp.oUserRepositoryImp.update(id, mapOf(
                        "name" to result["name"].toString(),
                        "photo" to result["photo"].toString()
                    ))
                }
            }
        sessionManager.saveUser(id, result["name"].toString(), result["email"].toString(), result["photo"].toString(), "GOOGLE")
        Toast.makeText(context, result["message"].toString(), Toast.LENGTH_SHORT).show()
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
}