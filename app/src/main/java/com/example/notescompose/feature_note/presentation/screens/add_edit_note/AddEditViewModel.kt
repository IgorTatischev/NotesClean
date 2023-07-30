package com.example.notescompose.feature_note.presentation.screens.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notescompose.feature_note.domain.model.InvalidNote
import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1){
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also {
                        currentNoteId = it.id
                        _noteTitle.value = noteTitle.value.copy(text = it.title, isHintVisible = false)
                        _noteContent.value = noteContent.value.copy(text = it.content, isHintVisible = false)
                        _noteColor.value = it.color
                    }
                }
            }
        }
    }
    private var currentNoteId: Int? = null

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Content..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddEditEvent){
        when(event){
            is AddEditEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }
            is AddEditEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }
            is AddEditEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank())
            }
            is AddEditEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank())
            }
            is AddEditEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            timestamp = System.currentTimeMillis(),
                            color = noteColor.value,
                            id = currentNoteId
                        ))
                        _eventFlow.emit(UiEvent.SaveNote)
                    }
                    catch (e: InvalidNote){
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        object ShowSnackbar: UiEvent()
        object SaveNote: UiEvent()
    }
}