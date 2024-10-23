package com.project.dictionary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.dictionary.ui.views.ColorPicker

@Composable
fun SettingsScreen(wordItemColor: MutableState<String>, colorPick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        ColorPicker(wordItemColor, colorPick = {
            colorPick(it)
        })
    }
}