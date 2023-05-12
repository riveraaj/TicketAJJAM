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
 * - [Andres Morales]: [Help to create base function (bind)]
 */

package com.example.project.view.main.seat

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Theater
import com.example.project.model.SharedApp

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var seatSelected = view.findViewById(R.id.seatSelect) as CheckBox
    private var priceSeat = view.findViewById(R.id.priceSeatID) as TextView
    private var seats = view.findViewById(R.id.numberSeatID) as TextView

    @SuppressLint("SetTextI18n")
    fun bind(seat: Theater.ADM_BUTACA){
        priceSeat.text = "₡8000"
        seats.text = seat.ID_BUTACACOORDENADA.NOMBREDEFANTASIA
        seatSelected.setOnCheckedChangeListener { _, isChecked ->
            checkSeatSelected(seat, isChecked)
        }
    }

    private fun checkSeatSelected(seat: Theater.ADM_BUTACA, isChecked: Boolean){
        if(isChecked){
            SharedApp.ticketList[0].seating.add(seat.ID_BUTACACOORDENADA.NOMBREDEFANTASIA)
            SharedApp.seatSelectedList[seat.ID_BUTACACOORDENADA.ID] = seat.ID_BUTACACOORDENADA.NOMBREDEFANTASIA
        }else{
            SharedApp.ticketList[0].seating.removeIf{ it == seat.ID_BUTACACOORDENADA.NOMBREDEFANTASIA}
            SharedApp.seatSelectedList.remove(seat.ID_BUTACACOORDENADA.ID)
        }
    }
}