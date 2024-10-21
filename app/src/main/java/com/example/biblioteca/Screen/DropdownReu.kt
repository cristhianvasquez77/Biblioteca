package com.example.biblioteca.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ReusableDropdownMenu(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    label: String,
    itemToString: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    TextField(
        value = selectedItem?.let { itemToString(it) } ?: searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = true },
        readOnly = true
    )


    val filteredItems = items.filter { item ->
        itemToString(item).contains(searchQuery, ignoreCase = true)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        filteredItems.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onItemSelected(item)
                    searchQuery = itemToString(item)
                    expanded = false
                },
                text = { Text(text = itemToString(item)) }
            )
        }
    }
}