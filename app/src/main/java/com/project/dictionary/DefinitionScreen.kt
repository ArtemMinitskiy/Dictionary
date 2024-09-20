package com.project.dictionary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.dictionary.model.Word
import com.project.dictionary.ui.theme.White

@Composable
fun DefinitionScreen(word: MutableState<Word>, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 8.dp, end = 8.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.TopStart),
            text = word.value.wordName + " - " + word.value.wordDescription.replaceFirstChar { it.titlecase() },
            style = TextStyle(fontSize = 16.sp),
            color = White
        )
    }

    BackHandler {
        onBack()
    }
}