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

package com.example.project.view.main.buy

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project.R
import com.example.project.controller.main.buy.PayingController
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class PayingFragment : Fragment() {
    private var nameBuyer = ""
    private var emailBuyer = ""
    private var phoneBuyer = ""
    private lateinit var btnNext : Button
    private lateinit var cardCCV: EditText
    private lateinit var warning: TextView
    private lateinit var txtTotal : TextView
    private lateinit var cardNumber: EditText
    private lateinit var txtSubtotal : TextView
    private lateinit var txtUnitPrice : TextView
    private lateinit var cardExpiration: EditText
    private lateinit var txtAmountSeat : TextView
    private lateinit var nameEventPaying: TextView
    private lateinit var seatEventPaying: TextView
    private lateinit var hourEventPaying: TextView
    private lateinit var dateEventPaying: TextView
    private lateinit var imageViewWaiting: ImageView
    private lateinit var imageEventPaying: ImageView
    private lateinit var sessionManager: SessionManager
    private lateinit var oPayingController: PayingController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_paying, container, false)
        initComponents(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponents(view: View){
        oPayingController = PayingController()
        btnNext = view.findViewById(R.id.nextStep2)
        txtTotal = view.findViewById(R.id.totalPaying)
        cardCCV = view.findViewById(R.id.cardCCVPayment)
        sessionManager = SessionManager(requireContext())
        txtSubtotal = view.findViewById(R.id.subtotalPaying)
        cardNumber = view.findViewById(R.id.cardNumberPayment)
        txtUnitPrice = view.findViewById(R.id.unitPricePaying)
        txtAmountSeat = view.findViewById(R.id.amountSeatPaying)
        nameEventPaying = view.findViewById(R.id.nameEventPaying)
        seatEventPaying = view.findViewById(R.id.seatEventPaying)
        hourEventPaying = view.findViewById(R.id.hourEventPaying)
        dateEventPaying = view.findViewById(R.id.dateEventPaying)
        warning = view.findViewById(R.id.txtWarningRegisterPaying)
        imageEventPaying = view.findViewById(R.id.imageEventPaying)
        imageViewWaiting = view.findViewById(R.id.imageViewWaiting)
        cardExpiration = view.findViewById(R.id.cardExpirationPayment)
        (activity as PaymentActivity).updateProgress(1)
        bind()
        goNext()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun bind(){
        warning.visibility = View.GONE
        imageViewWaiting.visibility = View.GONE
        Glide.with(this)
            .asGif()
            .load(R.drawable.waiting)
            .into(imageViewWaiting)
        Picasso.get().load(SharedApp.ticketList[0].url)
            .error(R.drawable.shrek)
            .into(imageEventPaying)
        val seating = SharedApp.ticketList[0].seating.dropLast(1).joinToString(", ")
        val lastSeat = SharedApp.ticketList[0].seating.lastOrNull()
        val date = LocalDate.parse(SharedApp.ticketList[0].dateEvent)
        val result = date.format(DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault()))
        emailBuyer = arguments?.getString("emailBuyer").toString()
        nameBuyer = arguments?.getString("nameBuyer").toString()
        phoneBuyer = arguments?.getString("phoneBuyer").toString()
        nameEventPaying.text = SharedApp.ticketList[0].nameEvent
        seatEventPaying.text = "${seating}${if (lastSeat != null) ", $lastSeat" else ""}"
        hourEventPaying.text = SharedApp.ticketList[0].timeEvent
        dateEventPaying.text = "${result.replaceFirstChar { it.uppercase(Locale.getDefault()) }}, "
        txtUnitPrice.text = "₡${SharedApp.ticketList[0].price}"
        txtAmountSeat.text = "${SharedApp.ticketList[0].seating.size}"
        txtSubtotal.text = "₡${SharedApp.ticketList[0].price.toInt() * SharedApp.ticketList[0].seating.size}"
        txtTotal.text = "₡${SharedApp.ticketList[0].price.toInt() * SharedApp.ticketList[0].seating.size}"
    }

    private fun goNext(){
        btnNext.setOnClickListener{
            if(cardNumber.text.isEmpty() || cardExpiration.text.isEmpty() || cardCCV.text.isEmpty()){
                warning.visibility = View.VISIBLE
            }else{
                SharedApp.finalCallApiList["total"] = SharedApp.ticketList[0].price.toInt() * SharedApp.ticketList[0].seating.size
                SharedApp.finalCallApiList["card"] = cardNumber.text.toString().toInt()
                imageViewWaiting.visibility = View.VISIBLE
                oPayingController.sendClient("insertar", SharedApp.finalCallApiList["emailClient"] as String, SharedApp.finalCallApiList["nameClient"] as String, SharedApp.finalCallApiList["phoneClient"].toString().toInt(), SharedApp.finalCallApiList["firstLastName"].toString(), SharedApp.finalCallApiList["secondLastName"].toString(), SharedApp.finalCallApiList["emailClient"].toString(), requireActivity(), ::finalStep)
                oPayingController.sendTicket(sessionManager)
            }
        }
    }

    private fun finalStep(){
        SharedApp.ticketList[0].dateBuy = Date().toString()
        SharedApp.oLocation.getLastLocation(requireContext()).thenAccept { locationList ->
            SharedApp.oAPISubmitData.sendLocationData(requireActivity(), locationList[0], locationList[1])
        }
        SharedApp.seatSelectedList.clear()
        HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed({
            (activity as PaymentActivity).replaceFragment(SummaryFragment())
            imageViewWaiting.visibility = View.GONE
        }, 5000)
    }
}