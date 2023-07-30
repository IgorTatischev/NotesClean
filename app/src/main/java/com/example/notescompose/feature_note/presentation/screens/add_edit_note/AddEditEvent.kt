package com.example.notescompose.feature_note.presentation.screens.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditEvent{
    data class EnteredTitle(val value: String): AddEditEvent()
    data class EnteredContent(val value: String): AddEditEvent()
    data class ChangeColor(val color: Int): AddEditEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditEvent()
    object SaveNote: AddEditEvent()
}