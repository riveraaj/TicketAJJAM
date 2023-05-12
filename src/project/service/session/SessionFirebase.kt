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


package com.example.project.service.session

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SessionFirebase {

    fun loginWithGoogle(idToken: String): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(idToken)
            .requestEmail()
            .build()
    }

    fun loginWithGoogleResponse(requestCode: Int, data: Intent?): MutableMap<String, Any>{
        val result: MutableMap<String, Any> = mutableMapOf()
        val GOOGLE_SING_IN = 100
        var isSuccessful = false

        if(requestCode == GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            isSuccessful = it.isSuccessful
                        }
                }
                return if(!isSuccessful){
                    result["state"] = true
                    result["message"] = "Inicio de sesión válido"
                    result["email"] = account.email ?: ""
                    result["name"] = account.displayName ?: ""
                    result["photo"] = account.photoUrl.toString()
                    result
                }else{
                    result["state"] = false
                    result["message"] = "Falló el inicio de sesión con Google"
                    result
                }
            }catch (e: ApiException){
                result["state"] = false
                result["message"] = "Falló el inicio de sesión con Google"
                return result
            }
        }
        return result
    }

    fun registerFireBase(email: String, password: String): MutableMap<String, Any>{
        val result: MutableMap<String, Any> = mutableMapOf()
        var isSuccessful = false
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isSuccessful =  (it.isSuccessful)
            }
        return if(!isSuccessful){
            result["state"] = true
            result["message"] = "¡Registro exitoso!"
            result
        }else{
            result["state"] = false
            result["message"] = "Lo siento, no fue posible registrar la cuenta"
            result
        }
    }

    fun loginFireBase(email: String, password: String): MutableMap<String, Any>{
        val result: MutableMap<String, Any> = mutableMapOf()
        var isSuccessful = false
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isSuccessful = (it.isSuccessful)
            }
        return if(!isSuccessful){
            result["state"] = true
            result["message"] = "Inicio de sesión válido"
            result
        }else{
            result["state"] = false
            result["message"] = "*El correo o contraseña no son válidos"
            result
        }
    }
}