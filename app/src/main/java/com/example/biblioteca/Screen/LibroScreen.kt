package com.example.biblioteca.Screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Repository.BibliotecaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroScreen(navController: NavController, bibliotecaRepository: BibliotecaRepository) {
    var titulo by remember { mutableStateOf("") }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) }
    var genero by remember { mutableStateOf("") }
    var books by remember { mutableStateOf<List<Libro>>(emptyList()) }
    var autores by remember { mutableStateOf<List<Autor>>(emptyList()) }
    var selectedBook by remember { mutableStateOf<Libro?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            autores = bibliotecaRepository.obtenerTodosLosAutores()
            books = bibliotecaRepository.obtenerTodosLosLibros()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al cargar los datos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text(text = "Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Blue,
                focusedLabelColor = Color.Blue
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = selectedAutor?.let { "${it.nombre} ${it.apellido}" } ?: "",
            onValueChange = {
                searchQuery = it
            },
            label = { Text("Autor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            readOnly = true
        )

        val filteredAutores = autores.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) || it.apellido.contains(searchQuery, ignoreCase = true)
        }

        if (filteredAutores.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(filteredAutores) { autor ->
                    Text(
                        text = "${autor.nombre} ${autor.apellido}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedAutor = autor
                                searchQuery = ""
                            }
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text(text = "Género") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Blue,
                focusedLabelColor = Color.Blue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))


        val isFormValid = titulo.isNotBlank() && selectedAutor != null && genero.isNotBlank()

        Button(onClick = {
            if (isFormValid) {
                scope.launch {
                    try {
                        if (isEditing && selectedBook != null) {
                            bibliotecaRepository.actualizarLibro(
                                libroId = selectedBook!!.libro_id,
                                titulo = titulo,
                                genero = genero,
                                autorId = selectedAutor?.autor_id ?: 0
                            )
                            Toast.makeText(context, "Cambios Guardados", Toast.LENGTH_LONG).show()
                        } else {
                            val book = Libro(
                                titulo = titulo,
                                genero = genero,
                                autor_id = selectedAutor?.autor_id ?: 0
                            )
                            bibliotecaRepository.insertarLibro(book)
                            Toast.makeText(context, "Libro Registrado", Toast.LENGTH_LONG).show()
                        }

                        titulo = ""
                        selectedAutor = null
                        genero = ""
                        books = bibliotecaRepository.obtenerTodosLosLibros()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Por favor complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Done else Icons.Default.Add,
                contentDescription = if (isEditing) "Guardar Cambios" else "Registrar Libro"
            )
            Text(text = if (isEditing) "Guardar Cambios" else "Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Listado de Libros", style = MaterialTheme.typography.bodyLarge)

        var showDialog by remember { mutableStateOf(false) }
        var bookToDelete by remember { mutableStateOf<Libro?>(null) }

        Column {
            books.forEach { book ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val autorNombre = autores.find { it.autor_id == book.autor_id }?.let { "${it.nombre} ${it.apellido}" } ?: "Autor no encontrado"

                    Text(
                        text = "Título: ${book.titulo}\nAutor: $autorNombre\nGénero: ${book.genero}",
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        selectedBook = book
                        titulo = book.titulo
                        genero = book.genero
                        selectedAutor = autores.find { it.autor_id == book.autor_id }
                        isEditing = true
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar este libro",
                            tint = Color.Blue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        showDialog = true
                        bookToDelete = book
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar libro",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

        }

            ConfirmationDialog(
                showDialog = showDialog,
                itemToDelete = bookToDelete,
                onConfirm = { item ->
                    val libro = item as Libro
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                bibliotecaRepository.eliminarLibroPorId(libro.libro_id)
                                books = bibliotecaRepository.obtenerTodosLosLibros()
                            }
                            Toast.makeText(context, "Libro eliminado", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_LONG).show()
                        }
                        showDialog = false
                        bookToDelete = null
                    }
                },
                onDismiss = {
                    showDialog = false
                    bookToDelete = null
                }
            )
        }
    }
}





