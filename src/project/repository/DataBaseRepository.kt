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


package com.example.project.repository

import com.google.android.gms.tasks.Task

interface DataBaseRepository<T, K> {
    suspend fun select(id : String):  Map<String, Any>
    fun insert(newT: T)
    fun update(id : String, params: Map<String, Any>)
    fun delete(id : String)
    fun find(k: K): Task<Boolean>
}