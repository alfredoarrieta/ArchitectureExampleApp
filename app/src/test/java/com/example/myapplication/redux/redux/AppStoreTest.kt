package com.example.myapplication.redux.redux

import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.CartState
import com.example.myapplication.redux.implementation.actions.ReduxAction
import com.example.myapplication.redux.implementation.epics.BaseEpic
import com.example.myapplication.redux.implementation.reducers.BaseReducer
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AppStoreTest {

    private lateinit var appStore: AppStore
    private lateinit var mockedEpic: BaseEpic
    private lateinit var mockedReducer: BaseReducer
    private val testAction: ReduxAction = object : ReduxAction() {}

    @Before
    fun setup() {
        mockedEpic = Mockito.mock(BaseEpic::class.java)
        mockedReducer = Mockito.mock(BaseReducer::class.java)
        appStore = AppStore(listOf(mockedEpic), listOf(mockedReducer))
    }

    @Test
    fun dispatchingTest() {
        // GIVEN
        val testAppState = AppState(cartState = CartState(cartTotal = 1000.0))
        whenever(mockedReducer.reduce(eq(testAction), any())).thenReturn(testAppState)
        assertNotEquals(testAppState, appStore.getState())
        // WHEN
        appStore.dispatch(testAction)
        // THEN
        Mockito.verify(mockedEpic, Times(1)).actionReceived(eq(testAction), any())
        Mockito.verify(mockedReducer, Times(1)).reduce(eq(testAction), any())
        assertEquals(testAppState, appStore.getState())
    }

}