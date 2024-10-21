package com.example.biblioteca.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.biblioteca.DAO.BibliotecaDAO
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo

@Database(
    entities = [Autor::class, Libro::class, Prestamo::class, Miembro::class],
    version = 1
)
abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun bibliotecaDao(): BibliotecaDAO

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getDatabase(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibliotecaDatabase::class.java,
                    "biblioteca_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}