package com.sokhibdzhon.readback.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScoreViewModel @Inject constructor(private val gameRepoImpl: GameRepoImpl) :
    ViewModel() {

    private val _bestScore = MutableLiveData(0)
    val bestScore: LiveData<Int>
        get() = _bestScore

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    init {
        viewModelScope.launch {
            _bestScore.value = gameRepoImpl.bestScore
        }
    }

    fun setScore(score: Int) {
        _score.value = score
        checkBestScoreChange()
    }

    private fun checkBestScoreChange() {
        if (_bestScore.value!! < _score.value!!) {
            _bestScore.value = score.value
            gameRepoImpl.updateBestScore(bestScore.value!!)
        }
    }

}