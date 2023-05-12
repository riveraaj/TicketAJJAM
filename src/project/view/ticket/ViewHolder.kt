/**
 * Copyright (c) [2023] [Jonathan Rivera Vasquez]
 * All Rights Reserved.
 *
 * This code is the intellectual property of [Jonathan Rivera Vasquez].
 * You may not use, modify, or distribute this code without the express
 * written permission of [Jonathan Rivera Vasquez].
 *
 */

package com.example.project.view.ticket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Ticket
import com.example.project.view.ticket.detail.DetailActivity

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private var nameTicket = view.findViewById(R.id.nameTicket) as TextView
    private var priceTicket = view.findViewById(R.id.priceTicket) as TextView
    private var hourTicket = view.findViewById(R.id.hourTicket) as TextView
    private var placeTicket = view.findViewById(R.id.placeTicket) as TextView
    private var theaterTicket = view.findViewById(R.id.theaterTicket) as TextView
    private var btnDetail = view.findViewById(R.id.btnDetail) as TextView
    private var amountSeat = view.findViewById(R.id.numberPersonTicket) as TextView

    @SuppressLint("SetTextI18n")
    fun bind(oTicket : Ticket, context: Context){
        val unitPrice = oTicket.price.toInt()
        val amountSeats = oTicket.seating.size
        nameTicket.text = oTicket.nameEvent
        priceTicket.text = "₡${if(unitPrice * amountSeats != 0) unitPrice * amountSeats else "Sin datos"}"
        hourTicket.text = "${oTicket.dateEvent} | ${oTicket.timeEvent}"
        placeTicket.text = oTicket.place
        theaterTicket.text = oTicket.theaterName
        amountSeat.text = oTicket.seating.size.toString()
        btnDetail.setOnClickListener{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("idTicket", oTicket.id)
            context.startActivity(intent);
        }
    }
}