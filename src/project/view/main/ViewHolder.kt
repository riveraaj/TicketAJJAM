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
 * [ViewHolder]
 *
 * Contributions:
 * - [Ayram Tolentino]: [Created base class]
 * - [Jonathan Rivera Vasquez]: [Code refactoring and information management]
 */

package com.example.project.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.model.Theater
import com.example.project.model.Ticket
import com.example.project.view.main.seat.SeatActivity
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val photo = view.findViewById(R.id.photo) as ImageView
    private val month = view.findViewById(R.id.txtMonth) as TextView
    private val btn = view.findViewById(R.id.item) as RelativeLayout
    private val day = view.findViewById(R.id.txtDayNumber) as TextView
    private val dayName = view.findViewById(R.id.txtDayName) as TextView
    private val playName = view.findViewById(R.id.txtPlayName) as TextView
    private val startHour = view.findViewById(R.id.txtStartHour) as TextView
    private val direction = view.findViewById(R.id.txtDirection) as TextView
    private val description = view.findViewById(R.id.txtDescription) as TextView

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(date: Theater.EventDate, context: Context){
        day.text = date.DIA.toString();
        playName.text = date.EVENTO.NOMBRE.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.ROOT) else it.toString() }
        startHour.text= date.NUMERO_HORA_INICIA.toString() + ":" + date.NUMERO_MINUTO_INICIA.toString() + "0"
        description.text = date.EVENTO.DESCRIPCION.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.ROOT) else it.toString() }
        direction.text = date.EMPRESA.DIRECCION;
        val url = "http://lenguajescuarta-001-site1.ctempurl.com/Imageneseventos/" + date.EVENTO.ID.toString() + ".jpg"
        Picasso.get().load(url)
            .error(R.drawable.shrek)
            .into(photo)
        val selectedDate = LocalDate.of(2023,date.MES.toString().toInt(), date.DIA.toString().toInt());
        month.text = selectedDate.month.getDisplayName(TextStyle.SHORT, Locale("es")).substring(0, 3).uppercase();
        dayName.text = selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es")).substring(0, 3).uppercase();

        btn.setOnClickListener {
            val intent = Intent(context, SeatActivity::class.java)
            val data = Bundle().apply {
                putInt("IDCALENDARIO", date.ID.toInt() )
                putInt("IDEMPRESA", date.EMPRESA.ID.toInt())
            }
            val (lat, long) = date.EMPRESA.DIRECCIONXY.split(" ")

            SharedApp.ticketList.add(
                Ticket("", date.EMPRESA.NOMBRE, "", selectedDate.toString(),
                date.EVENTO.NOMBRE.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                date.NUMERO_HORA_INICIA.toString() + ":" + date.NUMERO_MINUTO_INICIA.toString() + "0",
                "", arrayListOf(), "", lat, long, date.EMPRESA.DIRECCION,
                "http://lenguajescuarta-001-site1.ctempurl.com/Imageneseventos/" + date.EVENTO.ID.toString() + ".jpg")
            )
            SharedApp.finalCallApiList["idCompany"] = date.EMPRESA.ID
            SharedApp.finalCallApiList["idCalendar"] = date.ID
            intent.putExtra("Datos", data)
            context.startActivity(intent);
        }
    }
}