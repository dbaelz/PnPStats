package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
private fun CharactersList(characters: List<Character>, onCharacterSelected: (Int) -> Unit) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        characters.forEach {
            CharacterListCard(it, onCharacterSelected)

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CharacterListCard(character: Character, onCharacterSelected: (Int) -> Unit) {
    Card(
        elevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onCharacterSelected(character.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${character.experience} XP",
                style = MaterialTheme.typography.h6
            )

            Row {
                Currency("pp", character.currency.platinum)
                Currency("gp", character.currency.gold)
                Currency("sp", character.currency.silver)
                Currency("cp", character.currency.copper, false)
            }
        }
    }
}

@Composable
private fun Currency(coinName: String, value: Int, withDelimiter: Boolean = true) {
    Text(
        text = "$value $coinName${if (withDelimiter) " • " else ""}",
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(end = 8.dp)
    )
}