package com.example.proyecto_grupal_music

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_grupal_music.model.Album
import com.example.proyecto_grupal_music.model.ITunesResponse
import com.example.proyecto_grupal_music.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var tvEstado: TextView
    private lateinit var contenedorIndie: LinearLayout
    private lateinit var contenedorCorridos: LinearLayout
    private lateinit var contenedorDannyOcean: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvEstado = findViewById(R.id.tvEstado)
        contenedorIndie = findViewById(R.id.contenedorIndie)
        contenedorCorridos = findViewById(R.id.contenedorCorridos)
        contenedorDannyOcean = findViewById(R.id.contenedorDannyOcean)

        // Se carga cada género en su propio contenedor
        obtenerAlbum("rock mexicano", contenedorIndie)
        obtenerAlbum("corridos", contenedorCorridos)
        obtenerAlbum("pop", contenedorDannyOcean)
    }

    // Metodo para cargar los albumnes
    private fun obtenerAlbum(genero: String, contenedor: LinearLayout) {
        RetrofitClient.api.obtenerAlbumGenero(
            genero = genero,
            pais = "MX",
            media = "music",
            entidad = "album",
            limite = 10,
        ).enqueue(object : Callback<ITunesResponse> {

            // Metodos obligatorios para el funcionanmiento
            // Metodo para verificar que la respuesta sea correcta
            override fun onResponse(call: Call<ITunesResponse?>, response: Response<ITunesResponse?>) {

                // Variable que obtenga los resultados del API
                val albumes = response.body()?.results ?: emptyList()

                // Si la lista de album no esta vacia se manda a llamar a mostrarAlbunes
                if (albumes.isNotEmpty()) {
                    mostrarAlbunes(albumes, contenedor)
                } else {
                    Toast.makeText(this@HomeActivity, "No hay Resultados", Toast.LENGTH_SHORT).show()
                }
            }

            // Metodo para verificar si no hay respuesta
            override fun onFailure(call: Call<ITunesResponse?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en el Api", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Metodo para mostrar un listado de albumnes
    private fun mostrarAlbunes(albumes: List<Album>, contenedor: LinearLayout) {
        // Evitar resultados duplicados
        contenedor.removeAllViews()

        // Ciclo para recorrer la informacion de la respuesta
        for (album in albumes) {
            // Indicar la vista en la que se va a mostrar la info (item_album)
            val vistaAlbum = layoutInflater.inflate(
                R.layout.item_album,
                contenedor,
                false
            )

            val imgPortada = vistaAlbum.findViewById<ImageView>(R.id.imgPortadaAlbum)
            val tvNombreAlbum = vistaAlbum.findViewById<TextView>(R.id.tvNombreAlbum)
            val tvNombreArtista = vistaAlbum.findViewById<TextView>(R.id.tvNombreArtista)

            tvNombreAlbum.text = album.collectionName ?: "Album sin nombre"
            tvNombreArtista.text = album.artistName ?: "Artista desconocido"

            Glide.with(this)
                .load(album.obtenerImagenGrande())
                .centerCrop()
                .into(imgPortada)
            //generar evento
            vistaAlbum.setOnClickListener {
                //variable para verificar si el album tiene id
                val idAlbum = album.collectionId
                if (idAlbum!=null){
                    val intent = Intent(this@HomeActivity, AlbumDetailActivity::class.java)
                    intent.putExtra("ID_ALBUM",idAlbum)
                    intent.putExtra("NOMBRE_ALBUM",album.collectionName)
                    intent.putExtra("ARTISTA_ALBUM",album.artistName)
                    intent.putExtra("GENERO_ALBUM",album.primaryGenreName)
                    intent.putExtra("CANCIONES_ALBUM",album.trackCount)
                    intent.putExtra("FECHA_ALBUM",album.releaseDate)
                    intent.putExtra("IMGEN_ALBUM",album.obtenerImagenGrande())

                    startActivity(intent)

                }
            }

            // Mostrar la informacion
            contenedor.addView(vistaAlbum)

        }
    }
}