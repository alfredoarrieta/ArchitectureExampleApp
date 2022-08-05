package com.example.myapplication.redux.implementation.epics

import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.actions.ReduxAction
import com.example.myapplication.redux.implementation.actions.StoreActions

class StoreEpic(private val localDataSource: LocalDataSource): BaseEpic() {

    override fun actionReceived(action: ReduxAction, appStore: AppStore) {
        when(action){
            is StoreActions.GetStoreProducts -> {
                appStore.dispatch(StoreActions.SetStoreProducts(localDataSource.getStoreProducts()))
            }
        }
    }
}