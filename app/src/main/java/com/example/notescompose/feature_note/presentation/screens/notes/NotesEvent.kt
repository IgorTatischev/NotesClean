package com.example.notescompose.feature_note.presentation.screens.notes

import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.util.NotesOrder

sealed class NotesEvent {
    data class Order(val notesOrder: NotesOrder): NotesEvent()
    data class DeleteNote(val note : Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
