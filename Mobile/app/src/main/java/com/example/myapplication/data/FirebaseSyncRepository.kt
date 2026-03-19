package com.example.myapplication.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log

class FirebaseSyncRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val TAG = "FirebaseSync"

    suspend fun syncHealthRecord(record: HealthRecord) {
        try {
            firestore.collection("health_records")
                .document(record.id.toString())
                .set(record)
                .await()
            Log.d(TAG, "Successfully synced record: ${record.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing record: ${record.id}", e)
        }
    }

    suspend fun deleteHealthRecord(recordId: Int) {
        try {
            firestore.collection("health_records")
                .document(recordId.toString())
                .delete()
                .await()
            Log.d(TAG, "Successfully deleted record: $recordId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting record: $recordId", e)
        }
    }

    suspend fun getAllHealthRecords(): List<HealthRecord> {
        return try {
            firestore.collection("health_records")
                .get()
                .await()
                .toObjects(HealthRecord::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching records", e)
            emptyList()
        }
    }
}
