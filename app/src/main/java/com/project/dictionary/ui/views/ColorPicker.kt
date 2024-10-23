package com.project.dictionary.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.project.dictionary.ui.theme.TransparentPicker
import com.project.dictionary.ui.theme.blueThemeColors
import com.project.dictionary.ui.theme.settingsColors
import com.project.dictionary.utils.Constants.BLUE
import com.project.dictionary.utils.Constants.GREEN
import com.project.dictionary.utils.Constants.RED
import com.project.dictionary.utils.Constants.YELLOW
import com.project.dictionary.utils.noRippleClickable

@Composable
fun ColorPicker(wordItemColor: MutableState<String>, colorPick: (String) -> Unit) {
    val isDropOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .background(blueThemeColors[0])
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp)
                .noRippleClickable {
                    isDropOpen.value = !isDropOpen.value
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = when (wordItemColor.value) {
                    BLUE -> stringResource(id = R.string.blue_theme)
                    RED -> stringResource(id = R.string.red_theme)
                    GREEN -> stringResource(id = R.string.green_theme)
                    YELLOW -> stringResource(id = R.string.yellow_theme)
                    else -> stringResource(id = R.string.orange_theme)
                },
                color = Color.White,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                ),
            )
            Image(
                modifier = Modifier
                    .size(30.dp),
                contentScale = ContentScale.FillBounds,
                painter = if (isDropOpen.value) painterResource(id = R.drawable.baseline_arrow_drop_up_24) else painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                contentDescription = ""
            )
        }
    }
    if (isDropOpen.value) {
        Row(
            modifier = Modifier
                .background(TransparentPicker)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            settingsColors.forEach {
                ColorItem(wordItemColor, it, colorPick = { color ->
                    colorPick(color)
                })
            }
        }
    }
}