package com.example.adem_energy.realtime_firebase

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adem_energy.firebase.ResultState
import kotlinx.coroutines.launch

data class ItemsState(
    val item: List<RealTimeUser> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

class RealTimeViewModel(private val repository: RealTimeRepository) : ViewModel() {

    private val _res: MutableState<ItemsState> = mutableStateOf(ItemsState())
    val res: State<ItemsState> = _res

    // call repository and collect result to actually execute the flow
    fun insert(items: RealTimeUser.RealTimeItems) {
        viewModelScope.launch {
            repository.insert(items).collect { result ->
                when (result) {
                    is ResultState.Loading -> _res.value = _res.value.copy(isLoading = true)
                    is ResultState.Success -> _res.value = _res.value.copy(isLoading = false)
                    is ResultState.Error -> _res.value = _res.value.copy(isLoading = false, error = result.exception.message ?: "Insert error")
                }
            }
        }
    }

    fun getItems() {
        viewModelScope.launch {
            repository.getItems().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _res.value = ItemsState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _res.value = ItemsState(item = result.response)
                    }
                    is ResultState.Error -> {
                        _res.value = ItemsState(error = result.exception.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun delete(key: String) {
        viewModelScope.launch {
            repository.delete(key).collect { result ->
                when (result) {
                    is ResultState.Loading -> _res.value = _res.value.copy(isLoading = true)
                    is ResultState.Success -> _res.value = _res.value.copy(isLoading = false)
                    is ResultState.Error -> _res.value = _res.value.copy(isLoading = false, error = result.exception.message ?: "Delete error")
                }
            }
        }
    }

    fun update(item: RealTimeUser) {
        viewModelScope.launch {
            repository.update(item).collect { result ->
                when (result) {
                    is ResultState.Loading -> _res.value = _res.value.copy(isLoading = true)
                    is ResultState.Success -> _res.value = _res.value.copy(isLoading = false)
                    is ResultState.Error -> _res.value = _res.value.copy(isLoading = false, error = result.exception.message ?: "Update error")
                }
            }
        }
    }
}