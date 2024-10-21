package com.example.biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biblioteca.DAO.BibliotecaDAO
import com.example.biblioteca.Database.BibliotecaDatabase
import com.example.biblioteca.Repository.BibliotecaRepository
import com.example.biblioteca.Screen.AutorScreen
import com.example.biblioteca.Screen.LibroScreen
import com.example.biblioteca.Screen.MainScreen
import com.example.biblioteca.Screen.MiembroScreen
import com.example.biblioteca.Screen.PrestamoScreen
import com.example.biblioteca.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    private lateinit var bibliotecaDAO: BibliotecaDAO
    private lateinit var bibliotecaRepository: BibliotecaRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db= BibliotecaDatabase.getDatabase(applicationContext)
        bibliotecaDAO = db.bibliotecaDao()
        bibliotecaRepository = BibliotecaRepository(bibliotecaDAO)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Surface {
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") {
                            MainScreen(navController, bibliotecaRepository)
                        }
                        composable("libroScreen") {
                            LibroScreen(navController, bibliotecaRepository)
                        }
                        composable("autorScreen") {
                            AutorScreen(navController, bibliotecaRepository)
                        }
                        composable("miembroScreen") {
                            MiembroScreen(navController, bibliotecaRepository)
                        }
                        composable("prestamoScreen") {
                            PrestamoScreen(navController, bibliotecaRepository, libros = listOf(), miembros = listOf())
                        }
                    }
                }
            }
        }
    }
}
