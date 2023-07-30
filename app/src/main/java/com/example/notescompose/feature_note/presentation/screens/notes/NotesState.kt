package com.example.notescompose.feature_note.presentation.screens.notes

import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.util.NotesOrder
import com.example.notescompose.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
