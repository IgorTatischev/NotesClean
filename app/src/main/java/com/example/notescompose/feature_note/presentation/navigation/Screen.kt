package com.example.notescompose.feature_note.presentation.navigation

sealed class Screen(val route: String) {
    object NotesListScreen: Screen("notes_list_screen")
    object AddEditNoteScreen: Screen("add_edit_screen")
}