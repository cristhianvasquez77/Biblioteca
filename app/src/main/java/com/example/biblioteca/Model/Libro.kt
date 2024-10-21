package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "libros",
    foreignKeys = [ForeignKey(
        entity = Autor::class,
        parentColumns = ["autor_id"],
        childColumns = ["autor_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["autor_id"])]
)
data class Libro(
    @PrimaryKey(autoGenerate = true)
    val libro_id: Int = 0,
    val titulo: String,
    val genero: String,
    val autor_id: Int
)