package com.example.myapplication.redux.implementation

import com.example.myapplication.redux.implementation.actions.ReduxAction
import com.example.myapplication.redux.implementation.epics.CartEpic
import com.example.myapplication.redux.implementation.epics.StoreEpic
import com.example.myapplication.redux.implementation.reducers.CartReducer
import com.example.myapplication.redux.implementation.reducers.NavigationReducer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AppStore: KoinComponent {

    private val state = BehaviorSubject.create<AppState>()

    private val epics = listOf(
        CartEpic(get()),
        StoreEpic(get())
    )

    private val reducers = listOf(
        CartReducer(),
        NavigationReducer()
    )

    init {
        state.onNext(AppState())
    }

    fun getState(): AppState{
        return state.value
    }

    fun getObservableState(): Observable<AppState> {
        return state.observeOn(AndroidSchedulers.mainThread())
    }

    fun dispatch(action: ReduxAction){
        reducers.forEach { reducer ->
            val newSate = reducer.reduce(action, state.value)
            if(state.value != newSate){ state.onNext(newSate) }
        }
        epics.forEach { it.actionReceived(action, this) }
    }
}