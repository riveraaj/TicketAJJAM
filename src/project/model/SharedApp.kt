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
 * This source code file was created by [Jonathan Rivera Vasquez] and contains original code
 * created by him/her and his/her colleagues.
 *
 * Contributions by:
 * - [Ayram Tolentino]: [create a variable] - ticketList, eventList
 * - [Andres Morales]: [create a variable] - seatList
 *
 */

package com.example.project.model

import android.annotation.SuppressLint
import android.app.Application
import com.example.project.repository.ticket.TicketRepositoryImp
import com.example.project.repository.user.UserRepositoryImp
import com.example.project.service.api.APISubmitData
import com.example.project.service.permission.Contact
import com.example.project.service.permission.Location
import com.example.project.service.session.SessionFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SharedApp : Application() {

    companion object{
        var auxContact = 0
        lateinit var oContact: Contact
        lateinit var oLocation: Location
        lateinit var userList: MutableList<User>
        @SuppressLint("StaticFieldLeak")
        lateinit var dataBase: FirebaseFirestore
        lateinit var oAPISubmitData: APISubmitData
        lateinit var ticketList: MutableList<Ticket>
        lateinit var oSessionFirebase: SessionFirebase
        lateinit var oUserRepositoryImp: UserRepositoryImp
        lateinit var seatList: MutableList<Theater.ADM_BUTACA>
        lateinit var eventList: MutableList<Theater.EventDate>
        lateinit var seatSelectedList: MutableMap<Int, String>
        lateinit var oTicketRepositoryImp: TicketRepositoryImp
        lateinit var finalCallApiList: MutableMap<String, Any>
    }

    override fun onCreate(){
        super.onCreate()
        oContact = Contact()
        oLocation = Location()
        userList = ArrayList()
        userList = ArrayList()
        seatList = ArrayList()
        eventList = ArrayList()
        ticketList = ArrayList()
        finalCallApiList = HashMap()
        seatSelectedList = HashMap()
        dataBase = Firebase.firestore
        oAPISubmitData = APISubmitData()
        oSessionFirebase = SessionFirebase()
        oUserRepositoryImp = UserRepositoryImp()
        oTicketRepositoryImp = TicketRepositoryImp()
    }
}