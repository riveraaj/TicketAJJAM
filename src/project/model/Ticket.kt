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


package com.example.project.model

data class Ticket(
    var id : String,
    var theaterName : String,
    var dateBuy: String,
    var dateEvent : String,
    var nameEvent: String,
    var timeEvent : String,
    var buyer: String,
    var seating: ArrayList<String>,
    var price: String,
    var lat: String,
    var long: String,
    var place: String,
    var url: String
){
    constructor() : this("","","","","","","", arrayListOf(),"","","","", "")}