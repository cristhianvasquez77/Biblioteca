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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Repository.BibliotecaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiembroScreen(navController: NavController, bibliotecaRepository: BibliotecaRepository) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fechaInscripcion by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }

    var miembros by rememberSaveable { mutableStateOf(listOf<Miembro>()) }
    var selectedMiembro by remember { mutableStateOf<Miembro?>(null) }
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
            value = fechaInscripcion,
            onValueChange = { fechaInscripcion = it },
            label = { Text(text = "Fecha de Inscripción") },
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

        val isFormValid = nombre.isNotBlank() && apellido.isNotBlank() && fechaInscripcion.isNotBlank()

        Button(onClick = {
            if (isFormValid) {
                if (isEditing && selectedMiembro != null) {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.actualizarMiembro(
                                miembroId = selectedMiembro!!.miembro_id,
                                nombre = nombre,
                                apellido = apellido,
                                fechaInscripcion = fechaInscripcion
                            )
                            miembros = bibliotecaRepository.obtenerTodosLosMiembros()
                        }
                        Toast.makeText(context, "Cambios Guardados", Toast.LENGTH_LONG).show()
                        nombre = ""
                        apellido = ""
                        fechaInscripcion = ""
                        selectedMiembro = null
                        isEditing = false
                    }
                } else {
                    val miembro = Miembro(
                        nombre = nombre,
                        apellido = apellido,
                        fecha_inscripcion = fechaInscripcion
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.insertarMiembro(miembro)
                        }
                        Toast.makeText(context, "Miembro Registrado", Toast.LENGTH_LONG).show()
                        nombre = ""
                        apellido = ""
                        fechaInscripcion = ""
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
                contentDescription = if (isEditing) "Guardar Cambios" else "Registrar Miembro"
            )
            Text(text = if (isEditing) "Guardar Cambios" else "Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                miembros = withContext(Dispatchers.IO) {
                    bibliotecaRepository.obtenerTodosLosMiembros()
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
                contentDescription = "Listar Miembros"
            )
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        var showDialog by remember { mutableStateOf(false) }
        var miembroToDelete by remember { mutableStateOf<Miembro?>(null) }

        Column {
            miembros.forEach { miembro ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "${miembro.nombre} \n${miembro.apellido}\nFecha Inscripción: ${miembro.fecha_inscripcion}",
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        selectedMiembro = miembro
                        nombre = miembro.nombre
                        apellido = miembro.apellido
                        fechaInscripcion = miembro.fecha_inscripcion
                        isEditing = true
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar este miembro",
                            tint = Color.Blue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        showDialog = true
                        miembroToDelete = miembro
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar miembro",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            ConfirmationDialog(
                showDialog = showDialog,
                itemToDelete = miembroToDelete,
                onConfirm = { item ->
                    val miembro = item as Miembro
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                bibliotecaRepository.eliminarMiembroPorId(miembro.miembro_id)
                                miembros = bibliotecaRepository.obtenerTodosLosMiembros()
                            }
                            Toast.makeText(context, "Miembro eliminado", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar el miembro", Toast.LENGTH_LONG).show()
                        }
                        showDialog = false
                        miembroToDelete = null
                    }
                },
                onDismiss = {
                    showDialog = false
                    miembroToDelete = null
                }
            )
        }
    }
}
