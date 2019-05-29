package com.josemartinez.proyectofinal


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.josemartinez.proyectofinal.databinding.FragmentCrearCuentaBinding

class CrearCuentaFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Crear cuenta"
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val binding: FragmentCrearCuentaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_crear_cuenta, container, false)

        binding.crearCrearCuentaButton.setOnClickListener {

            val email = binding.correoCrearCuentaEdittext.text.toString()
            val password = binding.contrasenaCrearCuentaEdittext.text.toString()

            if( email != "" && password != ""){
                hideKeyboard(this.context!!)

                val builder = AlertDialog.Builder(this.context!!)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                builder.setView(dialogView)
                builder.setCancelable(false)

                val dialog = builder.create()
                dialog.show()

                auth.createUserWithEmailAndPassword(
                    binding.correoCrearCuentaEdittext.text.toString(),
                    binding.contrasenaCrearCuentaEdittext.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val data = HashMap<String, Any>()
                        data.put("email", email)
                        db.collection("users").add(data)
                        Toast.makeText(this.context, "Se creado al usuario", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this.context, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                    }

                    dialog.hide()
                }
            } else {
                if(binding.correoCrearCuentaEdittext.text.toString() == ""){
                    binding.correoCrearCuentaEdittext.error = "Llena el espacio"
                }

                if(binding.contrasenaCrearCuentaEdittext.text.toString() == ""){
                    binding.contrasenaCrearCuentaEdittext.error = "Llena el espacio"
                }

                Toast.makeText(this.context, "Llene todos los campos.", Toast.LENGTH_SHORT).show()
            }


        }
        return binding.root
    }

    fun hideKeyboard(mContext: Context) {
        val imm = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            (mContext as Activity).window
                .currentFocus!!.windowToken, 0
        )
    }

}


