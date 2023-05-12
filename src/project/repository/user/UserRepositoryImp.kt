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


package com.example.project.repository.user

import android.content.ContentValues
import android.util.Log
import com.example.project.model.SharedApp
import com.example.project.model.User
import com.example.project.repository.DataBaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class UserRepositoryImp : DataBaseRepository<User, String>{

    override fun insert(newUser: User){
        SharedApp.dataBase.collection("user")
            .add(newUser)
            .addOnSuccessListener  {x ->
                Log.d("My Project", "User insert: DocumentSnapshot successfully written!")
                val userDocRef  = SharedApp.dataBase.collection("user").document(x.id)
                val ordersCollectionRef = userDocRef.collection("theater")
                ordersCollectionRef.document("0").set(emptyMap<String, Any>())
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }

    override fun update(id: String, toUpdate: Map<String, Any>){
        val user = SharedApp.dataBase.collection("user").document(id)
        user.set(toUpdate, SetOptions.merge())
            .addOnSuccessListener { Log.d("My Project", "User update: DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error updating document", e) }
    }

    override suspend fun select(id: String): Map<String, Any> {
        try {
            val query = SharedApp.dataBase.collection("user").document(id)
                .get()
                .await()
            if (query.exists()) {
                return query.data ?: emptyMap()
            }
        }catch (e: FirebaseFirestoreException){
            Log.d(ContentValues.TAG, e.toString())
        }
        return emptyMap()
    }

    override fun delete(id: String) {
        val user = SharedApp.dataBase.collection("user").document(id)
        user.delete()
            .addOnSuccessListener { Log.d("My Project", "User Delete: DocumentSnapshot successfully delete!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
    }

    override fun find(k: String): Task<Boolean> {
        TODO("Not yet implemented")
    }

    suspend fun getUserIdByEmail(email: String): String? {
        try {
            val userCollectionRef = SharedApp.dataBase.collection("user")
            val query = userCollectionRef
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!query.isEmpty) {
                val document = query.documents[0]
                return document.id
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d(ContentValues.TAG, e.toString())
        }
        return null
    }

    fun isEmailAlreadyRegistered(email: String): Task<Boolean> {
        val userCollectionRef = SharedApp.dataBase.collection("user")
        val query = userCollectionRef.whereEqualTo("email", email)
        return query.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.let { querySnapshot ->
                    return@continueWith (querySnapshot.size() > 0)
                }
            }
            return@continueWith false
        }
    }
}