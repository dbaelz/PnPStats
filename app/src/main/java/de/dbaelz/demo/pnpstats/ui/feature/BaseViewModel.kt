package de.dbaelz.demo.pnpstats.ui.feature

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState : ViewState, Event : ViewEvent, Effect : ViewSideEffect> :
    ViewModel() {

    // State
    private val initialState: UiState by lazy { provideInitialState() }
    abstract fun provideInitialState(): UiState

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    // Event
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    // Effect
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _event.collect {
                handleEvent(it)
            }
        }
    }


    protected fun updateState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }


    fun processEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    abstract fun handleEvent(event: Event)

    protected fun setEffect(effectBuilder: () -> Effect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }
}

interface ViewEvent
interface ViewState
interface ViewSideEffect

const val LAUNCHED_EFFECT_KEY = "launched-effect"