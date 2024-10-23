package com.project.dictionary.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.dictionary.R
import com.project.dictionary.ui.theme.blueThemeColors
import com.project.dictionary.utils.noRippleClickable

@Composable
fun Toolbar(onSettings: () -> Unit) {
    Row(
        modifier = Modifier
            .background(blueThemeColors[0])
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.app_name), color = Color.White,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center
            ),
        )
        Image(
            modifier = Modifier
                .size(30.dp)
                .noRippleClickable {
                    onSettings()
                },
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = ""
        )
    }
}