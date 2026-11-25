package com.example.proyecto_grupal_music

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPass: EditText
    private lateinit var btnInicio: Button
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private lateinit var btnGoogle: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvCrearCuenta = findViewById<TextView>(R.id.tvCrearCuenta)

        etCorreo = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPassword)
        btnInicio = findViewById(R.id.btnIngresar)

        dbHelper = DataBaseHelper(this)
        //Evento para iniciar sesion

        btnGoogle = findViewById(R.id.btnGoogle)
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)


        btnInicio.setOnClickListener {
            //todo: agegar metodo de inicio de sesion
            iniciarSesion()
        }

        btnGoogle.setOnClickListener {
            iniciarSesionGoogle()
        }

        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)

        }
    }

    //metodo para iniciar sesion
    private fun iniciarSesion() {
        //Acceder a la informacion que escribe el usuario
        val correo = etCorreo.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (correo.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
//llamar al metodo de inisio de sesion en la BD

        val resultado = dbHelper.iniciarSesion(correo, pass)
        if (resultado) {
            //si la sesion es correcta
            Toast.makeText(this, "BIENVENIDO", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            //cerrar la pantalla de inicio de sesion
            finish()
        } else {
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }

    }

    // 3. MÉTODO PARA AUTENTICAR EN FIREBSAE
    private fun autenticarFirebase(idToken: String) {

        val credencialFirebase = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credencialFirebase).addOnCompleteListener(this) {

                inicio ->
            //validar si el inicio de sesion con firebase es exitoso
            if (inicio.isSuccessful) {
                //variable para obtener toda la inf del usuario
                val usuario = auth.currentUser
                Toast.makeText(this, "Bienvenido ${usuario?.displayName}", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al validar con Firebase", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 4. MÉTODO PARA PROCESAR LA CREDENCIAL
    private fun procesarCredencial(credential: Credential) {

        if (credential is CustomCredential && credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            try {
                val credencialGoogle = GoogleIdTokenCredential.createFrom(
                    credential.data
                )
                autenticarFirebase(credencialGoogle.idToken)

            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(this, "Credencial invalida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. MÉTODO PARA INICIAR SESIÓN
    private fun iniciarSesionGoogle() {

        val inicioGoogle = GetSignInWithGoogleOption.Builder(
            getString(R.string.default_web_client_id)
        ).build()

        val solicitud = GetCredentialRequest.Builder()
            .addCredentialOption(inicioGoogle)
            .build()
        lifecycleScope.launch {
            try {
                val resultado = credentialManager.getCredential(
                    context = this@MainActivity,
                    request = solicitud
                )

            } catch (e: GetCredentialException) {
                Toast.makeText(this@MainActivity, "Error al iniciar sesion", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()


        val usuarioctual = auth.
        currentUser
        if (usuarioctual != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
