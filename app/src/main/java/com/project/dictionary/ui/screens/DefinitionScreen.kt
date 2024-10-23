package com.project.dictionary.ui.screens

import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.dictionary.R
import com.project.dictionary.model.Word
import com.project.dictionary.rememberTextToSpeech
import com.project.dictionary.ui.theme.White
import com.project.dictionary.utils.noRippleClickable

@Composable
fun DefinitionScreen(word: MutableState<Word>, onBack: () -> Unit) {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf20_v90rvaig))
    val tts = rememberTextToSpeech()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (!word.value.wordImage.isNullOrEmpty() && word.value.wordImage != "null") {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .align(Alignment.Center)
                            .padding(8.dp)
                            .background(Color.Transparent, RoundedCornerShape(16.dp))
                    ) {
                        LottieAnimation(
                            modifier = Modifier.align(Alignment.Center),
                            composition = composition.value,
                            iterations = LottieConstants.IterateForever,
                        )
                    }
                },
                model = word.value.wordImage,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            modifier = Modifier.noRippleClickable {
                tts.value?.speak(
                    word.value.wordName + word.value.wordDescription, TextToSpeech.QUEUE_FLUSH, null, ""
                )
            },
            text = word.value.wordName + " - " + word.value.wordDescription.replaceFirstChar { it.titlecase() },
            style = TextStyle(fontSize = 16.sp),
            color = White
        )
    }

    BackHandler {
        onBack()
    }
}