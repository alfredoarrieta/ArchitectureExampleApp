package com.example.myapplication.redux.implementation.epics

import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.actions.ReduxAction

abstract class BaseEpic {
    abstract fun actionReceived(action: ReduxAction, appStore: AppStore)
}