package com.sokhibdzhon.readback.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class StartViewModel @Inject constructor(private val gameRepoImpl: GameRepoImpl) : ViewModel() {


    private val _level = MutableLiveData(1)
    val level: LiveData<Int>
        get() = _level

    init {
        viewModelScope.launch {
            Timber.d("StartViewModel: Init Called ")
            getLevelFromRepo()
        }

    }

    fun getLevel() = level.value

    fun getLevelFromRepo() {
        _level.value = gameRepoImpl.level
    }
}
