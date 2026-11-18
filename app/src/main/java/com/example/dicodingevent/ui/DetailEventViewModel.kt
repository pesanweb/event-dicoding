package com.example.dicodingevent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.room.FavoriteEvent
import com.example.dicodingevent.data.repository.FavoriteEventRepository
import kotlinx.coroutines.launch

class DetailEventViewModel(private val repository: FavoriteEventRepository) : ViewModel() {
    fun getFavoriteEventById(id: String) = repository.getFavoriteEventById(id)

    fun insert(event: FavoriteEvent) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }

    fun delete(event: FavoriteEvent) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }
}
