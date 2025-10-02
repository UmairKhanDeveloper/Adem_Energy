package com.example.adem_energy.realtime_firebase

import com.example.adem_energy.firebase.ResultState
import kotlinx.coroutines.flow.Flow


interface RealTimeRepository {
    fun insert(item: RealTimeUser.RealTimeItems): Flow<ResultState<String>>
    fun getItems(): Flow<ResultState<List<RealTimeUser>>>
    fun delete(key: String): Flow<ResultState<String>>
    fun update(res: RealTimeUser): Flow<ResultState<String>>
}