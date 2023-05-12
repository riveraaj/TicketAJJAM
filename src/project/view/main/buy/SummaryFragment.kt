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
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.view.ticket.TicketActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SummaryFragment : Fragment() {
    private lateinit var btnNext : Button
    private lateinit var nameEvent: TextView
    private lateinit var priceTotal: TextView
    private lateinit var dateEvent: TextView
    private lateinit var hourEvent: TextView
    private lateinit var theaterName: TextView
    private lateinit var seatList: TextView
    private lateinit var buyerName: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        initComponents(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponents(view: View){
        (activity as PaymentActivity).updateProgress(2)
        btnNext = view.findViewById(R.id.nextStep3)
        nameEvent = view.findViewById(R.id.nameEventSummary)
        priceTotal = view.findViewById(R.id.priceTotalSummary)
        dateEvent = view.findViewById(R.id.dateTicketSummary)
        hourEvent = view.findViewById(R.id.hourTicketSummary)
        theaterName = view.findViewById(R.id.nameTheaterTicket)
        seatList = view.findViewById(R.id.seatsTicketSummary)
        buyerName = view.findViewById(R.id.nameBuyerTicketSummary)
        finish()
        bind()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun bind(){
        val seating = SharedApp.ticketList[0].seating.dropLast(1).joinToString(", ")
        val lastSeat = SharedApp.ticketList[0].seating.lastOrNull()
        val date = LocalDate.parse(SharedApp.ticketList[0].dateEvent)
        val result = date.format(DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault()))

        nameEvent.text = SharedApp.ticketList[0].nameEvent
        buyerName.text = "Comprador: ${SharedApp.ticketList[0].buyer}"
        priceTotal.text = "Total: ₡${SharedApp.ticketList[0].price.toInt() * SharedApp.ticketList[0].seating.size}"
        dateEvent.text = "Fecha: ${result.replaceFirstChar { it.uppercase(Locale.getDefault())}}"
        seatList.text = "${seating}${if (lastSeat != null) ", $lastSeat" else ""}"
        hourEvent.text = "Hora: ${SharedApp.ticketList[0].timeEvent}"
        theaterName.text = "Lugar: ${SharedApp.ticketList[0].place}"
    }

    private fun finish(){
        btnNext.setOnClickListener{
            (activity as PaymentActivity).updateProgress(3)
            HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed({
                SharedApp.ticketList.clear()
                SharedApp.seatSelectedList.clear()
                SharedApp.finalCallApiList.clear()
                startActivity(Intent(requireContext(), TicketActivity::class.java))
                requireActivity().finish()
            }, 1250)
        }
    }
}