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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.SharedApp
import com.example.project.model.Ticket
import com.example.project.service.session.SessionManager
import com.example.project.utility.layout.CustomSpacing
import kotlinx.coroutines.runBlocking

class TicketFragment : Fragment() {
    lateinit var oRecyclerView: RecyclerView
    lateinit var sessionManager: SessionManager
    private val oAdapter = TicketAdapter()
    private lateinit var warning : LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)
        setUpRecyclerView(view)
        return view
    }

    private fun setUpRecyclerView(view: View){
        sessionManager = SessionManager(requireContext())
        oRecyclerView = view.findViewById(R.id.ticketList)
        warning = view.findViewById(R.id.containerNoTickets)
        warning.visibility = View.GONE
        oRecyclerView.visibility = View.GONE
        oRecyclerView.addItemDecoration(CustomSpacing(200))
        val ticket = runBlocking {
            SharedApp.oTicketRepositoryImp.selectAll(sessionManager.getUserId())
        }
        if(ticket.size > 1){
            oRecyclerView.visibility = View.VISIBLE
            warning.visibility = View.GONE
            oRecyclerView.setHasFixedSize(true)
            oRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            val ticketFiltered = ticket.filter { it.id != "0" }
            oAdapter.RecyclerAdapter(ticketFiltered, requireContext())
            oRecyclerView.adapter = oAdapter
        }else{
            oRecyclerView.visibility = View.GONE
            warning.visibility = View.VISIBLE
        }
    }
}