package com.example.notescompose.feature_note.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.notescompose.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier,
    onDeleteClick:() -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        backgroundColor = Color(ColorUtils.blendARGB(note.color, 0x000000,0.2f))
    ){
        Box(modifier = modifier.background(Color(ColorUtils.blendARGB(note.color, 0x000000,0.2f)))){
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(end = 32.dp)

            ) {
                Text(text = note.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = note.content,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteItemPreview(){
    NoteItem(
        note = Note(title = "Preview", content = "Content",id =1, color = 2, timestamp = 123132),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        onDeleteClick = { })
}
