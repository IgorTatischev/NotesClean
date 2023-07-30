package com.example.notescompose.feature_note.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notescompose.feature_note.domain.util.NotesOrder
import com.example.notescompose.feature_note.domain.util.OrderType


@Composable
fun OrderSection(
    modifier: Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending),
    onOrderChange: (NotesOrder) -> Unit
    ) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(text = "Title", selected = notesOrder is NotesOrder.Title,
                onSelect = { onOrderChange(NotesOrder.Title(notesOrder.orderType))
                })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Date", selected = notesOrder is NotesOrder.Date,
                onSelect = { onOrderChange(NotesOrder.Date(notesOrder.orderType))
                })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Color", selected = notesOrder is NotesOrder.Color,
                onSelect = { onOrderChange(NotesOrder.Color(notesOrder.orderType))
                })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(text = "Ascending",
                selected = notesOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(notesOrder.copy(OrderType.Ascending))
                })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Descending",
                selected = notesOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(notesOrder.copy(OrderType.Descending))
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderSectionPreview(){
    OrderSection(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp), onOrderChange = { } )
}
