package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repository.FavoriteEventRepository

class FavoriteViewModel(private val repository: FavoriteEventRepository) : ViewModel() {
    fun getAllFavoriteEvents() = repository.getAllFavoriteEvents()
}
