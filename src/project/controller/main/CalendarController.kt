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
 *
 * This source code file was created by [Jonathan Rivera Vasquez] and contains original code
 * created by me and my colleague
 *
 * Contributions by:
 * - [Ayram Tolentino]: [API call function]
 *
 */


package com.example.project.controller.main

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.model.SharedApp
import com.example.project.model.Theater
import com.example.project.utility.layout.CustomSpacing
import com.example.project.view.main.CalendarAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class CalendarController {

    fun apiCall(mes: String, anno: String, activity: Activity, oRecyclerView: RecyclerView, oAdapter: CalendarAdapter, context: Context){
       CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).registrationPost("listarparamovil", mes, anno).execute()
            activity.runOnUiThread {
                SharedApp.eventList = call.body()!!
                setUpRecyclerView(oRecyclerView, oAdapter, context)
            }
       }
    }

    private interface APIService {
        @POST("Calendario")
        @FormUrlEncoded
        fun registrationPost(
            @Field("op") op: String,
            @Field("mes") mes: String,
            @Field("anno") anno: String
        ): Call<MutableList<Theater.EventDate>>
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://lenguajescuarta-001-site1.ctempurl.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setUpRecyclerView(oRecyclerView: RecyclerView, oAdapter: CalendarAdapter, context: Context){
        oRecyclerView.addItemDecoration(CustomSpacing(200))
        oRecyclerView.setHasFixedSize(true)
        oRecyclerView.layoutManager = LinearLayoutManager(context)
        oAdapter.RecyclerAdapter(SharedApp.eventList, context)
        oRecyclerView.adapter = oAdapter
    }
}