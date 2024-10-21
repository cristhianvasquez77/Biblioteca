package com.example.biblioteca.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Prestamo

data class LibroConPrestamos(
    @Embedded val libro: Libro,
    @Relation(
        parentColumn = "libro_id",
        entityColumn = "libro_id"
    )
    val prestamos: List<Prestamo>
)