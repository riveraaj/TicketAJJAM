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


package com.example.project.repository.ticket

import android.content.ContentValues
import android.util.Log
import com.example.project.model.SharedApp
import com.example.project.model.Ticket
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class TicketRepositoryImp {

    fun insert(idUser: String, newTicket: Ticket){
        SharedApp.dataBase.collection("user")
            .document(idUser).collection("theater")
            .add(newTicket)
            .addOnSuccessListener {
                Log.d("My Project", "Tikcet: DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.d(ContentValues.TAG, "Error writing document", e)
            }
    }

    suspend fun selectJustOne(id: String, idTicket: String): Ticket?{
            val query = SharedApp.dataBase.collection("user")
                .document(id).collection("theater")
                .document(idTicket)
                .get()
                .await()
            val ticket = query.toObject<Ticket>()
            ticket?.let{
                it.id = query.id
            }
            return ticket
    }

    suspend fun selectAll(id: String): List<Ticket>{
        val tickets = mutableListOf<Ticket>()
        try {
            val query = SharedApp.dataBase.collection("user")
                .document(id).collection("theater")
                .get()
                .await()
            for(document in query.documents){
                val ticket = document.toObject<Ticket>()
                ticket?.let{
                    it.id = document.id
                    tickets.add(it)
                }
            }
            return tickets
        }catch (e: FirebaseFirestoreException){
            Log.d(ContentValues.TAG, e.toString())
        }
        return tickets
    }
}