package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
        is CharactersContract.State.Characters -> CharactersList(
            characters = state.characters,
            onCharacterSelected = {
                onEvent(CharactersContract.Event.CharacterSelected(it))
            },
            onCharacterDeleted = {
                onEvent(CharactersContract.Event.CharacterDeleted(it))
            })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CharactersList(
    characters: List<Character>,
    onCharacterSelected: (Int) -> Unit,
    onCharacterDeleted: (Int) -> Unit
) {
    // TODO: Only dummy UI
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(
            items = characters,
            key = { character -> character.id }
        ) { item ->
            val dismissState = rememberDismissState {
                if (it == DismissValue.DismissedToStart) {
                    onCharacterDeleted(item.id)
                }
                it != DismissValue.DismissedToStart
            }

            SwipeToDismiss(
                modifier = Modifier.padding(vertical = 8.dp),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {

                },
                dismissContent = {
                    CharacterListCard(item, onCharacterSelected)
                })
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