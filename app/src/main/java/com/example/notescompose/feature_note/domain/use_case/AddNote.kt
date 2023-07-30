package com.example.notescompose.feature_note.domain.use_case

import com.example.notescompose.feature_note.domain.model.InvalidNote
import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.repository.NoteRepository

class AddNote(private val repository: NoteRepository) {

    @Throws(InvalidNote::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNote()
        }
        if (note.content.isBlank()){
            throw InvalidNote()
        }
        else repository.insertNote(note)
    }
}