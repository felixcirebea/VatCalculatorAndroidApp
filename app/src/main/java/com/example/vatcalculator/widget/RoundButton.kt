package com.example.vatcalculator.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    onClickEvent: () -> Unit,
    text: String,
    textStyle: TextStyle,
    size: Int,
    bottomPadding: Int = 5
) {
    Card(
        modifier = Modifier
            .padding(bottom = bottomPadding.dp)
            .clickable { onClickEvent.invoke() }
            .size(size.dp)
            .shadow(elevation = 5.dp, shape = CircleShape),
        shape = CircleShape,
        colors = CardDefaults.cardColors(Color(0xFFE2E2E2)),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color(0xFFCDCED7))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, style = textStyle)
        }
    }
}