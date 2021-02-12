/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 *
 *
 * This fragment contains all the logic for the GuessIt game itself. It contains:
 *
 * - word - A variable for the current word to guess.
 *
 * - score - A variable for the current score.
 *
 * - wordList - A variable for a mutable list of all the words you need to guess. This list is
 * created and immediately shuffled using resetList() so that you get a new order of words every time.
 *
 * - resetList() - Method that creates and shuffles the list of words.
 *
 * - onSkip()/onCorrect() - Methods for when you press the Skip/Got It buttons. They modify the
 * score and then go to the next word in your worldList.
 *
 * - nextWord() - A method for moving to the next word to guess. If there are still words in your
 * mutable list of words, remove the current word, and then set currentWord to whatever is next in
 * the list. This will finish the game if there are no more words to guess.
 *
 * - gameFinished() - A method that is called to finish the game. This passes your current score
 * to the ScoreFragment using SafeArgs.
 */
class GameFragment : Fragment() {

    // Reference to the ViewModel in your fragment class
    private lateinit var viewModel: GameViewModel


    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate view and obtain an instance of the binding class
        binding = GameFragmentBinding.inflate(inflater, container, false)

        /** Get a reference to the ViewModel
         * Never construct ViewModels yourself, because if you did, you’d end up constructing a
         * ViewModel every time the fragment was recreated, which wouldn’t solve the problems with
         * configuration changes. Instead, the Lifecycle Library creates the ViewModel for you.
         *
         * You request creation of the ViewModel from a class called ViewModelProvider, which
         * provides you with the correct ViewModel. In this method you pass your fragment with the
         * reference ‘this’, and you pass in the specific ViewModel class that you want.
         *
         * The first time that this line of code is executed, it will create a new instance of the
         * GameViewModel for you, and that instance will be associated with the class that is passed
         * before it (the fragment). When ViewModelProvider is called again, it will return a
         * reference to a pre-existed GameViewModel associated with this UI Controller.
         * This is what reestablishes the connection to the same ViewModel.
         *
         * The Game Fragment would be responsible for displaying the game fragment: drawing the game
         * elements to the screen, and knowing when the user presses the buttons, nothing more.*/

        Log.i("GameFragment", "Called ViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

/*      You should do the next initialization when the ViewModel gets created, and not every time
        that the fragment gets created.
        resetList()
        nextWord()*/

        /*Setting up OnClickListeners and updating the screen are responsibility only of the UI
        Controllers.
        The methods onCorrect() and onSkip() do some data processing, so they belong in the ViewModel
        The methods updateScoreText() and updateWordText() are used to update the screen, so they
        belong in the UI Controller. They are called each time the word gets changed.
        */
        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
            updateScoreText()
            updateWordText()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
            updateScoreText()
            updateWordText()
        }

        return binding.root
    }

    /**
     * Called when the game is finished
     *
     * Another thing to mention is that when navigating from the game destination to score
     * destination, a pop to behavior is set. This pop to behavior modifies the back stack to pop
     * off the game destination. This ensures that if you press the back button from the score
     * screen, you're never going to be taken back to a finished game.
     *
     * Regarding the MVVM design, the gameFinished() method causes a navigation, and navigation needs
     * access to a nav controller. And nav controller is found by passing in a view or fragment,
     * which are things you don't want in the ViewModel. So basically, any navigation that you do,
     * needs to be done in the UI Controller.
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score)
        findNavController(this).navigate(action)
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        binding.wordText.text = viewModel.word

    }

    private fun updateScoreText() {
        binding.scoreText.text = viewModel.score.toString()
    }
}
