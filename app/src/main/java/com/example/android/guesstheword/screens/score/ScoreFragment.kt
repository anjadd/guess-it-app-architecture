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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 *
 * This fragment gets the score passed in from the argument bundle and displays it.
 * There’s also a button for playing again, that takes you back to the GameFragment.
 */
class ScoreFragment : Fragment() {

    // Reference the ViewModel in your fragment class
    private lateinit var viewModel: ScoreViewModel

    // Reference the ViewModelFactory in your fragment class
    private lateinit var viewModelFactory: ScoreViewModelFactory

    private lateinit var binding: ScoreFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false)

        // Create and construct a ScoreViewModelFactory
        viewModelFactory = ScoreViewModelFactory(getScoreArgumentFromGameFragment())

        /** Get a reference to the ViewModel
         * Never construct ViewModels yourself. Instead, request creation of the ViewModel from a
         * class called ViewModelProvider, which provides you with the correct ViewModel. In this
         * method you will pass in your fragment with the reference ‘this’, and you pass in the
         * specific ViewModel  class that you want.
         *
         * By passing the ViewModel factory as a second argument, you're telling the
         * ViewModelProvider to use this specific factory to create ScoreViewModel*/
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ScoreViewModel::class.java)

        // Adding ViewModel to data binding
        // Pass the ViewModel to the binding variable in the UI Controller
        binding.scoreViewModel = viewModel

        viewModel.score.observe(viewLifecycleOwner, { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        /* When you add ViewModel to data binding, you can replace the OnClickListeners in the UI
        Controller, by setting up OnClickListeners in the XML layout.

        //Tell the ViewModel that the play again button has been clicked
        binding.playAgainButton.setOnClickListener {
            viewModel.onPlayAgain()
        }*/

        /* When a certain event happens (e.g. a Play again Event, when the user pressed the Play
        again button to start a new guessing game), the LiveData should tell the UI Controller that
        the play again has completed, and the UI Controller would know that it should navigate to
        another screen. This navigates back to title when Play again button is pressed*/
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, { isPlayAgainClicked ->
            if (isPlayAgainClicked) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })

        return binding.root
    }

    private fun getScoreArgumentFromGameFragment(): Int {

        //Get args about final score from Game fragment, option 1
        val scoreArgs = ScoreFragmentArgs.fromBundle(requireArguments())
        return scoreArgs.score

        // Get args using by navArgs property delegate, option 2
/*        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        binding.scoreText.text = scoreFragmentArgs.score.toString()*/
    }

}
