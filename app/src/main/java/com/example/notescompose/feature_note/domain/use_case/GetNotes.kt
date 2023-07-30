package com.example.notescompose.feature_note.domain.use_case

import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.repository.NoteRepository
import com.example.notescompose.feature_note.domain.util.NotesOrder
import com.example.notescompose.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(private val repository: NoteRepository) {
    operator fun invoke(
        notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending)): Flow<List<Note>>{
        return repository.getNotes().map { notes ->
            when(notesOrder.orderType) {
                is OrderType.Ascending -> {
                    when(notesOrder) {
                        is NotesOrder.Title -> notes.sortedBy {it.title.lowercase()}
                        is NotesOrder.Date -> notes.sortedBy { it.timestamp}
                        is NotesOrder.Color -> notes.sortedBy {it.color}
                    }
                }
                is OrderType.Descending -> {
                    when(notesOrder) {
                        is NotesOrder.Title -> notes.sortedByDescending {it.title.lowercase()}
                        is NotesOrder.Date -> notes.sortedByDescending { it.timestamp}
                        is NotesOrder.Color -> notes.sortedByDescending {it.color}
                    }
                }
            }
        }
    }
}