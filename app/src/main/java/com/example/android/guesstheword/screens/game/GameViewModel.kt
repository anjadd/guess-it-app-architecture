package com.example.android.guesstheword.screens.game

import androidx.lifecycle.MutableLiveData
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

    /**
     * The fields in the ViewModel whose change will cause the UI to update itself, should be made
     * LiveDatas.
     * You will convert your ordinary fields into LiveData, by turning them into
     * MutableLiveData, which is a LiveData whose value can be changed. The MutableLiveData is a
     * generic class, so you will also have to specify what type of data it holds.
     * */

    // The current word
    //var word = ""

    // The current score
    //var score = 0

    // The current word wrapped / converted in LiveData
    var word = MutableLiveData<String>()

    // The current score wrapped / converted in LiveData
    var score = MutableLiveData<Int>()

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        /*You should do the resetList() and nextWord() initialization when the ViewModel gets
        created. If this initialization was still in the fragment, it will get called every time
        the fragment gets created, which is wrong. */
        resetList()
        nextWord()

        /*To set a value to a MutableLiveData, use the .value method*/
        score.value = 0
    }

    override fun onCleared() {
        //GameViewModel gets destroyed
        super.onCleared()
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
            //word = wordList.removeAt(0)
            word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses
     * The methods onCorrect() and onSkip() do some data processing, so they belong in the ViewModel**/

    fun onSkip() {
        /* When the variable score was a simple Int, you could do the subtraction like this: score--.
        But when the score is a MutableLiveData, its type is nullable, so you need to check each
        time you call methods on it, that its value is not null.
        So call your methods on LiveDatas with null safety*/
        //score--
        score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        /* When the variable score was a simple Int, you could do the addition like this: score++.
        But when the score is a MutableLiveData, its type is nullable, so you need to check each
        time you call methods on it, that its value is not null.
        So call your methods on LiveDatas with null safety.*/
        //score++
        score.value = (score.value)?.plus(1)
        nextWord()
    }
}