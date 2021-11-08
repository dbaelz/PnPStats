package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CharactersScreenTest {
    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun loadingIndicatorComposableIsShown() {
        testRule.setContent {
            MaterialTheme {
                CharactersScreen(
                    state = CharactersContract.State.Loading,
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading…").assertExists().assertIsDisplayed()
    }

    @Test
    fun emptyCharactersListIsShown() {
        testRule.setContent {
            MaterialTheme {
                CharactersScreen(
                    state = CharactersContract.State.Characters(emptyList()),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading...").assertDoesNotExist()
        testRule.onNodeWithContentDescription("delete character").assertDoesNotExist()
    }

    @Test
    fun charactersListIsShown() {
        val expectedCharacters = listOf(
            Character(1, "one", 11, Character.Currency(1, 11, 111, 1111)),
            Character(2, "two", 22, Character.Currency(2, 22, 222, 2222)),
            Character(3, "three", 33, Character.Currency(3, 33, 333, 3333)),
        )

        testRule.setContent {
            MaterialTheme {
                CharactersScreen(
                    state = CharactersContract.State.Characters(expectedCharacters),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading...").assertDoesNotExist()
        testRule.onAllNodesWithContentDescription("delete character").assertCountEquals(3)

        fun assertExists(character: Character) {
            testRule.onNodeWithText(character.name).assertExists().assertIsDisplayed()
            testRule.onNodeWithText("${character.experience} XP").assertExists().assertIsDisplayed()
            testRule.onNodeWithText(character.currency.toFormattedString()).assertExists()
                .assertIsDisplayed()
        }

        expectedCharacters.forEach {
            assertExists(it)
        }
    }

    @Test
    fun clickOnCharacterTriggersSelectedEvent() {
        val expectedCharacters = listOf(
            Character(1, "one", 11, Character.Currency(1, 11, 111, 1111)),
            Character(2, "two", 22, Character.Currency(2, 22, 222, 2222)),
            Character(3, "three", 33, Character.Currency(3, 33, 333, 3333)),
        )

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                CharactersScreen(
                    state = CharactersContract.State.Characters(expectedCharacters),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            expectedCharacters[0].id,
                            (it as CharactersContract.Event.CharacterSelected).id
                        )

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText(expectedCharacters[0].name).performClick()
        testRule.waitUntil { onEventFinished }
    }

    @Test
    fun swipeOnCharacterTriggersDeleteEvent() {
        val expectedCharacters = listOf(
            Character(1, "one", 11, Character.Currency(1, 11, 111, 1111)),
            Character(2, "two", 22, Character.Currency(2, 22, 222, 2222)),
            Character(3, "three", 33, Character.Currency(3, 33, 333, 3333)),
        )

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                CharactersScreen(
                    state = CharactersContract.State.Characters(expectedCharacters),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            expectedCharacters[0].id,
                            (it as CharactersContract.Event.CharacterDeleted).id
                        )
                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText(expectedCharacters[0].name).performTouchInput { swipeLeft() }
        testRule.waitUntil { onEventFinished }
    }
}
