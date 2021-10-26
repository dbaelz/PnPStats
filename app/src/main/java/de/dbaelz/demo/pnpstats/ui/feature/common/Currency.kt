package de.dbaelz.demo.pnpstats.ui.feature.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString

@Composable
fun CurrencyRectangle(currency: Character.Currency) {
    Text(
        text = currency.toFormattedString(),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(8.dp, MaterialTheme.colors.primary, RoundedCornerShape(16.dp))
            .padding(32.dp)
    )
}