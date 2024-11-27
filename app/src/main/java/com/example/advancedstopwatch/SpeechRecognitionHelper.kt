package com.example.advancedstopwatch

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.result.ActivityResultLauncher

object SpeechRecognitionHelper {
    fun startSpeechRecognition(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a command")
        }
        launcher.launch(intent)
    }

    fun handleSpeechResult(result: String, viewModel: StopwatchViewModel) {
        when (result.toLowerCase()) {
            "start" -> viewModel.toggleStopwatch()
            "stop" -> viewModel.toggleStopwatch()
            "reset" -> viewModel.resetStopwatch()
            "lap" -> viewModel.lapTime()
        }
    }
}

