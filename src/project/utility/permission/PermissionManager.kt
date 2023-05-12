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


package com.example.project.utility.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat

class PermissionManager(private val activity: Activity) {
    private val PERMISSIONS_CODE = 100

    fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_CODE)
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray): Boolean {
        return requestCode == PERMISSIONS_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }

    fun showPermissionDeniedMessage() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Permisos necesarios")
        builder.setMessage("Para el correcto funcionamiento de la aplicación, se requieren permisos especiales. Por favor, otorgue los permisos necesarios en la configuración de la aplicación.")
        builder.setPositiveButton("Configuración") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", activity.packageName, null)
            activity.startActivity(intent)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}