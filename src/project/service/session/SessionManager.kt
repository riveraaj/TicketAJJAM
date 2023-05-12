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

import android.content.Context

class SessionManager(context: Context) {
    private val pref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val PROVIDER_TYPE = "provider_type"
        const val KEY_USER_PHOTO = "user_photo"
    }

    fun saveUser(userId: String, userName: String, userEmail: String, userPhoto: String, providerType: String) {
        pref.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(PROVIDER_TYPE, providerType)
            putString(KEY_USER_PHOTO, userPhoto)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): String {
        return pref.getString(KEY_USER_ID, "") ?: ""
    }

    fun getUserName(): String {
        return pref.getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserEmail(): String {
        return pref.getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getProviderType(): String{
        return pref.getString(PROVIDER_TYPE, "") ?: ""
    }

    fun getUserPhoto(): String{
        return pref.getString(KEY_USER_PHOTO, "") ?: ""
    }

    fun clearSession() {
        pref.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PHOTO)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }
}