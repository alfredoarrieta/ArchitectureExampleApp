package com.example.myapplication.redux.reducers

import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.FragmentType
import com.example.myapplication.redux.implementation.NavigationState
import com.example.myapplication.redux.implementation.actions.NavigationActions
import com.example.myapplication.redux.implementation.reducers.NavigationReducer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NavigationReducerTest {

    private lateinit var navigationReducer: NavigationReducer

    @Before
    fun setup() {
        navigationReducer = NavigationReducer()
    }

    @Test
    fun goToStoreTest() {
        // GIVEN
        val navigationState = NavigationState()
        val appState = AppState(navigationState = navigationState)
        // WHEN
        val resultAppState = navigationReducer.reduce(
            NavigationActions.SetLastFragment(FragmentType.STORE),
            appState
        )
        // THEN
        with(resultAppState.navigationState) {
            Assert.assertEquals(FragmentType.STORE, lastFragment)
        }
    }

    @Test
    fun goToCartTest() {
        // GIVEN
        val navigationState = NavigationState()
        val appState = AppState(navigationState = navigationState)
        // WHEN
        val resultAppState = navigationReducer.reduce(
            NavigationActions.SetLastFragment(FragmentType.CART),
            appState
        )
        // THEN
        with(resultAppState.navigationState) {
            Assert.assertEquals(FragmentType.CART, lastFragment)
        }
    }

    @Test
    fun goToAboutTest() {
        // GIVEN
        val navigationState = NavigationState()
        val appState = AppState(navigationState = navigationState)
        // WHEN
        val resultAppState = navigationReducer.reduce(
            NavigationActions.SetLastFragment(FragmentType.ABOUT),
            appState
        )
        // THEN
        with(resultAppState.navigationState) {
            Assert.assertEquals(FragmentType.ABOUT, lastFragment)
        }
    }

    @Test
    fun goToBothTest() {
        // GIVEN
        val navigationState = NavigationState(lastFragment = FragmentType.CART)
        val appState = AppState(navigationState = navigationState)
        // WHEN
        val resultAppState = navigationReducer.reduce(
            NavigationActions.SetLastFragment(FragmentType.ABOUT),
            appState
        )
        // THEN
        with(resultAppState.navigationState) {
            Assert.assertEquals(FragmentType.BOTH, lastFragment)
        }
    }

}