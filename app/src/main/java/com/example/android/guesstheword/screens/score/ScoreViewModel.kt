package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Create a ViewModel that takes in a constructor parameter
class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()

    val score: LiveData<Int>
        get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()

    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        /*Use Log statement to test if you are successfully getting the score argument through
          the ScoreViewModelFactory:
        Log.i("ScoreViewModel", "Final score: $finalScore") */

        //Get the score argument (finalScore) through the ScoreViewModelFactory:
        _score.value = finalScore

        //Initialize the eventPlayAgain to false, meaning the user hasn't clicked the Play again btn
        _eventPlayAgain.value = false
    }

    /* This function calls the UI Controller, so it can tell the ViewModel that the play again
    button has been clicked*/
    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    /*You need to tell the ViewModel that the onPlayAgain() in the fragment has completed, otherwise
    each time you rotate the phone, the code in the observer (e.g. the onPlayAgain() which navigates
    to Game Start screen) gets called again.

    This means that the event gets called multiple times, but you want that play again event to only
    happen once. So create a function in which you will take that event and set its value back to
    false. This will actually represent that the event has been handled, and will ensure that the
    play again event will only happen once, even after rotation.*/
    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}