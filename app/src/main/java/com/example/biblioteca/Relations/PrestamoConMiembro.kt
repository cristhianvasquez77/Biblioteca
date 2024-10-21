package com.example.biblioteca.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo

data class PrestamoConMiembro(
    @Embedded
    val prestamo: Prestamo,
    @Relation(
        parentColumn = "miembro_id",
        entityColumn = "miembro_id"
    )
    val miembro: Miembro
)