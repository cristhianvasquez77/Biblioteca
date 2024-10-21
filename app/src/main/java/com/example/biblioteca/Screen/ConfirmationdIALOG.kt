package com.example.biblioteca.Screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    itemToDelete: Any?,
    onConfirm: (Any) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Confirmar Eliminación")
            },
            text = {
                Text(text = "¿Estás seguro de eliminar?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(itemToDelete)
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}