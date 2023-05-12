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
 * - [Andres Morales]: [Manage intent]
 */

package com.example.project.view.main.seat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.controller.main.seat.SeatController
import com.example.project.model.SharedApp
import com.example.project.utility.animation.CustomStatusBarIcons
import com.example.project.utility.layout.CustomSpacing
import com.example.project.view.main.MainActivity
import com.example.project.view.main.buy.PaymentActivity

class SeatActivity : AppCompatActivity() {
    private val oAdapter = SeatAdapter()
    private lateinit var btnContinue : Button
    private lateinit var btnBack : ImageButton
    private val oSeatController = SeatController()
    private lateinit var oRecyclerView : RecyclerView
    private lateinit var containerNoSeats: LinearLayout
    private var oCustomStatusBarIcons = CustomStatusBarIcons()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents(){
        setContentView(R.layout.activity_seat)
        oCustomStatusBarIcons.setIconsDark(this)
        btnBack = findViewById(R.id.btn_backSeat)!!
        btnContinue = findViewById(R.id.btnContinueSeat)
        oRecyclerView = findViewById(R.id.lista_asientos)
        containerNoSeats = findViewById(R.id.containerNoSeats)
        oRecyclerView.visibility = View.GONE
        containerNoSeats.visibility = View.GONE
        val data = intent.getBundleExtra("Datos")
        if (data != null) {
            oSeatController.apiCall(data.getInt("IDCALENDARIO"), data.getInt("IDEMPRESA"), this, oRecyclerView, oAdapter, this, containerNoSeats)
        }
        continuePayment()
        goBack()
    }

    private fun continuePayment(){
        btnContinue.setOnClickListener{
            if(SharedApp.seatSelectedList.isNotEmpty()){
                SharedApp.ticketList[0].price = "8000"
                startActivity(Intent(this, PaymentActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Seleccione un asiento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goBack(){
        btnBack.setOnClickListener{
            SharedApp.ticketList.clear()
            SharedApp.seatSelectedList.clear()
            SharedApp.finalCallApiList.clear()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}