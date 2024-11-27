package com.example.advancedstopwatch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: StopwatchViewModel, navController: NavController) {
    val sessions by viewModel.allSessions.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Stopwatch History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(sessions) { session ->
                SessionItem(session, viewModel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun SessionItem(session: StopwatchSession, viewModel: StopwatchViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val laps by viewModel.getLapsForSession(session.id).collectAsState(initial = emptyList())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Session ${session.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Date: ${(session.startTime)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Total Time: ${(session.totalTime)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (expanded) "Hide Laps" else "Show Laps")
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                laps.forEachIndexed { index, lap ->
                    Text(
                        text = "Lap ${index + 1}: ${(lap.lapTime)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


