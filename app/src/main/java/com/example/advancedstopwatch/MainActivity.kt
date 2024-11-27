package com.example.advancedstopwatch

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.advancedstopwatch.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: StopwatchViewModel

    private val speechRecognitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let { SpeechRecognitionHelper.handleSpeechResult(it, viewModel) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createNotificationChannel(this)

        setContent {
            AdvancedStopwatchApp(speechRecognitionLauncher)
        }
    }
}

@Composable
fun AdvancedStopwatchApp(speechRecognitionLauncher: ActivityResultLauncher<Intent>) {
    val navController = rememberNavController()
    val viewModel: StopwatchViewModel = viewModel()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    AppTheme(darkTheme = isDarkTheme) {
        NavHost(navController = navController, startDestination = "stopwatch") {
            composable("stopwatch") {
                StopwatchScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onSpeechRecognition = { SpeechRecognitionHelper.startSpeechRecognition(speechRecognitionLauncher) }
                )
            }
            composable("history") {
                HistoryScreen(viewModel = viewModel, navController = navController)
            }
            composable("settings") {
                SettingsScreen(viewModel = viewModel, navController = navController)
            }
        }
    }
}
