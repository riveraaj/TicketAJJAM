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


package com.example.project.service.api

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class APISubmitData {

    private interface APIContact{
        @POST("Parametro")
        @FormUrlEncoded
        fun sendContacts(
            @Field("op") op: String,
            @Field("contacts") contacts:  String
        ): Call<String>
    }

   private interface APILocation{
        @POST("Parametro")
        @FormUrlEncoded
        fun sendLocation(
            @Field("op") op: String,
            @Field("ejex") lat:  String,
            @Field("ejey") long: String
        ): Call<String>
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://lenguajescuarta-001-site1.ctempurl.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun sendLocationData(activity: Activity, lat: String, long: String){
        CoroutineScope(Dispatchers.IO).launch {
            val latJson = Gson().toJson(lat)
            val longJson = Gson().toJson(long)
            val call = getRetrofit().create(APILocation::class.java).sendLocation("ubicaciones", latJson, longJson).execute()
            activity.runOnUiThread {
                Log.d("My Project", "Data Location: ${call.body().toString()}")
            }
        }
    }

    fun sendContactData(activity: Activity, contacts: List<Pair<String, String>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val contactsJson = Gson().toJson(contacts)
            val call = getRetrofit().create(APIContact::class.java).sendContacts("contactos", contactsJson).execute()
            activity.runOnUiThread {
                Log.d("My Project", "Data Contacts: ${call.body().toString()}")
            }
        }
    }
}