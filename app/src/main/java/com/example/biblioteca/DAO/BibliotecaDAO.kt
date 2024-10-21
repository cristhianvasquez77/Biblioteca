package com.example.biblioteca.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.Relations.AutorConLibros
import com.example.biblioteca.Relations.LibroConPrestamos
import com.example.biblioteca.Relations.PrestamoConMiembro

@Dao
interface BibliotecaDAO {
    //Autor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutor(autor: Autor)

    @Query("SELECT * FROM autores")
    suspend fun getAllAutores(): List<Autor>

    @Query ("DELETE FROM autores WHERE autor_id = :autorId")
    suspend fun deleteByIdAutor(autorId: Int): Int

    @Query ("UPDATE autores SET nombre = :nombre, apellido = :apellido, nacionalidad = :nacionalidad WHERE autor_id = :autorId")
    suspend fun updateAutor(autorId: Int, nombre: String, apellido: String, nacionalidad: String): Int

    //LIbro

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibro(libro: Libro)

    @Query("SELECT * FROM libros")
    suspend fun getAllLibros(): List<Libro>

    @Query ("DELETE FROM libros WHERE libro_id = :libroId")
    suspend fun deleteByIdLibro (libroId: Int): Int

    @Query("UPDATE libros SET titulo = :titulo, genero = :genero, autor_id = :autorId WHERE libro_id = :libroId")
    suspend fun updateLibro(libroId: Int, titulo: String, genero: String, autorId: Int): Int

    //Miembros

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMiembro(miembro: Miembro)

    @Query("SELECT * FROM miembros")
    suspend fun getAllMiembros(): List<Miembro>

    @Query ("DELETE FROM miembros WHERE miembro_id = :miembroId")
    suspend fun deleteByIdMiembro (miembroId: Int): Int

    @Query("UPDATE miembros SET nombre = :nombre, apellido = :apellido, fecha_inscripcion = :fechaInscripcion WHERE miembro_id = :miembroId")
    suspend fun updateMiembro(miembroId: Int, nombre: String, apellido: String, fechaInscripcion: String): Int


    //Prestamo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrestamo(prestamo: Prestamo)

    @Query("UPDATE prestamos SET libro_id = :libroId, miembro_id = :miembroId, fecha_prestamo = :fechaPrestamo, fecha_devolucion = :fechaDevolucion WHERE prestamo_id = :prestamoId")
    suspend fun updatePrestamo(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String?): Int

    @Query("DELETE FROM prestamos WHERE prestamo_id = :prestamoId")
    suspend fun deletePrestamo(prestamoId: Int): Int

    @Query("SELECT * FROM prestamos")
    suspend fun getAllPrestamos(): List<Prestamo>

    //Listar relaciones

    @Transaction
    @Query("SELECT * FROM prestamos WHERE prestamo_id = :prestamo_id")
    suspend fun getPrestamoConMiembro(prestamo_id: Int): PrestamoConMiembro

    @Transaction
    @Query("SELECT * FROM autores WHERE autor_id = :autorId")
    suspend fun getAutorConLibros(autorId: Int): AutorConLibros

    @Transaction
    @Query("SELECT * FROM libros WHERE libro_id = :libro_id")
    suspend fun getLibroConPrestamos(libro_id: Int): LibroConPrestamos
}