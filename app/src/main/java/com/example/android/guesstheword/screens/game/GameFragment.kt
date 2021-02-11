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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

    // The current word
    private var word = ""

    // The current score
    private var score = 0

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate view and obtain an instance of the binding class
        binding = GameFragmentBinding.inflate(inflater, container, false)

        resetList()
        nextWord()

        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        updateScoreText()
        updateWordText()
        return binding.root

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
     * Called when the game is finished
     *
     * Another thing to mention is that when navigating from the game destination to score
     * destination, a pop to behavior is set. This pop to behavior modifies the back stack to pop
     * off the game destination. This ensures that if you press the back button from the score
     * screen, you're never going to be taken back to a finished game.
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(score)
        findNavController(this).navigate(action)
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            gameFinished()
        } else {
            word = wordList.removeAt(0)
        }
        updateWordText()
        updateScoreText()
    }

    /** Methods for buttons presses **/

    private fun onSkip() {
        score--
        nextWord()
    }

    private fun onCorrect() {
        score++
        nextWord()
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        binding.wordText.text = word

    }

    private fun updateScoreText() {
        binding.scoreText.text = score.toString()
    }
}
