package com.example.notescompose.feature_note.presentation.screens.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notescompose.R
import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.presentation.components.NoteItem
import com.example.notescompose.feature_note.presentation.components.OrderSection
import com.example.notescompose.feature_note.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NotesListScreen(
    navController: NavController,
    viewModel: ListNotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ListNotesViewModel.UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.message_delete),
                        actionLabel = context.resources.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)

                }, backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.NoteAdd, contentDescription = "Add note")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.list),
                        style = MaterialTheme.typography.h5
                    )
                    IconButton(onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                        notesOrder = state.notesOrder,
                        onOrderChange = {
                            viewModel.onEvent(NotesEvent.Order(it))
                        })
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    state.notes.forEach { note ->
                        boxItem(note,
                            onClick = {
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route
                                            + "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                            onDeleteClick = {
                                viewModel.onEvent(NotesEvent.DeleteNote(note))
                            })
                    }
                }
            }
        }
    }
}

fun LazyListScope.boxItem(note: Note, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    item {
        NoteItem(note = note,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            onDeleteClick = { onDeleteClick() })
        Spacer(modifier = Modifier.height(16.dp))
    }
}