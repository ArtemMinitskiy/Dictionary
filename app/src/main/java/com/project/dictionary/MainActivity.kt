package com.project.dictionary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.dictionary.model.Word
import com.project.dictionary.ui.theme.DictionaryTheme
import com.project.dictionary.ui.theme.Purple40
import com.project.dictionary.ui.theme.PurpleGrey40
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var listOfWords: ArrayList<Word> = arrayListOf()

//        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val isLoading = remember { mutableStateOf(false) }
            val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf20_v90rvaig))

            DictionaryTheme {
                if (isLoading.value) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = listOfWords, itemContent = { item ->
                            Text(modifier = Modifier.padding(4.dp), text = item.wordName + " - " + item.wordDescription, style = TextStyle(fontSize = 14.sp), color = Purple40)
                            Spacer(modifier = Modifier.height(10.dp))
                        })
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp)
                                .background(PurpleGrey40, RoundedCornerShape(16.dp))
                        ) {
                            LottieAnimation(
                                modifier = Modifier.align(Alignment.Center),
                                composition = composition.value,
                                iterations = LottieConstants.IterateForever,
                            )
                        }
                    }
                }

            }

            LaunchedEffect(viewModel) {
                viewModel.getListOfWords().collectLatest {
                    when {
                        it.isSuccess -> {
                            it?.let {
                                listOfWords = it.getOrNull() as ArrayList<Word>
//                                listOfWords.forEach {
//                                    Log.e("mLogFirebase", "$it")
//                                }
                                isLoading.value = true
                            }
                        }

                        it.isFailure -> {
                            Log.e("mLogFirebase", "Failure")
                            it.exceptionOrNull()?.printStackTrace()
                            isLoading.value = false
                        }
                    }
                }
            }
        }
    }
}