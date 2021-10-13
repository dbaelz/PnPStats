package de.dbaelz.demo.pnpstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import de.dbaelz.demo.pnpstats.ui.feature.characters.CharactersContract
import de.dbaelz.demo.pnpstats.ui.feature.characters.CharactersScreen
import de.dbaelz.demo.pnpstats.ui.feature.characters.CharactersViewModel
import de.dbaelz.demo.pnpstats.ui.feature.createcharacter.CreateCharacterScreen
import de.dbaelz.demo.pnpstats.ui.feature.createcharacter.CreateCharacterViewModel
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyScreen
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyViewModel
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceContract
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceScreen
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceViewModel
import de.dbaelz.demo.pnpstats.ui.feature.overview.OverviewContract
import de.dbaelz.demo.pnpstats.ui.feature.overview.OverviewScreen
import de.dbaelz.demo.pnpstats.ui.feature.overview.OverviewViewModel
import de.dbaelz.demo.pnpstats.ui.theme.PnPStatsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PnPStatsTheme {
                val navController = rememberNavController()
                val backstackEntry = navController.currentBackStackEntryAsState()
                val currentScreen = backstackEntry.value?.destination?.route?.let {
                    Screen.valueOf(it)
                } ?: Screen.CHARACTERS

                Scaffold(
                    topBar = {
                        val showCharactersAction =
                            currentScreen != Screen.CHARACTERS && currentScreen != Screen.CREATE_CHARACTER

                        TopBar(currentScreen.displayName, showCharactersAction) {
                            navController.navigate(Screen.CHARACTERS.route)
                        }
                    },
                    floatingActionButton = {
                        CreateCharacterActionButton(currentScreen) {
                            navController.navigate(Screen.CREATE_CHARACTER.route)
                        }
                    },
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            currentScreen = currentScreen,
                            items = listOf(
                                Screen.OVERVIEW,
                                Screen.EXPERIENCE,
                                Screen.CURRENCY,
                                Screen.SETTINGS
                            )
                        )
                    },
                    content = {
                        PnPStatsNavHost(navController, Modifier.padding(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    withCharactersAction: Boolean,
    onActionButtonClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        actions = if (withCharactersAction) {
            {
                IconButton(onClick = onActionButtonClicked) {
                    Icon(Screen.CHARACTERS.icon, null)
                }
            }
        } else {
            {}
        }
    )
}

@Composable
private fun CreateCharacterActionButton(currentScreen: Screen, onActionButtonClicked: () -> Unit) {
    if (currentScreen == Screen.CHARACTERS) {
        FloatingActionButton(onClick = onActionButtonClicked) {
            Icon(Icons.Default.Add, null)
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    currentScreen: Screen,
    items: List<Screen>
) {
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, null) },
                selected = screen == currentScreen,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

@Composable
private fun PnPStatsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.OVERVIEW.route,
        modifier = modifier
    ) {
        composable(Screen.CHARACTERS.route) {
            CharactersDestination(navController)
        }

        composable(Screen.CREATE_CHARACTER.route) {
            CreateCharacterDestination(navController)
        }

        composable(Screen.OVERVIEW.route) {
            OverviewDestination(navController)
        }

        composable(Screen.EXPERIENCE.route) {
            ExperienceDestination(navController)
        }

        composable(Screen.CURRENCY.route) {
            CurrencyDestination(navController)
        }

        composable(Screen.SETTINGS.route) {
            Text("${Screen.SETTINGS.displayName} Screen")
        }
    }
}

@Composable
private fun CharactersDestination(navController: NavHostController) {
    val viewModel: CharactersViewModel = hiltViewModel()
    CharactersScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEvent = { viewModel.processEvent(it) },
        onNavigation = { navigation ->
            when (navigation) {
                is CharactersContract.Effect.Navigation.ToOverview -> {
                    navController.navigate(Screen.OVERVIEW.route)
                }
            }
        }
    )
}

@Composable
private fun CreateCharacterDestination(navController: NavHostController) {
    val viewModel: CreateCharacterViewModel = hiltViewModel()
    CreateCharacterScreen(
        effectFlow = viewModel.effect,
        onEvent = { viewModel.processEvent(it) },
        onNavigation = { navController.navigate(Screen.CHARACTERS.route) }
    )
}

@Composable
private fun OverviewDestination(navController: NavHostController) {
    val viewModel: OverviewViewModel = hiltViewModel()
    OverviewScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEvent = { viewModel.processEvent(it) },
        onNavigation = { navigation ->
            when (navigation) {
                OverviewContract.Effect.Navigation.ToCharacters -> {
                    navController.navigate(Screen.CHARACTERS.route)
                }
                is OverviewContract.Effect.Navigation.ToExperience -> {
                    navController.navigate(Screen.EXPERIENCE.route)
                }
                is OverviewContract.Effect.Navigation.ToCurrency -> {
                    navController.navigate(Screen.CURRENCY.route)
                }
            }
        }
    )
}

@Composable
private fun ExperienceDestination(navController: NavHostController) {
    val viewModel: ExperienceViewModel = hiltViewModel()
    ExperienceScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEvent = { viewModel.processEvent(it) },
        onNavigation = { navigation ->
            when (navigation) {
                ExperienceContract.Effect.Navigation.ToCharacters -> navController.navigate(Screen.CHARACTERS.route)
            }
        }
    )
}

@Composable
private fun CurrencyDestination(navController: NavHostController) {
    val viewModel: CurrencyViewModel = hiltViewModel()
    CurrencyScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEvent = { viewModel.processEvent(it) },
        onNavigation = { navigation ->
            when (navigation) {
                CurrencyContract.Effect.Navigation.ToCharacters -> navController.navigate(Screen.CHARACTERS.route)
            }
        }
    )
}

private enum class Screen(val displayName: String, val icon: ImageVector) {
    CHARACTERS("Characters", Icons.Default.Person),
    CREATE_CHARACTER("Create Character", Icons.Default.Person),
    OVERVIEW("Overview", Icons.Default.Home),
    EXPERIENCE("Experience", Icons.Default.Info),
    CURRENCY("Currency", Icons.Default.AccountBox),
    SETTINGS("Settings", Icons.Default.Settings);

    val route = name
}