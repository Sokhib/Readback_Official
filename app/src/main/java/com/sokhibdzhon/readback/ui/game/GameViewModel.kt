package com.sokhibdzhon.readback.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.sokhibdzhon.readback.data.model.Word
import timber.log.Timber
import javax.inject.Inject

private const val LEVELS = "levels"
private const val WORDS = "words"
private const val DONE = 0L
private const val ONE_SECOND = 1000L
private const val COUNTDOWN_TIME = 60000L

class GameViewModel @Inject constructor(val firestoreDb: FirebaseFirestore) : ViewModel() {

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
        val levelFromDb = 1
        firestoreDb.collection(LEVELS).document(levelFromDb.toString()).collection(WORDS)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (word in document.documents) {
                        val options = word.get("options") as MutableList<String>
                        options.shuffle()
                        val correct = word.get("correct") as String
                        val word = word.get("word") as String
                        Timber.d("correct: $correct --> options: $options --> word: $word")
                        if (_wordList.value == null) {
                            _wordList.value = mutableListOf(Word(correct, options, word))
                            Timber.d("Word added")
                        } else {
                            _wordList.value!!.add(Word(correct, options, word))
                        }
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
            _wordList.value!!.shuffle()
            _current.value = _wordList.value!!.removeAt(0)
        } else {
            _gameFinish.value = true
        }
    }

    fun checkForCorrectness(text: String) {
        if (current.value!!.correct == text) {
            _correct.value = true
            _score.value = (score.value)?.plus(10)

        } else {
            _correct.value = false
            _score.value = (score.value)?.minus(5)
        }
    }

    fun isCorrect() = correct.value
    fun onGameFinished() {
        _gameFinish.value = false
    }
}