package com.sokhibdzhon.readback.ui.score

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScoreViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {
    private companion object {
        private const val SCORE = "score"
    }

    private val _bestScore = MutableLiveData(0)
    val bestScore: LiveData<Int>
        get() = _bestScore

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    init {
        viewModelScope.launch {
            _bestScore.value = sharedPreferences.getInt(SCORE, 0)
        }
    }

    fun setScore(score: Int) {
        _score.value = score
        checkBestScoreChange()
    }

    private fun checkBestScoreChange() {
        if (_bestScore.value!! < _score.value!!) {
            _bestScore.value = score.value
            sharedPreferences.edit().putInt(SCORE, bestScore.value!!).apply()
        }
    }

}