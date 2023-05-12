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


package com.example.project.service.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.concurrent.CompletableFuture

class Location{

    fun getLastLocation(context: Context): CompletableFuture<MutableList<String>> {
        val location = mutableListOf<String>()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val future = CompletableFuture<MutableList<String>>()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { locate: Location? ->
                    location.add(locate?.latitude.toString())
                    location.add(locate?.longitude.toString())
                    location.add(Date().toString())
                    future.complete(location)
                }
                .addOnFailureListener {
                    location.add("0")
                    location.add("0")
                    future.complete(location)
                }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            future.complete(location)
        }
        return future
    }
}