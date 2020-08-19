package com.sokhibdzhon.readback.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(gameRepoImpl: GameRepoImpl) : ViewModel() {
    private val _levelSkips = MutableLiveData(0)
    val levelSkips: LiveData<Int>
        get() = _levelSkips

    private val _level = MutableLiveData(1)
    val level: LiveData<Int>
        get() = _level

    init {
        viewModelScope.launch {
            _levelSkips.value = gameRepoImpl.getSkips(GameType.LEVELSGAME)
            _level.value = gameRepoImpl.getLevel()
        }

    }

    fun getLevel() = level.value
}
