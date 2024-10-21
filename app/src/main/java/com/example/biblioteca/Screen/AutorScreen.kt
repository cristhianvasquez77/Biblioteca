package com.example.biblioteca.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Repository.BibliotecaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorScreen(navController: NavController, bibliotecaRepository: BibliotecaRepository) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }

    var autores by rememberSaveable { mutableStateOf(listOf<Autor>()) }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Nombre") },
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
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text(text = "Apellido") },
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
            value = nacionalidad,
            onValueChange = { nacionalidad = it },
            label = { Text(text = "Nacionalidad") },
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

        val isFormValid = nombre.isNotBlank() && apellido.isNotBlank() && nacionalidad.isNotBlank()

        Button(onClick = {
            if (isFormValid) {
                if (isEditing && selectedAutor != null) {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.actualizarAutor(
                                autorId = selectedAutor!!.autor_id,
                                nombre = nombre,
                                apellido = apellido,
                                nacionalidad = nacionalidad
                            )
                            autores = bibliotecaRepository.obtenerTodosLosAutores()
                        }
                        Toast.makeText(context, "Cambios Guardados", Toast.LENGTH_LONG).show()
                        nombre = ""
                        apellido = ""
                        nacionalidad = ""
                        selectedAutor = null
                        isEditing = false
                    }
                } else {
                    val autor = Autor(
                        nombre = nombre,
                        apellido = apellido,
                        nacionalidad = nacionalidad
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.insertarAutor(autor)
                        }
                        Toast.makeText(context, "Autor Registrado", Toast.LENGTH_LONG).show()
                        nombre = ""
                        apellido = ""
                        nacionalidad = ""
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
                contentDescription = if (isEditing) "Guardar Cambios" else "Registrar Autor"
            )
            Text(text = if (isEditing) "Guardar Cambios" else "Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                autores = withContext(Dispatchers.IO) {
                    bibliotecaRepository.obtenerTodosLosAutores()
                }
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Listar Autores"
            )
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        var showDialog by remember { mutableStateOf(false) }
        var autorToDelete by remember { mutableStateOf<Autor?>(null) }

        Column {
            autores.forEach { autor ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${autor.nombre} \n${autor.apellido}\nNacionalidad: ${autor.nacionalidad}",
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        selectedAutor = autor
                        nombre = autor.nombre
                        apellido = autor.apellido
                        nacionalidad = autor.nacionalidad
                        isEditing = true
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar este autor",
                            tint = Color.Blue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        showDialog = true
                        autorToDelete = autor
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar autor",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            ConfirmationDialog(
                showDialog = showDialog,
                itemToDelete = autorToDelete,
                onConfirm = { item ->
                    val autor = item as Autor
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                bibliotecaRepository.eliminarAutorPorId(autor.autor_id)
                                autores = bibliotecaRepository.obtenerTodosLosAutores()
                            }
                            Toast.makeText(context, "Autor eliminado", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar el autor", Toast.LENGTH_LONG).show()
                        }
                        showDialog = false
                        autorToDelete = null
                    }
                },
                onDismiss = {
                    showDialog = false
                    autorToDelete = null
                }
            )
        }
    }
}
