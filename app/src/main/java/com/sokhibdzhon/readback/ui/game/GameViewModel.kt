package com.sokhibdzhon.readback.ui.game

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.Status
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

//App doing too much shit on MainThread get data from shared from back thread.
//Get sharedPref in back then set to timer then get words then start.
class GameViewModel @Inject constructor(
    val gameRepoImpl: GameRepoImpl,
    sharedPref: SharedPreferences
) : ViewModel() {
    private companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val CORRECT_POINTS = 1
        private const val INCORRECT_POINTS = -1
        private const val SECONDS = "seconds"
        private const val SKIPS = "skips"

    }

    private var _wordList = MutableLiveData<Resource<MutableList<Word>>>()
    val wordList: LiveData<Resource<MutableList<Word>>>
        get() = _wordList

    private val _correct = MutableLiveData<Boolean>()
    private val correct: LiveData<Boolean>
        get() = _correct

    private val _gameFinish = MutableLiveData<Boolean>()
    val gameFinish: LiveData<Boolean>
        get() = _gameFinish

    private val _current = MutableLiveData<Word>()
    val current: LiveData<Word>
        get() = _current

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private lateinit var timer: CountDownTimer
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft: LiveData<Long>
        get() = _timeLeft

    private val _skipNumber = MutableLiveData(5)
    val skipNumber: LiveData<Int>
        get() = _skipNumber

    private val _type = MutableLiveData(GameType.CUSTOMGAME)
    val type: LiveData<GameType>
        get() = _type

    private val _level = MutableLiveData(1)
    val level: LiveData<Int>
        get() = _level


    init {
        _correct.value = false
        _gameFinish.value = false
        _score.value = 0
        viewModelScope.launch {
            _timeLeft.value = sharedPref.getInt(SECONDS, 15).toLong()
            _skipNumber.value = sharedPref.getInt(SKIPS, 5)
        }
        prepareTimer()
    }

    fun getWords(level: Int, type: GameType) {
        gameRepoImpl.getCustomGameWords(level, type)
            .onEach {
                Timber.d("${it.status}")
                if (it.status == Status.SUCCESS) {
                    _wordList.value = it
                    Timber.d("${_wordList.value?.data?.size}")
                    timer.start()
                }
            }.launchIn(viewModelScope)
    }

    private fun prepareTimer() {
        timer = object : CountDownTimer(
            timeLeft.value!! * 1000,
            ONE_SECOND
        ) {
            override fun onFinish() {
                _gameFinish.value = true
                _timeLeft.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / ONE_SECOND)
            }

        }
    }

    fun minusSkip() {
        _skipNumber.value = _skipNumber.value!! - 1
    }

    fun nextWord() {
        if (!_wordList.value?.data.isNullOrEmpty()) {
            _current.value = _wordList.value!!.data!!.removeAt(0)
        } else {
            _correct.value = true
            _gameFinish.value = true
        }
    }

    fun checkForCorrectness(text: String): Boolean {
        _correct.value = if (current.value!!.correct == text) {
            updateScore(CORRECT_POINTS)
            true
        } else {
            updateScore(INCORRECT_POINTS)
            _gameFinish.value = true
            false
        }
        return correct.value!!
    }

    private fun updateScore(value: Int) {
        _score.value = (score.value)?.plus(value)
    }

    fun isCorrect() = correct.value

    fun setType(type: GameType) {
        _type.value = type
    }

    fun setLevel(level: Int) {
        _level.value = level
    }

    fun onGameFinished() {
        _gameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}