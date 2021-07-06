package com.example.myapplication.redux.implementation.reducers

import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.FragmentType
import com.example.myapplication.redux.implementation.actions.NavigationActions
import com.example.myapplication.redux.implementation.actions.ReduxAction

class NavigationReducer: BaseReducer() {

    override fun reduce(action: ReduxAction, state: AppState): AppState {
        return when(action){
            is NavigationActions.SetLastFragment -> setLastFragment(action,state)
            else -> state
        }
    }

    private fun setLastFragment(action: NavigationActions.SetLastFragment, state: AppState): AppState {
        val newFragmentType = if(state.navigationState.lastFragment == FragmentType.CART && action.lastFragment == FragmentType.ABOUT){
            FragmentType.BOTH
        }else{
            action.lastFragment
        }
        return state.copy(navigationState = state.navigationState.copy(lastFragment = newFragmentType))
    }
}