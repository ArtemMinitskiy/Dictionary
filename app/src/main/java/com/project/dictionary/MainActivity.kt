package com.project.dictionary

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.project.dictionary.notifications.AlarmSchedulerImpl
import com.project.dictionary.notifications.NavigationItem
import com.project.dictionary.ui.screens.DefinitionScreen
import com.project.dictionary.ui.screens.SettingsScreen
import com.project.dictionary.ui.screens.WordsScreen
import com.project.dictionary.ui.theme.DictionaryTheme
import com.project.dictionary.ui.views.Toolbar
import com.project.dictionary.utils.Constants.NOTIFICATION_WORD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var tempIntentWord: Word? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alarmScheduler = AlarmSchedulerImpl(this@MainActivity)

        if (intent.hasExtra(NOTIFICATION_WORD)) {
            tempIntentWord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NOTIFICATION_WORD, Word::class.java)
            } else {
                intent.getParcelableExtra(NOTIFICATION_WORD)
            }
        }

//        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val word = remember { mutableStateOf(Word()) }
            val scrollIndex = remember { mutableIntStateOf(0) }
            val navController = rememberNavController()
            val wordItemColor = remember { mutableStateOf("") }

            DictionaryTheme {
                viewModel.colorSettingsFlow.collectAsState().let { color ->
                    if (!color.value.isNullOrEmpty()) {
                        wordItemColor.value = color.value
                        Log.i("mLogFirebase", "color ${color.value}")
                        alarmScheduler.schedule(wordItemColor.value)
                    }
                }

                LaunchedEffect(word.value) {
                    tempIntentWord?.let {
                        word.value = it
                    }
                }
//                LaunchedEffect(Unit) {
//                    alarmScheduler?.schedule(wordItemColor.value)

                    //Not working on Xiaomi
//                    viewModel.scheduleReminderNotification()
//                }

                Image(modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds, painter = painterResource(id = R.drawable.background), contentDescription = "")

                Column(modifier = Modifier.fillMaxSize()) {
                    Toolbar(onSettings = {
                        navController.navigate(NavigationItem.Settings.route)
                    })
                    NavHost(navController = navController,
                        startDestination = if (intent.hasExtra(NOTIFICATION_WORD)) NavigationItem.Definition.route else NavigationItem.List.route,
                        exitTransition = {
                            ExitTransition.None
                        },
                        popExitTransition = {
                            ExitTransition.None
                        }
                    ) {
                        composable(NavigationItem.List.route) {
                            WordsScreen(viewModel, scrollIndex, wordItemColor) { item, index ->
                                word.value = item
                                scrollIndex.intValue = index
                                navController.navigate(NavigationItem.Definition.route)
                            }
                        }
                        composable(NavigationItem.Definition.route) {
                            DefinitionScreen(word) {
                                if (intent.hasExtra(NOTIFICATION_WORD)) navController.navigate(NavigationItem.List.route) else navController.popBackStack()
                                intent.removeExtra(NOTIFICATION_WORD)
                                word.value = Word()
                                tempIntentWord = null
                            }
                        }
                        composable(NavigationItem.Settings.route) {
                            SettingsScreen(wordItemColor, colorPick = {
                                viewModel.updateColorSettings(it)
                            })
                        }
                    }
                }
            }
        }
    }
}