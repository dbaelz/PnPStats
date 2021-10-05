package de.dbaelz.demo.pnpstats.ui.feature.characterselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character

@Composable
fun CharacterSelection(characters: List<Character>, onCharacterSelected: (Int) -> Unit) {
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