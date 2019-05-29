package com.josemartinez.proyectofinal


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.josemartinez.proyectofinal.databinding.FragmentLoginBinding
import android.app.Activity



class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Login"
        auth = FirebaseAuth.getInstance()
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.crearCuentaButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_crearCuentaFragment2)
        )

        binding.loginLoginButton.setOnClickListener{

            val email = binding.correoLoginEdittext.text.toString()
            val password = binding.contrasenaLoginEdittext.text.toString()

            if(email != "" && password != ""){
                hideKeyboard(this.context!!)
                
                val builder = AlertDialog.Builder(this.context!!)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                builder.setView(dialogView)
                builder.setCancelable(false)

                val dialog = builder.create()
                dialog.show()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this.context, "Se ha iniciado sesión correctamente", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this.context, "Se ha iniciado sesión incorrectamente", Toast.LENGTH_LONG).show()
                        }

                        dialog.hide()
                    }
            } else {
                if(binding.correoLoginEdittext.text.toString() == ""){
                    binding.correoLoginEdittext.error = "Llena el espacio"
                }

                if(binding.contrasenaLoginEdittext.text.toString() == ""){
                    binding.contrasenaLoginEdittext.error = "Llena el espacio"
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
