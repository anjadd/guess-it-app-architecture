package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    /* Place the countdown timer constants in a companion object. These represent different
    important times, such as game length. */
    companion object {
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game, 60 seconds
        const val COUNTDOWN_TIME = 10000L
    }

    private val timer: CountDownTimer

    /**
     * The fields in the ViewModel whose change will cause the UI to update itself, should be made
     * LiveDatas.
     * You will convert your ordinary fields into LiveData, by turning them into
     * MutableLiveData, which is a LiveData whose value can be changed. The MutableLiveData is a
     * generic class, so you will also have to specify what type of data it holds.
     *
     * It is an unwritten rule, that you should try to restrict edit access to your LiveData. This
     * means that you should encapsulate the LiveData, by exposing a public set of methods, that
     * could modify the private internal fields.
     * Without encapsulation, you could easily access the LiveData from your UI Controller (which
     * is against the architectural rules) and change its value.
     *
     * Only the ViewModel should be able to update the LiveData values.
     * So inside the ViewModel, you want the LiveData to be a MutableLiveData (read and write are
     * enabled), but outside the ViewModel, you want the MutableLiveData to be exposed only as a
     * LiveData (only read is enabled).
     * To achieve this, you need to make two versions of each LiveData object in your ViewModel:
     *      	One object that is mutable and will be used internally:
     *          	The internal version should be a MutableLiveData, have an underscore in front
     *              of its name, and be private (because it’s private to the ViewModel). The
     *              underscore is our convention for marking the variable as the internal version
     *              of a variable.
     *          	Next, you need to update the code in your ViewModel to use the internal versions
     *              of the LiveData objects.
     *      	One object that is not mutable and will be used externally:
     *          	Override the getter for this object using a backing property and return the
     *              internal object. Defining the external object type to be LiveData, makes sure
     *              that only read option is enabled.
     *
     *
     * You can use LiveData to model some Event, like the game finished event. An event happens
     * once, and it is done, until it’s triggered again.
     * When some event happens, the LiveData can tell the UI Controller that a certain event has
     * happened (e.g. the user has guessed all the words in the list in Guess it app, so list is
     * now empty), so the UI Controller would know that it should navigate to another screen.
     * To create an event with LiveData you should:
     *      - make a LiveData object to represent your specific event;
     *      - set the LiveData for the event game finished to false at the start;
     *      - set the LiveData for the event game finished to true, when the word list is empty
     *          which means the game should finish;
     *      - add a new observer in the UI Controller for that specific LiveData event
     *      - you need to tell the ViewModel that the gameFinished() has completed and navigation
     *      has happened, otherwise each time you rotate the phone, the code in the observer
     *      (e.g. the gameFinished() which navigates to Game End screen) gets called again.
     *      This means that the event gets called multiple times, but you want that game finished
     *      event to only happen once. So create a function in which you will take that event and
     *      set its value back to false. And then tell the ViewModel that gameFinished() has
     *      completed (in the observer code for the event). This will actually represent that the
     *      event has been handled, and will ensure that the game finish event will only happen
     *      once, even after rotation.
     * */

    // The current word
    //var word = ""

    // The current score
    //var score = 0

    // The current word wrapped / converted in LiveData
    private val _word = MutableLiveData<String>()

    val word: LiveData<String>
        get() {
            return _word
        }

    // The current score wrapped / converted in LiveData
    private val _score = MutableLiveData<Int>()

    val score: LiveData<Int>
        get() {
            return _score
        }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    //Make a LiveData object to represent your game finished event
    private val _eventGameFinish = MutableLiveData<Boolean>()

    val eventGameFinish: LiveData<Boolean>
        get() {
            return _eventGameFinish
        }

    //Encapsulated LiveData for the current time left on the timer
    private var _currentTime = MutableLiveData<Long>()

    val currentTime: LiveData<Long>
        get() {
            return _currentTime
        }

    /*Formatting current time left on the timer from Long to a String (data manipulations to
    LiveData) should be done in the ViewModel by using a method called Transformations.map().
    This method takes one LiveData as an input, does the conversion on it, and outputs another
    LiveData as a result. In other words, when the data in LiveData A changes, the map function
    takes that output of LiveData A, does some conversion on it, and then outputs the result to
    another LiveData B.

    In this example, when the time changes, LiveData A can output a long representing how much time
    has passed. Then you can do a conversion on it, to format it as a String showing the elapsed
    (passed) time. Then that string will be output from LiveData B, which would update the UI
    Controller.

    - The current time will be LiveData A (current time left on the timer);
    - Create a variable currentTimeString which will be LiveData B. It will output Strings. Its
        value should be the result of the function Transformations.map() which will take in 2
        arguments: the current time as an input LiveData and the function which will convert that
        LiveData. The function for converting can be a lambda function, which will take in the
        output of current time, which is a Long. Use the same DateUtils.formatElapsedTime() to
        convert the current time into a formatted time. */
    val currentTimeString = Transformations.map(currentTime) { time -> DateUtils.formatElapsedTime(time) }

    init {
        //Set the LiveData for the event game finished to false at the start
        _eventGameFinish.value = false

        /*You should do the resetList() and nextWord() initialization when the ViewModel gets
        created. If this initialization was still in the fragment, it will get called every time
        the fragment gets created, which is wrong. */
        resetList()
        nextWord()

        /*To set a value to a MutableLiveData, use the .value method*/
        _score.value = 0

        /*Creates a timer which triggers the end of the game when it finishes*/
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisecondsUntilFinished: Long) {
                // Implement what should happen each tick of the timer
                _currentTime.value = (millisecondsUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                // Implement what should happen when the timer finishes
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }
        //Start the timer
        timer.start()
    }

    /* Cancel the timer in onCleared().
    To avoid memory leaks, you should always cancel a CountDownTimer if you no longer need it.*/
    override fun onCleared() {
        //GameViewModel gets destroyed
        super.onCleared()
        timer.cancel()
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
     *
     * When you add a countdown timer, the game should end when you run out of time. Make a change in the game end, so each time
     * the wordlist is empty, reset the list instead of ending the game.
     */

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

/*    private fun nextWordOldCode() {
    //Select and remove a word from the list
    if (wordList.isEmpty()) {
        //The game should finish when the word list is empty
        _eventGameFinish.value = true
        //gameFinished()
    } else {
        //word = wordList.removeAt(0)
        _word.value = wordList.removeAt(0)
    }
}*/

    /** Methods for buttons presses
     * The methods onCorrect() and onSkip() do some data processing, so they belong in the ViewModel**/

    fun onSkip() {
        /* When the variable score was a simple Int, you could do the subtraction like this: score--.
        But when the score is a MutableLiveData, its type is nullable, so you need to check each
        time you call methods on it, that its value is not null.
        So call your methods on LiveDatas with null safety*/
        //score--
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        /* When the variable score was a simple Int, you could do the addition like this: score++.
        But when the score is a MutableLiveData, its type is nullable, so you need to check each
        time you call methods on it, that its value is not null.
        So call your methods on LiveDatas with null safety.*/
        //score++
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /*You need to tell the ViewModel that the gameFinished() has completed and navigation
    has happened, otherwise each time you rotate the phone, the code in the observer
    (e.g. the gameFinished() which navigates to Game End screen) gets called again.
    This means that the event gets called multiple times, but you want that game finished
    event to only happen once. So create a function in which you will take that event and
    set its value back to false. This will actually represent that the event has been
    handled, and will ensure that the game finish event will only happen once, even after
    rotation.*/
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}