package de.dbaelz.demo.pnpstats.ui.feature.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.dbaelz.demo.pnpstats.R

@Composable
fun LoadingIndicator() {
    // TODO: Only dummy UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.loading_indicator_text),
            style = MaterialTheme.typography.h3
        )
    }
}