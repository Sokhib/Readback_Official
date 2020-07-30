package com.sokhibdzhon.readback.ui.game

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sokhibdzhon.readback.data.model.Word
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

//App doing too much shit on MainThread get data from shared from back thread.
//Get sharedPref in back then set to timer then get words then start.
class GameViewModel @Inject constructor(
    val firestoreDb: FirebaseFirestore,
    sharedPref: SharedPreferences
) : ViewModel() {
    private companion object {
        private const val LEVELS = "levels"
        private const val WORDS = "words"
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val CORRECT_POINTS = 10
        private const val INCORRECT_POINTS = -5
    }

    private var words: MutableList<Word>? = null
    private var _wordList = MutableLiveData<MutableList<Word>>()
    val wordList: LiveData<MutableList<Word>>
        get() = _wordList

    private val _correct = MutableLiveData<Boolean>()
    val correct: LiveData<Boolean>
        get() = _correct

    private val _gameFinish = MutableLiveData<Boolean>()
    val gameFinish: LiveData<Boolean>
        get() = _gameFinish

    private val _isConnected = MutableLiveData<Boolean>(true)
    val isConnected: LiveData<Boolean>
        get() = _isConnected

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


    init {
        _correct.value = false
        _gameFinish.value = false
        _score.value = 0
        viewModelScope.launch {
            _timeLeft.value = sharedPref.getInt("seconds", 15).toLong()
            _skipNumber.value = sharedPref.getInt("skips", 5)
        }
        prepareTimer()
        getWords()
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

    private fun getWords() {
        firestoreDb.collection(LEVELS).document("custom").collection(WORDS)
            .get()
            .addOnSuccessListener { document ->
                document?.let { doc ->
                    for (currentWord in doc.documents) {
                        // In case there are wrong inserts data in Firebase Firestore not to get crash.
                        val options =
                            (currentWord.get("options") as MutableList<String>?)?.let {
                                currentWord.get("options") as MutableList<String>
                            } ?: mutableListOf("", "", "", "")
                        options.shuffle()
                        val correct =
                            (currentWord.get("correct") as String?)?.let { currentWord.get("correct") as String }
                                ?: ""
                        val word =
                            (currentWord.get("word") as String?)?.let { currentWord.get("word") as String }
                                ?: ""

//                        Timber.d("correct: $correct --> options: $options --> word: $word")
                        words?.let {
                            words!!.add(Word(correct, options, word))
                        } ?: run {
                            words = mutableListOf(Word(correct, options, word))
                            Timber.d("Word added")
                        }
                    }
                    if (!words.isNullOrEmpty()) {
//                        Timber.d("SHUFFLED :)")
                        words!!.shuffle()
                        _wordList.value = words
                        timer.start()
                    } else {
                        _isConnected.value = false
                        _gameFinish.value = true
                    }
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