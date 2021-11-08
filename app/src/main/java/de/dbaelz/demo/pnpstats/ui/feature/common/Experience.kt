package de.dbaelz.demo.pnpstats.ui.feature.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.R

@Composable
fun ExperienceCircle(experience: Int) {
    Text(
        text = stringResource(R.string.experience_value_formatted, experience.toString()),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = Modifier
            .border(8.dp, MaterialTheme.colors.primary, CircleShape)
            .padding(32.dp)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                layout(placeable.width, placeable.width) {
                    placeable.placeRelative(
                        0,
                        (placeable.width - placeable.measuredHeight) / 2
                    )
                }
            }

    )
}