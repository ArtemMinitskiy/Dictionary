package com.project.dictionary.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.dictionary.utils.noRippleClickable

@Composable
fun ColorItem(
    wordItemColor: MutableState<String>,
    colorPickList: Map.Entry<String, List<Color>>,
    colorPick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
    ) {
        colorPickList.value.forEach {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (wordItemColor.value == colorPickList.key) Color.White else it,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(40.dp)
                    .background(it, RoundedCornerShape(12.dp))
                    .noRippleClickable {
                        colorPick(colorPickList.key)
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}