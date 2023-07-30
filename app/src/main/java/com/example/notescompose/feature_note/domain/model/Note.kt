package com.example.notescompose.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notescompose.feature_note.presentation.ui.theme.LightBlue
import com.example.notescompose.feature_note.presentation.ui.theme.LightPink
import com.example.notescompose.feature_note.presentation.ui.theme.LightRed
import com.example.notescompose.feature_note.presentation.ui.theme.LightYellow
import com.example.notescompose.feature_note.presentation.ui.theme.YellowGreen

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
){
    companion object{
        val noteColors = listOf(LightYellow, YellowGreen, LightRed, LightBlue, LightPink)
    }
}

class InvalidNote() : Exception()