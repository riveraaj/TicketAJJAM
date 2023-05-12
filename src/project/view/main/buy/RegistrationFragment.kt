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

package com.example.project.view.main.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project.R
import com.example.project.model.SharedApp

class RegistrationFragment : Fragment() {
    private lateinit var btnNext : Button
    private lateinit var txtEmail : EditText
    private lateinit var txtName : EditText
    private lateinit var txtPhone : EditText
    private lateinit var warning: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        initComponents(view)
        return view
    }

    private fun initComponents(view: View){
        warning = view.findViewById(R.id.txtWarningRegisterBuyer)
        warning.visibility = View.GONE
        txtEmail = view.findViewById(R.id.emailBuyer)
        txtName = view.findViewById(R.id.nameBuyer)
        txtPhone = view.findViewById(R.id.phoneBuyer)
        btnNext = view.findViewById(R.id.nextStep1) as Button
        goNext()
    }

    private fun goNext(){
        btnNext.setOnClickListener{
            if(txtEmail.text.isEmpty() || txtName.text.isEmpty() || txtPhone.text.isEmpty()){
                warning.visibility = View.VISIBLE
            }else{
                val oPayingFragment = PayingFragment()
                val bundle = Bundle()
                bundle.putString("emailBuyer", txtEmail.text.toString())
                bundle.putString("nameBuyer", txtName.text.toString())
                bundle.putString("phoneBuyer", txtPhone.text.toString())
                SharedApp.ticketList[0].buyer = txtName.text.toString()
                oPayingFragment.arguments = bundle
                SharedApp.finalCallApiList["emailClient"] = txtEmail.text.toString()
                val inputText = txtName.text.toString()
                val nameParts = inputText.split(" ")
                var name = ""
                var firstLastName = ""
                var secondLastName = ""
                if (nameParts.size >= 2) {
                    name = nameParts[0]
                    firstLastName = nameParts[1]
                    secondLastName = if (nameParts.size >= 3) nameParts[2] else ""
                }
                SharedApp.finalCallApiList["nameClient"] = name
                SharedApp.finalCallApiList["firstLastName"] = firstLastName
                SharedApp.finalCallApiList["secondLastName"] = secondLastName
                SharedApp.finalCallApiList["phoneClient"] = txtPhone.text.toString()
                (activity as PaymentActivity).replaceFragment(oPayingFragment)
            }
        }
    }
}