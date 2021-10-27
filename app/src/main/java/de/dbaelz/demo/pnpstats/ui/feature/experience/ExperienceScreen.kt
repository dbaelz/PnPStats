package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.ExperienceCircle
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceContract.Effect
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceContract.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExperienceScreen(
    state: ExperienceContract.State,
    effectFlow: Flow<Effect>?,
    onEvent: (event: Event) -> Unit,
    onNavigation: (navigationEffect: Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        ExperienceContract.State.Loading -> LoadingIndicator()
        is ExperienceContract.State.ExperienceInfo -> {
            var errorState by remember { mutableStateOf(false) }

            ExperienceInfo(state.experience, errorState) { experienceText: String, reason: String ->
                val experience = experienceText.toIntOrNull()

                if (experience == null || experience < 0) {
                    errorState = true
                } else {
                    errorState = false
                    onEvent(Event.AddExperience(state.characterId, experience, reason))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ExperienceInfo(
    experience: Int,
    isError: Boolean,
    onExperienceAdded: (experience: String, reason: String) -> Unit
) {
    var experienceInput by rememberSaveable { mutableStateOf("") }
    var reasonInput by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val addExperience = {
        keyboardController?.hide()
        onExperienceAdded(experienceInput, reasonInput)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExperienceCircle(experience)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = experienceInput,
            onValueChange = { experienceInput = it },
            label = { Text("Experience") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = isError,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TEST_EXPERIENCE_TEXTFIELD")
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = reasonInput,
            onValueChange = { reasonInput = it },
            label = { Text("Reason") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    addExperience()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TEST_REASON_TEXTFIELD")
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                addExperience()
            },
            enabled = experienceInput.isNotEmpty(),
            modifier = Modifier
                .height(TextFieldDefaults.MinHeight)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.AddCircle, Icons.Default.AddCircle.name)
        }

    }
}