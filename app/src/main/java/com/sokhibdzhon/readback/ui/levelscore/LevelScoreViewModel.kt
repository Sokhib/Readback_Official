package com.sokhibdzhon.readback.ui.levelscore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LevelScoreViewModel @Inject constructor(private val gameRepoImpl: GameRepoImpl) :
    ViewModel() {

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> get() = _isSaved

    fun updateLevel() {
        viewModelScope.launch {
            gameRepoImpl.updateLevel()
            _isSaved.value = true
        }
    }
}