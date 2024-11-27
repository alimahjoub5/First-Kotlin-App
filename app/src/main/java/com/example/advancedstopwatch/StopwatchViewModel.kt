package com.example.advancedstopwatch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class StopwatchViewModel(application: Application) : AndroidViewModel(application) {
    private val database = StopwatchDatabase.getInstance(application)
    private val stopwatchDao = database.stopwatchDao()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _laps = MutableStateFlow<List<Long>>(emptyList())
    val laps: StateFlow<List<Long>> = _laps

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private var startTime: Long = 0
    private var timer: Timer? = null
    private var currentSessionId: Long? = null

    val allSessions = stopwatchDao.getAllSessions()

    fun toggleStopwatch() {
        if (_isRunning.value) {
            stopStopwatch()
        } else {
            startStopwatch()
        }
    }

    private fun startStopwatch() {
        _isRunning.value = true
        startTime = System.currentTimeMillis() - _elapsedTime.value
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                _elapsedTime.value = System.currentTimeMillis() - startTime
            }
        }, 0, 10)

        viewModelScope.launch {
            currentSessionId = stopwatchDao.insertSession(
                StopwatchSession(
                    startTime = System.currentTimeMillis(),
                    endTime = 0,
                    totalTime = 0
                )
            )
        }
    }

    private fun stopStopwatch() {
        _isRunning.value = false
        timer?.cancel()

        viewModelScope.launch {
            currentSessionId?.let { sessionId ->
                stopwatchDao.insertSession(
                    StopwatchSession(
                        id = sessionId.toInt(),
                        startTime = startTime,
                        endTime = System.currentTimeMillis(),
                        totalTime = _elapsedTime.value
                    )
                )
            }
        }
    }

    fun resetStopwatch() {
        stopStopwatch()
        _elapsedTime.value = 0
        _laps.value = emptyList()
        currentSessionId = null
    }

    fun lapTime() {
        val currentLaps = _laps.value.toMutableList()
        currentLaps.add(_elapsedTime.value)
        _laps.value = currentLaps

        viewModelScope.launch {
            currentSessionId?.let { sessionId ->
                stopwatchDao.insertLap(
                    Lap(
                        sessionId = sessionId.toInt(),
                        lapTime = _elapsedTime.value
                    )
                )
            }
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun getLapsForSession(sessionId: Int) = stopwatchDao.getLapsForSession(sessionId)
}

