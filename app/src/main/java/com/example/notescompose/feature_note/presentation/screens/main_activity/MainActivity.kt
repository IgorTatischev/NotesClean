package com.example.notescompose.feature_note.presentation.screens.main_activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.notescompose.feature_note.presentation.navigation.NavGraph
import com.example.notescompose.feature_note.presentation.ui.theme.NotesComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = remember { mutableStateOf(false) }
            NotesComposeTheme(darkTheme = darkTheme.value){
                Scaffold(
                    topBar = { TopBar(darkTheme = darkTheme) },
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        NavGraph()
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(darkTheme: MutableState<Boolean>) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        var iconTheme by remember { mutableStateOf(IconThemeState()) }
        IconButton(onClick = {
            darkTheme.value = !darkTheme.value
            iconTheme = if (darkTheme.value)
                IconThemeState(Icons.Default.DarkMode, "DarkMode")
            else
                IconThemeState()
        }) {
            Icon(iconTheme.icon, iconTheme.description, tint = MaterialTheme.colors.surface)
        }
    }
}

data class IconThemeState(
    val icon: ImageVector = Icons.Default.LightMode,
    val description: String = "LightMode"
)
