package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: HealthRecord)

    @Query("SELECT * FROM health_records")
    fun getAllHealthRecords(): Flow<List<HealthRecord>>

    @Query("SELECT * FROM health_records WHERE id = :id")
    fun getHealthRecordById(id: Int): Flow<HealthRecord?>

    @Query("SELECT * FROM health_records LIMIT 1")
    fun getHealthRecord(): Flow<HealthRecord?>

    @Update
    suspend fun updateHealthRecord(record: HealthRecord)

    @Delete
    suspend fun deleteHealthRecord(record: HealthRecord)
}

@Dao
interface JournalEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry)

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getJournalEntry(id: Int): Flow<JournalEntry?>

    @Update
    suspend fun updateJournalEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteJournalEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE date < :olderThanTimestamp")
    suspend fun deleteOldEntries(olderThanTimestamp: Long)
}

@Dao
interface WellnessMetricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: WellnessMetric)

    @Query("SELECT * FROM wellness_metrics ORDER BY date DESC")
    fun getAllMetrics(): Flow<List<WellnessMetric>>

    @Query("SELECT * FROM wellness_metrics WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getMetricsInDateRange(startDate: Long, endDate: Long): Flow<List<WellnessMetric>>

    @Update
    suspend fun updateMetric(metric: WellnessMetric)

    @Delete
    suspend fun deleteMetric(metric: WellnessMetric)
}

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments ORDER BY dateTime ASC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE dateTime >= :now ORDER BY dateTime ASC")
    fun getUpcomingAppointments(now: Long): Flow<List<Appointment>>

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)
}
