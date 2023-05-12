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
 * - [Jonathan Rivera Vasquez]: [Code refactoring]
 */

package com.example.project.view.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.controller.main.CalendarController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var btnToLeft: Button
    private lateinit var btnToRight: Button
    private val oAdapter = CalendarAdapter()
    private lateinit var txtYearMonth: TextView
    private lateinit var currentDate: LocalDate
    private lateinit var oRecyclerView : RecyclerView
    private var oCalendarController = CalendarController()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponents(view: View){
        currentDate = LocalDate.now()
        btnToLeft = view.findViewById(R.id.btnToLeft)
        btnToRight = view.findViewById(R.id.btnToRight)
        txtYearMonth = view.findViewById(R.id.txtYearMonth)
        oRecyclerView = view.findViewById(R.id.listadeobras)
        txtYearMonth.text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        oCalendarController.apiCall(currentDate.month.value.toString(),currentDate.year.toString(), requireActivity(), oRecyclerView, oAdapter, requireContext())
        btnToRight.setOnClickListener{
            nextMonth()
        }
        btnToLeft.setOnClickListener{
            previousMonth()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonth(){
        currentDate = currentDate.minusMonths(1);
        txtYearMonth.text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))).replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.ROOT) else it.toString() }
        oCalendarController.apiCall(currentDate.month.value.toString(),currentDate.year.toString(), requireActivity(), oRecyclerView, oAdapter, requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonth(){
        currentDate = currentDate.plusMonths(1);
        txtYearMonth.text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))).replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.ROOT) else it.toString() }
        oCalendarController.apiCall(currentDate.month.value.toString(),currentDate.year.toString(), requireActivity(), oRecyclerView, oAdapter, requireContext())
    }
}