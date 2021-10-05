package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CharactersScreen(
    state: CharactersContract.State,
    effectFlow: Flow<CharactersContract.Effect>?,
    onEvent: (event: CharactersContract.Event) -> Unit,
    onNavigation: (navigationEffect: CharactersContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is CharactersContract.Effect.Navigation) {
                onNavigation(effect)
            }
        }?.collect()
    }

    when (state) {
        CharactersContract.State.Loading -> LoadingIndicator()
        is CharactersContract.State.Characters -> CharactersList(state.characters) {
            onEvent(CharactersContract.Event.CharacterSelected(it))
        }
    }

}


@Composable
fun CharactersList(characters: List<Character>, onCharacterSelected: (Int) -> Unit) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Characters", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h3)

        characters.forEach {
            Text(text = it.name, modifier = Modifier.clickable { onCharacterSelected(it.id) })

            Spacer(Modifier.height(16.dp))
        }
    }
}