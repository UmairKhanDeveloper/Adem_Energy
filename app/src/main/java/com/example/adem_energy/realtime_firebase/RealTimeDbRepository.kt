package com.example.adem_energy.realtime_firebase

import android.content.Context
import android.provider.Settings
import com.example.adem_energy.firebase.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealTimeDbRepository(
    private val db: DatabaseReference,
    private val context: Context
) : RealTimeRepository {

    private fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: "unknown_user"
    }

    override fun insert(item: RealTimeUser.RealTimeItems): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val userId = getUserId()
            val userNode = db.child("users").child(userId).child("items")
            userNode.push().setValue(item)
                .addOnSuccessListener { trySend(ResultState.Success("Inserted")) }
                .addOnFailureListener { trySend(ResultState.Error(it)) }
            awaitClose { /* nothing to clean */ }
        }

    override fun getItems(): Flow<ResultState<List<RealTimeUser>>> = callbackFlow {
        trySend(ResultState.Loading)

        val userId = getUserId()
        val userNode = db.child("users").child(userId).child("items")

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<RealTimeUser>()
                if (!snapshot.exists()) {
                    trySend(ResultState.Success(emptyList()))
                    return
                }

                snapshot.children.forEach { dataSnapshot ->
                    val item = dataSnapshot.getValue(RealTimeUser.RealTimeItems::class.java)
                    val key = dataSnapshot.key
                    if (item != null && key != null) {
                        itemList.add(RealTimeUser(item, key))
                    }
                }
                trySend(ResultState.Success(itemList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(Exception(error.message)))
            }
        }

        userNode.addValueEventListener(valueEvent)
        awaitClose { userNode.removeEventListener(valueEvent) }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val userId = getUserId()
        val userNode = db.child("users").child(userId).child("items")

        userNode.child(key).removeValue()
            .addOnSuccessListener { trySend(ResultState.Success("Deleted")) }
            .addOnFailureListener { trySend(ResultState.Error(it)) }

        awaitClose { /* nothing */ }
    }

    override fun update(res: RealTimeUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val userId = getUserId()
        val userNode = db.child("users").child(userId).child("items")

        val map = hashMapOf<String, Any>(
            "userFirstName" to res.items.userFirstName,
            "email" to res.items.email,
            "password" to res.items.password,
            "remedyName" to res.items.remedyName,
            "remedyInfo" to res.items.remedyInfo,
            "potency" to res.items.potency,
            "scale" to res.items.scale,
            "units" to res.items.units,
            "timer" to res.items.timer,
            "color" to res.items.color
        )

        val key = res.key
        if (key == null) {
            trySend(ResultState.Error(Exception("Missing key for update")))
            awaitClose { }
            return@callbackFlow
        }

        userNode.child(key).updateChildren(map)
            .addOnSuccessListener { trySend(ResultState.Success("Updated")) }
            .addOnFailureListener { trySend(ResultState.Error(it)) }

        awaitClose { /* nothing */ }
    }
}