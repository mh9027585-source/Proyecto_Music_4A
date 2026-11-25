package com.example.proyecto_grupal_music

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION,
) {

    companion object {
        private const val DATABASE_NAME = "musica.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO = "correo"
        private const val COL_PASSWORD = "password"

        const val TABLE_FAVORITOS = "favoritos"
        const val COL_FAVORITO_ID = "id"
        const val COL_ALBUM_ID = "album_id"
        const val COL_NOMBRE_ALBUM = "nombre_album"
        const val COL_ARTISTA = "artista"
        const val COL_GENERO = "genero"
        const val COL_CANCIONES = "canciones"
        const val COL_FECHA = "fecha"
        const val COL_IMAGEN = "imagen"

        // crear tabla usuarios
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE $TABLE_USUARIOS(
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NOMBRE TEXT NOT NULL,
            $COL_CORREO TEXT NOT NULL UNIQUE,
            $COL_PASSWORD TEXT NOT NULL
            )
        """

        // crear tabla favoritos
        private const val CREATE_TABLE_FAVORITOS = """
            CREATE TABLE $TABLE_FAVORITOS(
            $COL_FAVORITO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_ALBUM_ID INTEGER UNIQUE NOT NULL,
            $COL_NOMBRE_ALBUM TEXT NOT NULL,
            $COL_ARTISTA TEXT,
            $COL_GENERO TEXT,
            $COL_CANCIONES INTEGER,
            $COL_FECHA TEXT,
            $COL_IMAGEN TEXT
            )
        """
    }

    // METODO PARA CREAR LA BASE DE DATOS
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USUARIOS)
        db?.execSQL(CREATE_TABLE_FAVORITOS)
    }

    // METODO PARA ACTUALIZAR LA BASE DE DATOS
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // verificar que la version sea menor a 2
        if (oldVersion < 2) {
            db?.execSQL(CREATE_TABLE_FAVORITOS)
        }
    }

    // metodo para insertar registro
    fun registrarUsuario(nombre: String, correo: String, password: String): Boolean {
        // crear variable para que la base de datos se pueda modificar
        val db = writableDatabase

        // crear variable para recibir los datos
        val datos = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_PASSWORD, password)
        }
        // insertar datos en la tabla
        val resultado = db.insert(TABLE_USUARIOS, null, datos)
        // cerrar la conexion de la base de datos
        db.close()

        // comparar el valor del resultado de la insercion y regresarlo como boolean
        return resultado != -1L
    }

    // metodo para consultar a un usuario
    fun iniciarSesion(correo: String, password: String): Boolean {
        val db = readableDatabase
        val query_sql = """
            SELECT * FROM $TABLE_USUARIOS
            WHERE $COL_CORREO = ? AND $COL_PASSWORD = ?
        """.trimIndent()

        // crear cursor para recorrer la informacion
        val cursor = db.rawQuery(
            query_sql, arrayOf(correo, password)
        )
        // crear variable de tipo bool que permita saber si el cursor encontro un registro
        val usuarioEncontrado = cursor.count > 0
        cursor.close()
        db.close()
        return usuarioEncontrado
    }

    // METODO PARA REGISTRAR INFORMACION PROVENIENTE DEL API
    fun guardarAlbumFavorito(
        albumId: Long,
        nombreAlbum: String,
        artista: String,
        genero: String,
        canciones: Int,
        fecha: String,
        imagen: String
    ): Boolean {
        // declarar el uso de la bd
        val db = writableDatabase

        val datos = ContentValues().apply {
            put(COL_ALBUM_ID, albumId)
            put(COL_NOMBRE_ALBUM, nombreAlbum)
            put(COL_ARTISTA, artista)
            put(COL_GENERO, genero)
            put(COL_CANCIONES, canciones)
            put(COL_FECHA, fecha)
            put(COL_IMAGEN, imagen)
        }

        // insercion
        val resultado = db.insertWithOnConflict(
            TABLE_FAVORITOS,
            null,
            datos,
            SQLiteDatabase.CONFLICT_IGNORE
        )
        return resultado != -1L
    }

    // METODO PARA ELIMINAR UN REGISTRO
    fun eliminarAlbumFavorito(albumId: Long): Boolean {
        val db = writableDatabase

        val filas_afectadas = db.delete(
            TABLE_FAVORITOS,
            "$COL_ALBUM_ID = ?",
            arrayOf(albumId.toString())
        )
        return filas_afectadas > 0
    }

    // metodo para verificar si un registro existe
    fun existeAlbumFavorito(albumId: Long): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_FAVORITO_ID FROM $TABLE_FAVORITOS WHERE $COL_ALBUM_ID = ?",
            arrayOf(albumId.toString())
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }
}