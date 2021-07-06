package com.example.myapplication.redux.implementation.reducers

import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.actions.ReduxAction

abstract class BaseReducer {
    abstract fun reduce(action: ReduxAction, state: AppState): AppState
}