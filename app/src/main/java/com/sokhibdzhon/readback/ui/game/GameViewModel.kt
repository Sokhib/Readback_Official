package com.sokhibdzhon.readback.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.sokhibdzhon.readback.data.model.Word
import timber.log.Timber
import javax.inject.Inject

class GameViewModel @Inject constructor(val firestoreDb: FirebaseFirestore) : ViewModel() {
    private companion object {
        private const val LEVELS = "levels"
        private const val WORDS = "words"
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 30000L
        private const val CORRECT_POINTS = 10
        private const val INCORRECT_POINTS = -5
    }

    private var _wordList = MutableLiveData<MutableList<Word>>()
    val wordList: LiveData<MutableList<Word>>
        get() = _wordList

    private val _correct = MutableLiveData<Boolean>()
    val correct: LiveData<Boolean>
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

    private val timer: CountDownTimer
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft: LiveData<Long>
        get() = _timeLeft

    init {
        _correct.value = false
        _gameFinish.value = false
        _score.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _gameFinish.value = true
                _timeLeft.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / ONE_SECOND)
            }

        }
        getWords()
    }

    private fun getWords() {
        firestoreDb.collection(LEVELS).document("custom").collection(WORDS)
            .get()
            .addOnSuccessListener { document ->
                document?.let { doc ->
                    for (currentWord in doc.documents) {
                        val options = currentWord.get("options") as MutableList<String>
                        options.shuffle()
                        val correct = currentWord.get("correct") as String
                        val word = currentWord.get("word") as String
                        Timber.d("correct: $correct --> options: $options --> word: $word")
                        _wordList.value?.let {
                            _wordList.value!!.add(Word(correct, options, word))
                        } ?: run {
                            _wordList.value = mutableListOf(Word(correct, options, word))
                            Timber.d("Word added")
                        }
                    }
                    if (!_wordList.value.isNullOrEmpty()) {
                        Timber.d("SHUFFLED :)")
                        _wordList.value!!.shuffle()
                    }
                    timer.start()
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("Error getting documents: $exception")
            }
    }

    fun nextWord() {
        if (!_wordList.value.isNullOrEmpty()) {
            _current.value = _wordList.value!!.removeAt(0)
        } else {
            _gameFinish.value = true
        }
    }

    fun checkForCorrectness(text: String) {
        _correct.value = if (current.value!!.correct == text) {
            updateScore(CORRECT_POINTS)
            true
        } else {
            updateScore(INCORRECT_POINTS)
            false
        }
    }

    private fun updateScore(value: Int) {
        _score.value = (score.value)?.plus(value)
    }

    fun isCorrect() = correct.value
    fun onGameFinished() {
        _gameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}