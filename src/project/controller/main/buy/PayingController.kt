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
 * created by me and my colleagues
 *
 * Contributions by:
 * - [Ayram Tolentino]: [API call functions]
 * - [Andres Morales]: [API call functions]
 * - [Jonathan Rivera]: [Code refactoring and API call functions]
 *
 */

package com.example.project.controller.main.buy

import android.app.Activity
import android.util.Log
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class PayingController {

    var aux = 0
    private var opInsert = "insertar"
    private var opModification = "modificar"

    fun sendTicket(sessionManager: SessionManager){
        SharedApp.oTicketRepositoryImp.insert(sessionManager.getUserId(), SharedApp.ticketList[0])
    }

    fun sendClient(op: String, idClient: String, nameClient: String, phoneClient: Int, firstLastName: String, secondLastName: String, email: String, activity: Activity, functionFinalStep: () -> Unit,){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIClient::class.java).clientPost(op, idClient, nameClient, phoneClient, firstLastName, secondLastName, email, 1).execute()
            activity.runOnUiThread{
                val result = call.body()!!
                if (result == 1){
                    Log.d("My Project", "Send Client: ${result.toString()}")
                    sendPurchase(idClient, SharedApp.finalCallApiList["idCalendar"].toString().toInt(), opInsert, activity, functionFinalStep)
                }
            }
        }
    }

    private fun sendPurchase(idClient: String, idCalendar: Int, op: String, activity: Activity, functionFinalStep: () -> Unit,) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIPurchase::class.java).purchasePost(idClient, idCalendar, op).execute()
            activity.runOnUiThread{
                val result = call.body()!!
                if (result != null){
                    Log.d("My Project", "Send Purchase ${result.toString()}")
                    SharedApp.seatSelectedList.keys.forEach{ x ->
                        sendSeat(opModification, x, SharedApp.finalCallApiList["idCompany"].toString().toInt(), idCalendar, result, functionFinalStep, activity)
                    }
                }
            }
        }
    }

    private fun sendSeat(op: String, idSeat: Int, idCompany: Int, idCalendar: Int, codePurchase: Int, functionFinalStep: () -> Unit, activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APISeat::class.java).seatPost(op, idSeat, idCompany, "N", "S", 0, codePurchase, idCalendar, 0, 0).execute()
            activity.runOnUiThread{
                val result = call.body()!!
                if (result == 1){
                    Log.d("My Project", "Send Seat: ${result.toString()}")
                    if(aux == 0){
                        var arraySeats = ""
                        SharedApp.seatSelectedList.keys.forEach{ x ->
                            arraySeats += "$x,"
                        }
                        sendPayment(opInsert, SharedApp.finalCallApiList["total"].toString().toInt(), idCalendar, SharedApp.finalCallApiList["card"].toString().toInt(), idCompany, arraySeats, functionFinalStep, activity)
                        aux = 1
                    }
                }
            }
        }
    }

    private fun sendPayment(op: String, total: Int, idCalendar: Int, card : Int, idCompany: Int, seats: String, functionFinalStep: () -> Unit, activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIPayment::class.java).paymentPost(op, total, "AMEX", "", "", "", idCalendar, card, idCompany, seats).execute()
            activity.runOnUiThread{
                val result = call.body()!!
                if (result == 1){
                    Log.d("My Project", "Send Payment: ${result.toString()}")
                    functionFinalStep()
                }
            }
        }
    }

    private interface APIClient {
        @POST("Cliente")
        @FormUrlEncoded
        fun clientPost(
            @Field("op") op: String,
            @Field("id") idClient: String,
            @Field("nombre") nameClient: String,
            @Field("cel") phoneClient: Int,
            @Field("pa") firstLastName: String,
            @Field("sa") secondLastName: String,
            @Field("email") email: String,
            @Field("publicidad") advertising: Int
        ): Call<Int>
    }

    private interface APIPurchase{
        @POST("Compra")
        @FormUrlEncoded
        fun purchasePost(
            @Field("clienteid") idClient: String,
            @Field("calendario") idCalendar: Int,
            @Field("op") op: String
        ): Call<Int>
    }

    private interface APISeat{
        @POST("AdmButacacoordenada")
        @FormUrlEncoded
        fun seatPost(
            @Field("op") op: String,
            @Field("idbutacacoordenada") idSeat: Int,
            @Field("idempresa") idCompany: Int,
            @Field("reservado") reserved: String,
            @Field("pagado") paid: String,
            @Field("codreserva") codeReservation: Int,
            @Field("codcompra") codePurchase: Int,
            @Field("calendario") idCalendar: Int,
            @Field("codpago") codPaid: Int,
            @Field("precio") price: Int
        ): Call<Int>
    }

    private interface APIPayment{
        @POST("Pago")
        @FormUrlEncoded
        fun paymentPost(
            @Field("op") op: String,
            @Field("montototal") total: Int,
            @Field("tipo") type: String,
            @Field("nreferencia") reference: String,
            @Field("autorizacion") authorization : String,
            @Field("transaccion") transaction : String,
            @Field("calendario") idCalendar: Int,
            @Field("targeta") card : Int,
            @Field("idempresa") idCompany: Int,
            @Field("asientos") seats: String
        ): Call<Int>
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://lenguajescuarta-001-site1.ctempurl.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}