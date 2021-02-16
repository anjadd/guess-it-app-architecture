package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel Factories are classes that know how to create ViewModels.
 *
 * The arguments that are sent in a bundle from Game fragment to Score fragment, now should be given
 * to the Score fragment’s associated ViewModel when it’s initialized, so they can be displayed
 * immediately. You can do this by adding a Constructor for the ViewModel.
 *
 * So you should create a factory that will create your specific ViewModel. With this you can
 * control what parameters get passed into that specific ViewModel.
 *
 * Here are the steps for adding a Constructor for the ViewModel:
 *      o	Create a ViewModel that takes in a constructor parameter
 *      o	Make a ViewModel Factory class for that ViewModel
 *      o	In the ViewModel Factory class, define a create method, which will call your ViewModel’s
 *          Constructor with certain parameters
 *      o	Finally, add the ViewModel Factory when using the ViewModel Provider for creating the
 *          ViewModel.
 */


/**
 * Make a ViewModelFactory class for the ScoreViewModel. It should extend the class
 * ViewModelProvider.Factory and have the constructor parameter.
 *
 * Note that the constructor of your view model factory should take any parameters you want to pass
 * into your ScoreViewModel. In this case, it takes in the final score.*/
class ScoreViewModelFactory(private val finalScore: Int) : ViewModelProvider.Factory {

    //The create method will call your ViewModel’s Constructor with certain parameters
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            /* Construct and return the ScoreViewModel, passing in finalScore.

            The create method's purpose is to create and return your view model. So you should
            construct a new ScoreViewModel and return it. You'll also need to deal with the generics,
            so the statement will be: return ScoreViewModel(finalScore) as T*/
            @Suppress("UNCHECKED_CAST")
            return ScoreViewModel(finalScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}