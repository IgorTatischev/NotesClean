package com.example.notescompose.feature_note.presentation.screens.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notescompose.R
import com.example.notescompose.feature_note.domain.model.Note
import com.example.notescompose.feature_note.presentation.components.HintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditViewModel = hiltViewModel()
) {

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val noteBackgroundAnimatable = remember {
        Animatable(Color(if (noteColor != -1) noteColor else viewModel.noteColor.value))
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = context.resources.getString(R.string.save_error))
                }

                is AddEditViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditEvent.SaveNote)
            }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(noteBackgroundAnimatable.value)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()
                        ColorItem(
                            color = color,
                            onClick = {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(AddEditEvent.ChangeColor(colorInt))
                            },
                            borderColor = viewModel.noteColor.value == colorInt
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HintTextField(
                    text = titleState.text,
                    hint = stringResource(id = R.string.title_hint),
                    isHintVisible = titleState.isHintVisible,
                    onValueChange = {
                        viewModel.onEvent(AddEditEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditEvent.ChangeTitleFocus(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(16.dp))
                HintTextField(
                    text = contentState.text,
                    hint = stringResource(id = R.string.content_hint),
                    isHintVisible = contentState.isHintVisible,
                    onValueChange = {
                        viewModel.onEvent(AddEditEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditEvent.ChangeContentFocus(it))
                    },
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ColorItem(color: Color, onClick: () -> Unit, borderColor: Boolean){
    Box(modifier = Modifier
        .size(50.dp)
        .shadow(15.dp, CircleShape)
        .clip(CircleShape)
        .background(color)
        .border(
            width = 3.dp,
            color = if (borderColor) Color.DarkGray else Color.Transparent,
            shape = CircleShape
        )
        .clickable { onClick() }
    )
}