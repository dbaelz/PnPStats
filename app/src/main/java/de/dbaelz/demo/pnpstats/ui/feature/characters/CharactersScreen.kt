package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.R
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.characters.CharactersContract.*
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CharactersScreen(
    state: State,
    effectFlow: Flow<Effect>?,
    onEvent: (event: Event) -> Unit,
    onNavigation: (navigationEffect: Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is Effect.Navigation) {
                onNavigation(effect)
            }
        }?.collect()
    }

    when (state) {
        State.Loading -> LoadingIndicator()
        is State.Characters -> CharactersList(
            characters = state.characters,
            onCharacterSelected = {
                onEvent(Event.CharacterSelected(it))
            },
            onCharacterDeleted = {
                onEvent(Event.CharacterDeleted(it))
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
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 24.dp, end = 4.dp)
                            .background(Color.Red, RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.characters_delete_content_description),
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxHeight()
                                .width(48.dp)
                        )
                    }
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
                text = stringResource(R.string.experience_value_formatted, character.experience),
                style = MaterialTheme.typography.h6
            )

            Text(
                text = character.currency.toFormattedString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
