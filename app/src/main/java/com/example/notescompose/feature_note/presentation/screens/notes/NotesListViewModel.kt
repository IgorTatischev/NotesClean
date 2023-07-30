package com.example.notescompose.feature_note.presentation.screens.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.domain.use_case.NoteUseCases
import com.example.notescompose.feature_note.domain.util.NotesOrder
import com.example.notescompose.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNotesViewModel @Inject constructor(private val noteUseCases: NoteUseCases): ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var lastDeletedNote: Note? = null

    private var getNotesJob : Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init{
        getNotes(NotesOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order -> {
                if (state.value.notesOrder::class == event.notesOrder::class &&
                    state.value.notesOrder.orderType == event.notesOrder.orderType){
                    return
                }
                getNotes(event.notesOrder)
            }
            is NotesEvent.DeleteNote ->{
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    lastDeletedNote = event.note
                    _eventFlow.emit(UiEvent.ShowSnackbar)
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(lastDeletedNote ?: return@launch)
                    lastDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }
        }
    }

    private fun getNotes(noteOrder: NotesOrder){
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder).onEach { notes ->
            _state.value = state.value.copy(notes = notes, notesOrder = noteOrder)
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent{
        object ShowSnackbar: UiEvent()
    }
}