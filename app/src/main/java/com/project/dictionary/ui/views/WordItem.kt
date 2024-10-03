package com.project.dictionary.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.dictionary.ui.theme.White

@Composable
fun WordItem(word: String, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .padding(8.dp)
        .background(color, RoundedCornerShape(4.dp))
        .clickable {
            onClick()
        })
    {
        Text(modifier = Modifier.align(Alignment.Center), text = word, style = TextStyle(fontSize = 14.sp), color = White)
    }
}