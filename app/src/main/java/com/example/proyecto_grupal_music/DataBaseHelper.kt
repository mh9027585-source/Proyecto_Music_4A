package com.example.proyecto_grupal_music

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DataBaseHelper(context: Context) : SQLiteOpenHelper (
    context,
    DataBASE_NAME,
    null,
    DATABASE_VERSION,
) {

    companion object{
        private const val DataBASE_NAME = "musica.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO= "correo"
        private const val COL_PASSWORD = "password"

    }

    //METODO PARA CREAR LA BASE DE DATOS
    override fun onCreate(db: SQLiteDatabase?) {
     val crearTablaUsuarios = """
         
         CREATE TABLE $TABLE_USUARIOS(
         $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
         $COL_NOMBRE TEXT NOT NULL,
         $COL_CORREO TEXT NOT NULL UNIQUE,
         $COL_PASSWORD TEXT NOT NULL
         
         )
        
         """.trimIndent()
        //CREAR TABLA DE SQL

        db?.execSQL(crearTablaUsuarios)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(p0)
    }
    //metodo para insetar registro
    fun registrarUsuario(nombre: String, correo : String, password: String): Boolean{
        //crear variable para que la base de datos se pueda modificar
        val db = writableDatabase

        // crear variable para recibir los datos
        val datos = ContentValues().apply {
            put(COL_NOMBRE,nombre)
            put(COL_CORREO,correo)
            put(COL_PASSWORD,password)

        }
        // insertar datos en la tabla
         val resultado = db.insert(TABLE_USUARIOS, null, datos)
        //crear la conexion de la base de datos
       db.close()

        //Comparar el  valor del resultado de la insercion y agregarlo como boleano
        return resultado != -1L
    }
    fun iniciarSesion(correo: String,password: String): Boolean{
       val db = readableDatabase
        val query_sql = """
            SELECT * FROM $TABLE_USUARIOS
            WHERE $COL_CORREO = ? AND $COL_PASSWORD =?
            
        """.trimIndent()
        //CREAR PULSOR PARA CRECORRER LA INFORMACION
        val cursor = db.rawQuery(
            query_sql,arrayOf(correo, password)
        )
        //crear variable de tipo bool que permita saber si el cursor encontro un registro
        val usuarioEncontrado = cursor.count > 0
        cursor.close()
        db.close()
        return  usuarioEncontrado
    }

}