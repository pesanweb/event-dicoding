package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingEventViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        getUpcomingEvents()
    }

    fun getUpcomingEvents() {
        if (_listEvents.value != null && _listEvents.value!!.isNotEmpty()) {
            return
        }
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getEvents(active = 1)
                }
                _isLoading.value = false
                if (!response.error) {
                    _listEvents.value = response.listEvents
                    if (response.listEvents.isEmpty()) {
                        _errorMessage.value = "Tidak ada event mendatang saat ini"
                    }
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Gagal memuat data: ${e.localizedMessage}"
            }
        }
    }
}
