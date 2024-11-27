package com.example.advancedstopwatch

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class StopwatchSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Long,
    val endTime: Long,
    val totalTime: Long
)

@Entity
data class Lap(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val lapTime: Long
)

@Dao
interface StopwatchDao {
    @Insert
    suspend fun insertSession(session: StopwatchSession): Long

    @Insert
    suspend fun insertLap(lap: Lap)

    @Query("SELECT * FROM StopwatchSession ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<StopwatchSession>>

    @Query("SELECT * FROM Lap WHERE sessionId = :sessionId ORDER BY id")
    fun getLapsForSession(sessionId: Int): Flow<List<Lap>>
}

@Database(entities = [StopwatchSession::class, Lap::class], version = 1)
abstract class StopwatchDatabase : RoomDatabase() {
    abstract fun stopwatchDao(): StopwatchDao

    companion object {
        private var instance: StopwatchDatabase? = null

        fun getInstance(context: android.content.Context): StopwatchDatabase {
            return instance ?: synchronized(this) {
                instance ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    StopwatchDatabase::class.java,
                    "stopwatch_database"
                ).build().also { instance = it }
            }
        }
    }
}

