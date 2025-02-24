package com.hfad.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class GameViewModel: ViewModel() {
    //Поля
    private val words = listOf("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase()
    private var correctGuesses = ""
    private val _secretWordDisplay = MutableLiveData<String>()
    private val _incorrectGuesses = MutableLiveData<String>("")
    private val _livesLeft = MutableLiveData<Int>(8)

    //Свойства
    val secretWordDisplay: LiveData<String>
        get() = _secretWordDisplay
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses
    val livesLeft: LiveData<Int>
        get() = _livesLeft

    //Методы
    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay() : String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            } else {
                _incorrectGuesses.value += "$guess "
                _livesLeft.value = _livesLeft.value?.minus(1)
            }
        }
    }

    fun isWon() = secretWord.equals(secretWordDisplay.value, true)
    fun isLost() = livesLeft.value ?: 0 <= 0

    fun wonLostMessage() : String {
        var message = ""
        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"
        message += " The word was $secretWord."
        return message
    }
}