package com.example.proyecto_grupal_music

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etCorreo : EditText
            private lateinit var etPass : EditText
            private lateinit var btnInicio: Button
            private lateinit var dbHelper : DataBaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvCrearCuenta = findViewById<TextView>(R.id.tvCrearCuenta)

        etCorreo = findViewById(R.id.etEmail)
        etPass  = findViewById(R.id.etPassword)
        btnInicio = findViewById(R.id.btnIngresar)

        dbHelper = DataBaseHelper(this)
        //Evento para iniciar sesion

        btnInicio.setOnClickListener {
            //todo: agegar metodo de inicio de sesion
            iniciarSesion()
        }

        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)

        }
    }

    //metodo para iniciar sesion
    private fun iniciarSesion(){
        //Acceder a la informacion que escribe el usuario
        val correo = etCorreo.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if(correo.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
//llamar al metodo de inisio de sesion en la BD

       val resultado = dbHelper.iniciarSesion(correo,pass)
        if(resultado){
            //si la sesion es correcta
            Toast.makeText(this, "BIENVENIDO", Toast.LENGTH_SHORT).show()

          val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            //cerrar la pantalla de inicio de sesion
            finish()
        }else{
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }

    }
}
