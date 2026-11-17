package com.example.proyecto_grupal_music

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AlbumDetailActivity : AppCompatActivity() {
    private lateinit var imgPortadaDetalle: ImageView
    private lateinit var tvNombreAlbumDetalle: TextView
    private lateinit var tvArtistaAlbum: TextView
    private lateinit var tvAlbumDetalle: TextView
    private lateinit var btnFavorito: ImageButton

    private var albumFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_album_detail)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        tvNombreAlbumDetalle = findViewById(R.id.tvNombreAlbumDetalle)
        tvArtistaAlbum = findViewById(R.id.tvArtistaAlbumDetalle)
        tvAlbumDetalle = findViewById(R.id.tvAlbumDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mostrarInformacionAlbum()
    }

    private fun mostrarInformacionAlbum() {
        val idAlbum = intent.getLongExtra("ID_ALBUM", 1)
        val nombreAlbum = intent.getStringExtra("NOMBRE_ALBUM")
        val artista = intent.getStringExtra("ARTISTA_ALBUM")
        val genero = intent.getStringExtra("GENERO_ALBUM")
        // Funciona si se manda como Int o como String
        val numeroCanciones = intent.extras?.get("CANCIONES_ALBUM")
        val fecha = intent.getStringExtra("FECHA_ALBUM")
        val imagen = intent.getStringExtra("IMAGEN_ALBUM")

        tvNombreAlbumDetalle.text = nombreAlbum
        tvArtistaAlbum.text = artista
        tvAlbumDetalle.text = genero + "-" + "$numeroCanciones canciones"

        Glide.with(this)
            .load(imagen)
            .centerCrop()
            .into(imgPortadaDetalle)

        btnFavorito.setOnClickListener {
            albumFavorito = !albumFavorito

            if (albumFavorito) {
                btnFavorito.setImageResource(R.drawable.favorito)
                Toast.makeText(this, "Álbum $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
            } else {
                btnFavorito.setImageResource(R.drawable.no_favorito)
                Toast.makeText(this, "Álbum $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}