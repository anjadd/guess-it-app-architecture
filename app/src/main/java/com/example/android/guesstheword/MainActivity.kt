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

package com.example.android.guesstheword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Creates an Activity that hosts all of the fragments in the app
 *
 * This class does very little. It’s just a container for the NavHostFragment.
 * The fragments that go in the NavHostFragment do most of the heavy lifting.
 *
 * The main_navigation is the navigation graph for this app. The navigation flow goes from the
 * TitleFragment to the GameFragment to the ScoreFragment. From the ScoreFragment, you can play the
 * game again by going back to the GameFragment.
 *
 * Note that the action from the GameFragment to the ScoreFragment has a Pop To attribute that
 * removes the game from the backstack. This makes it so that you can never press the back button
 * from the ScoreFragment to go back to a finished game. Instead you’ll go to the title screen.
 *
 * But there is a problem with this app: The app does not properly save and restore state during
 * configuration changes and process related shutdown. You can solve this by using classes from
 * the Lifecycle Library and an Application Architecture Pattern.
 *
 *
 * ==========
 * MVVM is an Architecture Design Pattern. It stands for Model – View – ViewModel, and it is a way
 * to structure code.
 *
 * The Separation of Concerns principle is a software design principle that you will following here.
 * It says that an app should be divided into classes, and each should have separate responsibilities.
 * Here you will be working with 3 classes (for simplicity): UI Controller, ViewModel, and LiveData.
 *
 * But actually there are a lot more classes in the MVVM architecture, once you start adding things
 * like databases and a backend connection.
 *
 * ---UI Controller---
 *
 * The UI Controller is actually the activities and fragments. It is responsible for any User
 * Interface related tasks, like displaying views and capturing user input.
 *
 * But you want to limit the UI Controller to only UI related tasks. You should take any sort of
 * decision-making power out of the UI Controller.
 *
 *
 * ---ViewModel---
 *
 * The purpose of the ViewModel is to hold the specific data, which needs to be displayed in the
 * fragment/activity that the ViewModel associated with. It holds your app’s UI data and survives
 * configuration changes.
 *
 * Also ViewModel can do simple calculations and transformations on data, so it’s ready to be displayed
 * by the UI controller.
 *
 * You request creation of the ViewModel from a class called ViewModelProvider, which provides you
 * with the correct ViewModel. Never construct ViewModels yourself, because if you did, you’d end up constructing a
 * ViewModel every time the fragment was recreated, which wouldn’t solve the problems with
 * configuration changes.
 *
 * The ViewModel class will contain instances of the third class, LiveData.
 *
 *
 * ---LiveData---
 *
 * The LiveData classes are crucial for communicating information from the ViewModel to the UI
 * Controller, that it should update and redraw the screen.
 *
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}
