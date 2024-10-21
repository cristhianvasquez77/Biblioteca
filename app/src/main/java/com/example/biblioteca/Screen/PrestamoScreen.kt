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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.Repository.BibliotecaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrestamoScreen(navController: NavController, bibliotecaRepository: BibliotecaRepository, libros: List<Libro>, miembros: List<Miembro>) {
    var libroId by remember { mutableStateOf("") }
    var miembroId by remember { mutableStateOf("") }
    var fechaPrestamo by remember { mutableStateOf("") }
    var fechaDevolucion by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var prestamos by rememberSaveable { mutableStateOf(listOf<Prestamo>()) }
    var selectedPrestamo by remember { mutableStateOf<Prestamo?>(null) }
    val scrollState = rememberScrollState()

    var selectedLibro by remember { mutableStateOf<Libro?>(null) }
    var selectedMiembro by remember { mutableStateOf<Miembro?>(null) }
    var libroExpanded by remember { mutableStateOf(false) }
    var miembroExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = libroExpanded,
            onExpandedChange = { libroExpanded = !libroExpanded }
        ) {
            TextField(
                value = selectedLibro?.titulo ?: "", // Mostrar el nombre del libro seleccionado
                onValueChange = {},
                label = { Text("Selecciona el libro") },
                readOnly = true, // Hace que no se pueda escribir manualmente
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (libroExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = libroExpanded,
                onDismissRequest = { libroExpanded = false }
            ) {
                libros.forEach { libro ->
                    DropdownMenuItem(onClick = {
                        selectedLibro = libro
                        libroExpanded = false
                    },
                        text = {Text(text = libro.titulo) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        ExposedDropdownMenuBox(
            expanded = miembroExpanded,
            onExpandedChange = { miembroExpanded = !miembroExpanded }
        ) {
            TextField(
                value = selectedMiembro?.nombre ?: "",
                onValueChange = {},
                label = { Text("Selecciona el miembro") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (miembroExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = miembroExpanded,
                onDismissRequest = { miembroExpanded = false }
            ) {
                miembros.forEach { miembro ->
                    DropdownMenuItem(onClick = {
                        selectedMiembro = miembro
                        miembroExpanded = false
                    },
                        text = {Text(text = miembro.nombre)}
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaPrestamo,
            onValueChange = { fechaPrestamo = it },
            label = { Text(text = "Fecha de Préstamo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaDevolucion,
            onValueChange = { fechaDevolucion = it },
            label = { Text(text = "Fecha de Devolución (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val isFormValid = libroId.isNotBlank() && miembroId.isNotBlank() && fechaPrestamo.isNotBlank()

        Button(onClick = {
            if (isFormValid) {
                if (isEditing && selectedPrestamo != null) {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.actualizarPrestamo(
                                prestamoId = selectedPrestamo!!.prestamo_id,
                                libroId = libroId.toInt(),
                                miembroId = miembroId.toInt(),
                                fechaPrestamo = fechaPrestamo,
                                fechaDevolucion = fechaDevolucion
                            )
                            prestamos = bibliotecaRepository.obtenerPrestamos()
                        }
                        Toast.makeText(context, "Cambios Guardados", Toast.LENGTH_LONG).show()
                        libroId = ""
                        miembroId = ""
                        fechaPrestamo = ""
                        fechaDevolucion = ""
                        selectedPrestamo = null
                        isEditing = false
                    }
                } else {
                    val prestamo = Prestamo(
                        fecha_prestamo = fechaPrestamo,
                        fecha_devolucion = fechaDevolucion,
                        libro_id = libroId.toInt(),
                        miembro_id = miembroId.toInt()
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            bibliotecaRepository.insertarPrestamo(prestamo)
                        }
                        Toast.makeText(context, "Préstamo Registrado", Toast.LENGTH_LONG).show()
                        libroId = ""
                        miembroId = ""
                        fechaPrestamo = ""
                        fechaDevolucion = ""
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
                contentDescription = if (isEditing) "Guardar Cambios" else "Registrar Préstamo"
            )
            Text(text = if (isEditing) "Guardar Cambios" else "Registrar Préstamo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                prestamos = withContext(Dispatchers.IO) {
                    bibliotecaRepository.obtenerPrestamos()
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
                contentDescription = "Listar Préstamos"
            )
            Text(text = "Listar Préstamos")
        }
        Spacer(modifier = Modifier.height(16.dp))

        var showDialog by remember { mutableStateOf(false) }
        var prestamoToDelete by remember { mutableStateOf<Prestamo?>(null) }

        Column {
            prestamos.forEach { prestamo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Libro ID: ${prestamo.libro_id}, Miembro ID: ${prestamo.miembro_id}, Fecha Préstamo: ${prestamo.fecha_prestamo}")

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        selectedPrestamo = prestamo
                        libroId = prestamo.libro_id.toString()
                        miembroId = prestamo.miembro_id.toString()
                        fechaPrestamo = prestamo.fecha_prestamo
                        fechaDevolucion = prestamo.fecha_devolucion ?: ""
                        isEditing = true
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar este préstamo",
                            tint = Color.Blue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        showDialog = true
                        prestamoToDelete = prestamo
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar préstamo",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            ConfirmationDialog(
                showDialog = showDialog,
                itemToDelete = prestamoToDelete,
                onConfirm = { item ->
                    val prestamo = item as Prestamo
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                bibliotecaRepository.eliminarPrestamoPorId(prestamo.prestamo_id)
                                prestamos = bibliotecaRepository.obtenerPrestamos()
                            }
                            Toast.makeText(context, "Préstamo eliminado", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar el préstamo", Toast.LENGTH_LONG).show()
                        }
                        showDialog = false
                        prestamoToDelete = null
                    }
                },
                onDismiss = {
                    showDialog = false
                    prestamoToDelete = null
                }
            )
        }
    }
}
