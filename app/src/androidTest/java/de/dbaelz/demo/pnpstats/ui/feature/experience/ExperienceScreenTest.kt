package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExperienceScreenTest {
    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun loadingIndicatorComposableIsShown() {
        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.Loading,
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading...").assertExists()
    }

    @Test
    fun correctExperienceValueIsDisplayed() {
        val initialExperience = 4242

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(1, initialExperience),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading...").assertDoesNotExist()
        testRule.onNodeWithText("$initialExperience XP").assertExists().assertIsDisplayed()
        testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD")).assertExists().assertIsDisplayed()
        testRule.onNodeWithContentDescription(Icons.Default.AddCircle.name)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun addingExperienceWithButtonTriggersEvent() {
        val characterId = 23
        val initialExperience = 4242
        val addedExperience = 3

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(characterId, initialExperience),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            characterId, (it as ExperienceContract.Event.AddExperience).characterId
                        )
                        assertEquals(addedExperience, it.experience)
                    },
                    onNavigation = {}
                )
            }
        }

        val textField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))
        val button = testRule.onNodeWithContentDescription(Icons.Default.AddCircle.name)

        textField.performTextInput(addedExperience.toString())
        button.performClick()
    }

    @Test
    fun addingExperienceWithKeyboardActionTriggersEvent() {
        val characterId = 23
        val initialExperience = 4242
        val addedExperience = 3

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(characterId, initialExperience),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            characterId, (it as ExperienceContract.Event.AddExperience).characterId
                        )
                        assertEquals(addedExperience, it.experience)
                    },
                    onNavigation = {}
                )
            }
        }

        val textField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))

        textField.performTextInput(addedExperience.toString())
        textField.performImeAction()
    }

    @Test
    fun addingInvalidInputShowsError() {
        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(23, 4242),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        val textField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))

        textField.performTextInput("invalid input")
        textField.performImeAction()
        textField.assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Error))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Error, "Invalid input"))
    }
}