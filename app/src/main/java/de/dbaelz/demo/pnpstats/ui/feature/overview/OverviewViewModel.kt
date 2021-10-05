package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) : BaseViewModel<OverviewContract.State, OverviewContract.Event, OverviewContract.Effect>() {

    init {
        viewModelScope.launch {
            // TODO: Only for testing, cause I'm lazy
            characterRepository.addCharacter("Character " + Random.nextInt())

            getCharacter()
        }
    }

    override fun provideInitialState() = OverviewContract.State.Loading

    override fun handleEvent(event: OverviewContract.Event) {}

    private suspend fun getCharacter() {
        // TODO: Move into UseCase?
        val characterId = preferenceRepository.getLastCharacterId().first()
        when (val result = characterRepository.getCharacter(characterId)) {
            is ApiResult.Success -> {
                updateState {
                    OverviewContract.State.CharacterInfo(result.value)
                }
            }
            is ApiResult.Error -> {
                setEffect { OverviewContract.Effect.ErrorLoadingCharacter }
                setEffect { OverviewContract.Effect.Navigation.ToCharacters }
            }
        }
    }
}
