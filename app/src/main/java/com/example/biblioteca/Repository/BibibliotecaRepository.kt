package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.BibliotecaDAO
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.Relations.AutorConLibros
import com.example.biblioteca.Relations.LibroConPrestamos
import com.example.biblioteca.Relations.PrestamoConMiembro

class BibliotecaRepository(private val bibliotecaDao: BibliotecaDAO) {

    // Métodos para Autor
    suspend fun insertarAutor(autor: Autor) {
        bibliotecaDao.insertAutor(autor)
    }

    suspend fun obtenerTodosLosAutores(): List<Autor> {
        return bibliotecaDao.getAllAutores()
    }

    suspend fun eliminarAutorPorId(autorId: Int): Int {
        return bibliotecaDao.deleteByIdAutor(autorId)
    }

    suspend fun actualizarAutor(autorId: Int, nombre: String, apellido: String, nacionalidad: String): Int {
        return bibliotecaDao.updateAutor(autorId, nombre, apellido, nacionalidad)
    }

    // Métodos para Libro
    suspend fun insertarLibro(libro: Libro) {
        bibliotecaDao.insertLibro(libro)
    }

    suspend fun obtenerTodosLosLibros(): List<Libro> {
        return bibliotecaDao.getAllLibros()
    }

    suspend fun eliminarLibroPorId(libroId: Int): Int {
        return bibliotecaDao.deleteByIdLibro(libroId)
    }

    suspend fun actualizarLibro(libroId: Int, titulo: String, genero: String, autorId: Int): Int {
        return bibliotecaDao.updateLibro(libroId, titulo, genero, autorId)
    }

    // Métodos para Miembro
    suspend fun insertarMiembro(miembro: Miembro) {
        bibliotecaDao.insertMiembro(miembro)
    }

    suspend fun obtenerTodosLosMiembros(): List<Miembro> {
        return bibliotecaDao.getAllMiembros()
    }

    suspend fun eliminarMiembroPorId(miembroId: Int): Int {
        return bibliotecaDao.deleteByIdMiembro(miembroId)
    }

    suspend fun actualizarMiembro(miembroId: Int, nombre: String, apellido: String, fechaInscripcion: String): Int {
        return bibliotecaDao.updateMiembro(miembroId, nombre, apellido, fechaInscripcion)
    }

    // Métodos para Préstamo
    suspend fun insertarPrestamo(prestamo: Prestamo) {
        bibliotecaDao.insertPrestamo(prestamo)
    }

    suspend fun actualizarPrestamo(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String?): Int {
        return bibliotecaDao.updatePrestamo(prestamoId, libroId, miembroId, fechaPrestamo, fechaDevolucion)
    }

    suspend fun obtenerPrestamos(): List<Prestamo> {
        return bibliotecaDao.getAllPrestamos()
    }

    suspend fun eliminarPrestamoPorId(prestamoId: Int): Int {
        return bibliotecaDao.deletePrestamo(prestamoId)
    }

    // Relaciones
    suspend fun obtenerPrestamoConMiembro(prestamoId: Int): PrestamoConMiembro {
        return bibliotecaDao.getPrestamoConMiembro(prestamoId)
    }

    suspend fun obtenerAutorConLibros(autorId: Int): AutorConLibros {
        return bibliotecaDao.getAutorConLibros(autorId)
    }

    suspend fun obtenerLibroConPrestamos(libroId: Int): LibroConPrestamos {
        return bibliotecaDao.getLibroConPrestamos(libroId)
    }
}
