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


package com.example.project.view.ticket.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.service.session.SessionManager
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.view.ticket.TicketActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.runBlocking

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var idTicket : String
    private lateinit var nameBuyer: TextView
    private lateinit var priceTicket : TextView
    private lateinit var dateTicket : TextView
    private lateinit var hourTicket : TextView
    private lateinit var nameTheater : TextView
    private lateinit var seatsTicket : TextView
    private lateinit var nameEvent : TextView
    private lateinit var btnBack : ImageButton
    private lateinit var sessionManager: SessionManager
    private var oCustomStatusBarIcons = CustomStatusBarIcons()
    private lateinit var mapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents(){
        setContentView(R.layout.activity_detail)
        oCustomStatusBarIcons.setIconsDark(this)
        btnBack = findViewById(R.id.btnBackDetail)
        priceTicket = findViewById(R.id.priceTicketDetail)
        dateTicket = findViewById(R.id.dateTicketDetail)
        hourTicket = findViewById(R.id.hourTicketDetail)
        nameTheater = findViewById(R.id.nameTheaterTicket)
        seatsTicket = findViewById(R.id.seatsTicket)
        nameEvent = findViewById(R.id.nameEventTicket)
        nameBuyer = findViewById(R.id.nameBuyerTicket)
        sessionManager = SessionManager(this)
        idTicket = intent.extras?.getString("idTicket").toString()
        mapFragment = (supportFragmentManager.findFragmentById(R.id.mapTheather) as? SupportMapFragment)!!
        mapFragment.getMapAsync(this)
        bind()
        goBack()
    }

    @SuppressLint("SetTextI18n")
    private fun bind(){
        val ticket = runBlocking {
            SharedApp.oTicketRepositoryImp.selectJustOne(sessionManager.getUserId(), idTicket)
        }
        val unitPrice = ticket?.price?.toInt() ?: 0
        val amountSeat = ticket?.seating?.size ?: 0
        nameEvent.text = ticket?.nameEvent ?: "Dracula"
        priceTicket.text = "Total: ₡${if(unitPrice * amountSeat != 0) unitPrice * amountSeat else "Sin datos"}"
        dateTicket.text = "Fecha: ${ticket?.dateEvent ?: "sin datos"}"
        hourTicket.text = "Hora: ${ticket?.timeEvent  ?: "sin datos"}"
        nameTheater.text = "Lugar: ${ticket?.theaterName  ?: "sin datos"}"
        val seating = ticket?.seating?.dropLast(1)?.joinToString(", ")
        val lastSeat = ticket?.seating?.lastOrNull()
        seatsTicket.text = "${seating}${if (lastSeat != null) ", $lastSeat" else ""}"
        nameBuyer.text = "Comprador: ${ticket?.buyer  ?: "sin datos"}"
    }

    private fun goBack(){
        btnBack.setOnClickListener{
            startActivity(Intent(this, TicketActivity::class.java))
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ticket = runBlocking {
            SharedApp.oTicketRepositoryImp.selectJustOne(sessionManager.getUserId(), idTicket)
        }
        val position = ticket?.lat?.let {
            LatLng(it.toDouble(), ticket.long.toDouble())
        } ?: LatLng(0.0, 0.0)

        googleMap.addMarker(
            MarkerOptions()
                .position(position)
                .title(ticket?.place))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
    }
}