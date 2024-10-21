package com.example.biblioteca.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biblioteca.Repository.BibliotecaRepository

@Composable
fun MainScreen(navController: NavController, bibliotecaRepository: BibliotecaRepository) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("libroScreen") }) {
            Text("Gestión de Libros")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("autorScreen") }) {
            Text("Gestión de Autores")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("miembroScreen") }) {
            Text("Gestión de Miembros")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("prestamoScreen") }) {
            Text("Gestión de Préstamos")
        }
    }
}
