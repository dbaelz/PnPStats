package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CurrencyScreenTest {
    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun loadingIndicatorComposableIsShown() {
        testRule.setContent {
            MaterialTheme {
                CurrencyScreen(
                    state = CurrencyContract.State.Loading,
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading…").assertExists()
    }

    @Test
    fun currencyInfoIsShown() {
        val expectedCurrency = Character.Currency(11, 22, 33, 44)

        testRule.setContent {
            MaterialTheme {
                CurrencyScreen(
                    state = CurrencyContract.State.CurrencyInfo(1, expectedCurrency, emptyList()),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Loading...").assertDoesNotExist()
        testRule.onNodeWithText(expectedCurrency.toFormattedString()).assertExists()
            .assertIsDisplayed()

        testRule.onNode(hasTestTag("TEST_PLATINUM_TEXTFIELD")).assertExists().assertIsDisplayed()
        testRule.onNode(hasTestTag("TEST_GOLD_TEXTFIELD")).assertExists().assertIsDisplayed()
        testRule.onNode(hasTestTag("TEST_SILVER_TEXTFIELD")).assertExists().assertIsDisplayed()
        testRule.onNode(hasTestTag("TEST_COPPER_TEXTFIELD")).assertExists().assertIsDisplayed()
        testRule.onNode(hasTestTag("TEST_REASON_TEXTFIELD")).assertExists().assertIsDisplayed()

        testRule.onNodeWithText("Adjust amounts")
            .assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun currencyIsAddedCorrectly() {
        val characterId = 23
        val expectedCurrency = Character.Currency(11, 22, 33, 44)
        val expectedReason = "Test Reason"

        var onEventFinished = false

        testRule.setContent {
            MaterialTheme {
                CurrencyScreen(
                    state = CurrencyContract.State.CurrencyInfo(
                        characterId,
                        Character.Currency(),
                        emptyList()
                    ),
                    effectFlow = null,
                    onEvent = {
                        assertEquals(
                            characterId, (it as CurrencyContract.Event.AddCurrency).characterId
                        )
                        assertEquals(expectedCurrency, it.currency)
                        assertEquals(expectedReason, it.reason)

                        onEventFinished = true
                    },
                    onNavigation = {}
                )
            }
        }

        testRule.onNode(hasTestTag("TEST_PLATINUM_TEXTFIELD"))
            .performTextInput(expectedCurrency.platinum.toString())
        testRule.onNode(hasTestTag("TEST_GOLD_TEXTFIELD"))
            .performTextInput(expectedCurrency.gold.toString())
        testRule.onNode(hasTestTag("TEST_SILVER_TEXTFIELD"))
            .performTextInput(expectedCurrency.silver.toString())
        testRule.onNode(hasTestTag("TEST_COPPER_TEXTFIELD"))
            .performTextInput(expectedCurrency.copper.toString())
        testRule.onNode(hasTestTag("TEST_REASON_TEXTFIELD"))
            .performTextInput(expectedReason)

        testRule.onNodeWithText("Adjust amounts").performClick()

        testRule.waitUntil { onEventFinished }
    }

    @Test
    fun currencyDetailsAreShown() {
        val currencyDetails = listOf(
            Character.Currency(-1, -1, -3, -4) to "REMOVE",
            Character.Currency(11, 22, 33, 44) to "",
            Character.Currency(0, 0, 0, 0) to "",
            Character.Currency(111, 222, 333, 444) to "ADD",
        )

        testRule.setContent {
            MaterialTheme {
                CurrencyScreen(
                    state = CurrencyContract.State.CurrencyInfo(
                        1,
                        Character.Currency(),
                        currencyDetails
                    ),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Currency Log").assertExists()
    }

    @Test
    fun currencyDetailsAreNotShownWhenEmpty() {
        val currencyDetails = emptyList<Pair<Character.Currency, String>>()

        testRule.setContent {
            MaterialTheme {
                CurrencyScreen(
                    state = CurrencyContract.State.CurrencyInfo(
                        1,
                        Character.Currency(),
                        currencyDetails
                    ),
                    effectFlow = null,
                    onEvent = {},
                    onNavigation = {}
                )
            }
        }

        testRule.onNodeWithText("Currency Log").assertDoesNotExist()
    }
}