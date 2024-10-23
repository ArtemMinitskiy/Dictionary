package com.project.dictionary.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.dictionary.MainViewModel
import com.project.dictionary.R
import com.project.dictionary.model.Word
import com.project.dictionary.ui.theme.settingsColors
import com.project.dictionary.ui.views.WordItem
import com.project.dictionary.utils.LoadingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun WordsScreen(
    viewModel: MainViewModel,
    scrollIndex: MutableState<Int>,
    wordItemColor: MutableState<String>,
    onClick: (Word, Int) -> Unit
) {
    val listOfWords = remember { mutableStateOf<ArrayList<Word>>(arrayListOf()) }
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf20_v90rvaig))
    val coroutine = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    val loadingState = viewModel.loadingState.collectAsState()

    when (loadingState.value) {
        is LoadingState.Idle -> {

        }

        is LoadingState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
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
            }
        }

        is LoadingState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = scrollState
            ) {
                itemsIndexed(items = listOfWords.value, itemContent = { index, item ->
                    when (index % 4) {
                        0 -> settingsColors[wordItemColor.value]?.let { WordItem(item.wordName, it[0]) { onClick(item, index) } }
                        1 -> settingsColors[wordItemColor.value]?.let { WordItem(item.wordName, it[1]) { onClick(item, index) } }
                        2 -> settingsColors[wordItemColor.value]?.let { WordItem(item.wordName, it[2]) { onClick(item, index) } }
                        else -> settingsColors[wordItemColor.value]?.let { WordItem(item.wordName, it[3]) { onClick(item, index) } }
                    }
                })
            }
        }

        is LoadingState.Error -> {
            Toast.makeText(context, "Loading Failed", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.getListOfWords().collectLatest {
            when {
                it.isSuccess -> {
                    coroutine.launch {
                        listOfWords.value = it.getOrNull() as ArrayList<Word>
                        listOfWords.value.sortBy { word -> word.wordName }
                    }.invokeOnCompletion {
                        viewModel.success("Success")
                    }
                }

                it.isFailure -> {
                    viewModel.error("Failure ${it.exceptionOrNull()?.printStackTrace()}")
                }
            }
        }
    }

    LaunchedEffect(scrollIndex.value) {
        if (loadingState.value == LoadingState.Loading) scrollState.animateScrollToItem(scrollIndex.value)
    }
}
