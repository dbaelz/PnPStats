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
                    state = ExperienceContract.State.ExperienceInfo(
                        1,
                        initialExperience,
                        emptyList()
                    ),
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
            .assertIsNotEnabled()
    }

    @Test
    fun addingExperienceWithButtonTriggersEvent() {
        val characterId = 23
        val initialExperience = 4242
        val addedExperience = 3
        val addedReason = "Test Reason"

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(
                        characterId,
                        initialExperience,
                        emptyList()
                    ),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            characterId, (it as ExperienceContract.Event.AddExperience).characterId
                        )
                        assertEquals(addedExperience, it.experience)
                        assertEquals(addedReason, it.reason)

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        val experienceTextField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))
        val reasonTextField = testRule.onNode(hasTestTag("TEST_REASON_TEXTFIELD"))
        val button = testRule.onNodeWithContentDescription(Icons.Default.AddCircle.name)

        button.assertIsNotEnabled()
        experienceTextField.performTextInput(addedExperience.toString())
        reasonTextField.performTextInput(addedReason)
        button.assertIsEnabled().performClick()

        testRule.waitUntil { onEventFinished }
    }

    @Test
    fun addingExperienceWithKeyboardActionTriggersEvent() {
        val characterId = 23
        val initialExperience = 4242
        val addedExperience = 3
        val addedReason = "Test Reason"

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(
                        characterId,
                        initialExperience,
                        emptyList()
                    ),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            characterId, (it as ExperienceContract.Event.AddExperience).characterId
                        )
                        assertEquals(addedExperience, it.experience)
                        assertEquals(addedReason, it.reason)

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithContentDescription(Icons.Default.AddCircle.name).assertIsNotEnabled()

        val experienceTextField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))
        val reasonTextField = testRule.onNode(hasTestTag("TEST_REASON_TEXTFIELD"))

        experienceTextField.performTextInput(addedExperience.toString())
        experienceTextField.performImeAction()
        reasonTextField.performTextInput(addedReason)
        reasonTextField.performImeAction()

        testRule.waitUntil { onEventFinished }
    }

    @Test
    fun addingInvalidInputShowsError() {
        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(23, 4242, emptyList()),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        val experienceTextField = testRule.onNode(hasTestTag("TEST_EXPERIENCE_TEXTFIELD"))
        val reasonTextField = testRule.onNode(hasTestTag("TEST_REASON_TEXTFIELD"))

        experienceTextField.performTextInput("invalid input")
        experienceTextField.performImeAction()

        reasonTextField.performImeAction()

        experienceTextField.assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Error))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Error, "Invalid input"))
    }

    @Test
    fun experienceDetailsAreShown() {
        val experienceDetails = listOf(
            100 to "",
            200 to "Test 200",
            20 to "Test 20",
            4000 to ""
        )

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(1, 12345, experienceDetails),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Experience Log").assertExists().assertIsDisplayed()
        experienceDetails.forEach {
            testRule.onNodeWithText(it.first.toString()).assertExists().assertIsDisplayed()
            if (it.second.isNotEmpty()) {
                testRule.onNodeWithText(it.second).assertExists().assertIsDisplayed()
            }
        }
    }

    @Test
    fun experienceDetailsAreNotShownWhenEmpty() {
        val experienceDetails = emptyList<Pair<Int, String>>()

        testRule.setContent {
            MaterialTheme {
                ExperienceScreen(
                    state = ExperienceContract.State.ExperienceInfo(1, 0, experienceDetails),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Experience Log").assertDoesNotExist()
    }
}