package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExperienceScreen(
    state: ExperienceContract.State,
    effectFlow: Flow<ExperienceContract.Effect>?,
    onEvent: (event: ExperienceContract.Event) -> Unit,
    onNavigation: (navigationEffect: ExperienceContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is ExperienceContract.Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is ExperienceContract.Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        ExperienceContract.State.Loading -> LoadingIndicator()
        is ExperienceContract.State.ExperienceInfo -> {
            var errorState by remember { mutableStateOf(false) }

            ExperienceInfo(state.experience, errorState) {
                val value = it.toIntOrNull()

                if (value == null || value < 0) {
                    errorState = true
                } else {
                    errorState = false
                    onEvent(ExperienceContract.Event.ExperienceAdded(value))
                }
            }
        }
    }
}

@Composable
private fun ExperienceInfo(
    experience: Int,
    isError: Boolean,
    onExperienceAdded: (value: String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "$experience XP",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(8.dp, MaterialTheme.colors.primary, RoundedCornerShape(16.dp))
                .padding(32.dp)
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TextFieldDefaults.MinHeight)
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    onExperienceAdded(textFieldValue.text)
                },
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(Icons.Default.AddCircle, Icons.Default.AddCircle.name)
            }
        }

    }
}