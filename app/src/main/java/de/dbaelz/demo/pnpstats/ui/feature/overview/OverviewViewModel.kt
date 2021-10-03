package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseViewModel<OverviewContract.Event, OverviewContract.State, OverviewContract.Effect>() {

    init {
        viewModelScope.launch {
            // TODO: Add use cases?
            //val character = characterRepository.getCharacter(0)
            val character = Character("Dummy Character", 42)

            updateState {
                OverviewContract.State.CharacterInfo(character)
            }
        }
    }

    override fun provideInitialState() = OverviewContract.State.Loading
}
