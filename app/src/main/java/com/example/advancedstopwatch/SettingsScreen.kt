package com.example.advancedstopwatch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(viewModel: StopwatchViewModel, navController: NavController) {
    var notificationInterval by remember { mutableStateOf(5) }
    var voiceCommandsEnabled by remember { mutableStateOf(true) }
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        SettingItem(
            title = "Dark Theme",
            content = {
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.toggleTheme() }
                )
            }
        )

        SettingItem(
            title = "Notification Interval (minutes)",
            content = {
                Slider(
                    value = notificationInterval.toFloat(),
                    onValueChange = { notificationInterval = it.toInt() },
                    valueRange = 1f..60f,
                    steps = 59
                )
                Text(text = "$notificationInterval min")
            }
        )

        SettingItem(
            title = "Voice Commands",
            content = {
                Switch(
                    checked = voiceCommandsEnabled,
                    onCheckedChange = { voiceCommandsEnabled = it }
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigateUp() }
        ) {
            Text("Save and Return")
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        content()
    }
}

