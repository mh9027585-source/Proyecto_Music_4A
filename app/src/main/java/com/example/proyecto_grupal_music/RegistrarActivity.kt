package com.example.proyecto_grupal_music

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistrarActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var btnRegistrar: Button

    //crear objeto de la clase de la base de datos
    private lateinit var bdHelper: DataBaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

//Iniciar el objeto de la base de datos
        bdHelper = DataBaseHelper(this)
        //Evento para registar a un usuario
        btnRegistrar.setOnClickListener {
            //llamar al metodo de registrar usuario
            registrarUsuario()
        }

    }

    //metodo para registrar un usuario
    private fun registrarUsuario() {
        // Obtener la información que ingresa el usuario
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // 1. Validar que los campos tengan información (incluyendo el password) y detener con return
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return // Esto detiene la ejecución para que no intente registrar datos vacíos
        }

        // 2. Llamar a nuestra base de datos y guardar el resultado de la inserción
        // (Asegúrate de que este método exista y esté bien escrito en tu clase DataBaseHelper)
        val resultado = bdHelper.registrarUsuario(nombre, email, password)

        if (resultado) {
            Toast.makeText(this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "REGISTRO FALLIDO", Toast.LENGTH_SHORT).show()
        }
    }
}