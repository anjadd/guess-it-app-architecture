package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

/**The ViewModel holds your app’s UI data, which needs to be displayed in the fragment/activity
 * that the ViewModel associated with.
 * Also ViewModel can do simple calculations and transformations on data, so it’s ready to be displayed
 * by the UI controller.
 * The ViewModel survives configuration changes.
 *
 * When using the ViewModel, you move all the data from your fragment into the ViewModel, and
 * there’s a reference in the fragment to the ViewModel.
 *
 * The lifecycle of a ViewModel extends from when the associated UI controller is first created,
 * till it is completely destroyed.
 *
 * To create the ViewModel class, create a class GameViewModel that extends ViewModel.
 * To track the lifetime of this ViewModel, add init block and override onCleared(), and add log
 * statements into them.*/
class GameViewModel : ViewModel() {

    // The current word
    var word = ""

    // The current score
    var score = 0

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    init {
        Log.i("GameViewModel", "GameViewModel created!")
        /*You should do the resetList() and nextWord() initialization when the ViewModel gets
        created. If this initialization was still in the fragment, it will get called every time
        the fragment gets created, which is wrong. */
        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     *
     * This list needs to be mutable, so you can shuffle it
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "dog",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     *
     * Regarding the MVVM design, the gameFinished() needs to stay in the fragment because navigation
     * is done from it. But also the ViewModel can't know about the fragment.
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //TODO fix the call to gameFinished() later
            //gameFinished()
        } else {
            word = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses
     * The methods onCorrect() and onSkip() do some data processing, so they belong in the ViewModel**/

    fun onSkip() {
        score--
        nextWord()
    }

    fun onCorrect() {
        score++
        nextWord()
    }
}