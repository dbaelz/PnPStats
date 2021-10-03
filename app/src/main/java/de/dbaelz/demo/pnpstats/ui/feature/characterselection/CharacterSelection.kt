package de.dbaelz.demo.pnpstats.ui.feature.characterselection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character

@Composable
fun CharacterSelection(characters: List<Character>) {
    // TODO: Only dummy UI
    Column {
        Text(text = "Characters", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h3)

        characters.forEach {
            Text(it.name)

            Spacer(Modifier.height(16.dp))
        }
    }
}