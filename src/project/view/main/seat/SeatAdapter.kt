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
 * [SeatAdapter]
 *
 * Contributions:
 * - [Andres Morales]: [Created base class]
 * - [Jonathan Rivera Vasquez]: [Code refactoring]
 */

package com.example.project.view.main.seat

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Theater

class SeatAdapter : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var context: Context
    private var availableSeats: MutableList<Theater.ADM_BUTACA> = ArrayList()

    fun RecyclerAdapter(availableSeats: MutableList<Theater.ADM_BUTACA>, context: Context){
        this.availableSeats = availableSeats
        this.context = context
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = availableSeats[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_seat, parent, false))
    }

    override fun getItemCount(): Int {
        return availableSeats.size
    }
}
