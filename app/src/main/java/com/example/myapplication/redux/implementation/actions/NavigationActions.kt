package com.example.myapplication.redux.implementation.actions

import com.example.myapplication.redux.implementation.FragmentType

open class NavigationActions: ReduxAction() {
    class SetLastFragment(val lastFragment: FragmentType): NavigationActions()
}