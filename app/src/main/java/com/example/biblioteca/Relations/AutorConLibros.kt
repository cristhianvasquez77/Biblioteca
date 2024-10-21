package com.example.biblioteca.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro

data class AutorConLibros(
    @Embedded
    val autor: Autor,
    @Relation(
        parentColumn = "autor_id",
        entityColumn = "autor_id"
    )
    val libros: List<Libro>
)