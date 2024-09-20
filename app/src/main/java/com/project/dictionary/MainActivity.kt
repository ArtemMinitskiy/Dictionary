package com.project.dictionary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.dictionary.model.Word
import com.project.dictionary.ui.theme.DictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val word = remember { mutableStateOf(Word("", "", "")) }
            val scrollIndex = remember { mutableIntStateOf(0) }
            val navController = rememberNavController()

            DictionaryTheme {
                Image(modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds, painter = painterResource(id = R.drawable.background), contentDescription = "")

                NavHost(navController = navController,
                    startDestination = NavigationItem.List.route,
                    exitTransition = {
                        ExitTransition.None
                    },
                    popExitTransition = {
                        ExitTransition.None
                    }
                ) {
                    composable(NavigationItem.List.route) {
                        WordsScreen(viewModel, scrollIndex) { item, index ->
                            word.value = item
                            scrollIndex.intValue = index
                            Log.i("mLogFirebase", "$index ${item.wordName}")
                            navController.navigate(NavigationItem.Definition.route)
                        }
                    }
                    composable(NavigationItem.Definition.route) { backStackEntry ->
                        DefinitionScreen(word) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}