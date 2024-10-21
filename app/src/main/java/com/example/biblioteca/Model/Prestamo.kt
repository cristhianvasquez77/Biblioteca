package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "prestamos",
    foreignKeys = [
        ForeignKey(
            entity = Libro::class,
            parentColumns = ["libro_id"],
            childColumns = ["libro_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Miembro::class,
            parentColumns = ["miembro_id"],
            childColumns = ["miembro_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["libro_id"]), Index(value = ["miembro_id"])]
)
data class Prestamo(
    @PrimaryKey(autoGenerate = true)
    val prestamo_id: Int = 0,
    val fecha_prestamo: String,
    val fecha_devolucion: String?,
    val libro_id: Int,
    val miembro_id: Int
)