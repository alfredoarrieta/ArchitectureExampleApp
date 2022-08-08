package com.example.myapplication.redux.implementation

import com.example.myapplication.redux.implementation.actions.ReduxAction
import com.example.myapplication.redux.implementation.epics.BaseEpic
import com.example.myapplication.redux.implementation.reducers.BaseReducer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.component.KoinComponent

class AppStore(private val epics: List<BaseEpic>, private val reducers: List<BaseReducer>) :
    KoinComponent {

    private val state = BehaviorSubject.create<AppState>()

    init {
        state.onNext(AppState())
    }

    fun getState(): AppState {
        return state.value
    }

    fun getObservableState(): Observable<AppState> {
        return state.observeOn(AndroidSchedulers.mainThread())
    }

    fun dispatch(action: ReduxAction) {
        reducers.forEach { reducer ->
            val newSate = reducer.reduce(action, state.value)
            if (state.value != newSate) {
                state.onNext(newSate)
            }
        }
        epics.forEach { it.actionReceived(action, this) }
    }
}