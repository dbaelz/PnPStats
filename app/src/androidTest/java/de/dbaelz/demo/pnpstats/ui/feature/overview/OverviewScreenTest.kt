package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {
    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun loadingIndicatorComposableIsShown() {
        testRule.setContent {
            MaterialTheme {
                OverviewScreen(
                    state = OverviewContract.State.Loading,
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading…").assertExists()
    }

    @Test
    fun correctCharacterInfoIsShown() {
        val expectedCharacter = Character(
            id = 1,
            name = "Character without a name",
            experience = 4242,
            currency = Character.Currency(11, 22, 33, 44),
            notes = "No name, no notes"
        )

        testRule.setContent {
            MaterialTheme {
                OverviewScreen(
                    state = OverviewContract.State.CharacterInfo(expectedCharacter),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText(expectedCharacter.name).assertExists().assertIsDisplayed()
        testRule.onNodeWithText("${expectedCharacter.experience} XP").assertExists()
            .assertIsDisplayed()
        testRule.onNodeWithText(expectedCharacter.currency.toFormattedString()).assertExists()
            .assertIsDisplayed()
        testRule.onNodeWithText(expectedCharacter.notes).assertExists().assertIsDisplayed()
    }

    @Test
    fun clickOnExperienceTriggersEvent() {
        val expectedCharacter = Character(
            id = 1,
            name = "Character without a name",
            experience = 4242,
            currency = Character.Currency(11, 22, 33, 44),
            notes = "No name, no notes"
        )

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                OverviewScreen(
                    state = OverviewContract.State.CharacterInfo(expectedCharacter),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            expectedCharacter.id,
                            (it as OverviewContract.Event.ExperienceSelected).characterId
                        )

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("${expectedCharacter.experience} XP")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        testRule.waitUntil { onEventFinished }
    }

    @Test
    fun clickOnCurrencyTriggersEvent() {
        val expectedCharacter = Character(
            id = 1,
            name = "Character without a name",
            experience = 4242,
            currency = Character.Currency(11, 22, 33, 44),
            notes = "No name, no notes"
        )

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                OverviewScreen(
                    state = OverviewContract.State.CharacterInfo(expectedCharacter),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            expectedCharacter.id,
                            (it as OverviewContract.Event.CurrencySelected).characterId
                        )

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText(expectedCharacter.currency.toFormattedString())
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        testRule.waitUntil { onEventFinished }
    }
}