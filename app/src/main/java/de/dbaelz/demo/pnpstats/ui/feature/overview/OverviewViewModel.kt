package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.CharacterRepository
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseViewModel<OverviewContract.Event, OverviewContract.State, OverviewContract.Effect>() {

    init {
        viewModelScope.launch {
            val character = characterRepository.getCharacter()
            updateState {
                copy(characterName = character.name)
            }
        }
    }

    override fun provideInitialState() = OverviewContract.State("")
}
