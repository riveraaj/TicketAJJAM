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

package com.example.project.view.ticket

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Ticket

class TicketAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var ticketList: List<Ticket> = ArrayList()
    private lateinit var context: Context

    fun RecyclerAdapter(ticketList: List<Ticket>, context: Context){
        this.ticketList = ticketList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_ticket, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ticketList[position]
        holder.bind(item, context)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
}