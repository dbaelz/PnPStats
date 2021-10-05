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
                val route = backstackEntry.value?.destination?.route

                Scaffold(
                    topBar = {
                        val title =
                            route?.let { Screen.valueOf(it).displayName }
                                ?: Screen.OVERVIEW.displayName
                        TopBar(title, route != Screen.OVERVIEW.name) {
                            navController.popBackStack()
                        }
                    },
                    bottomBar = {
                        val currentScreen = route?.let { Screen.valueOf(it) } ?: Screen.OVERVIEW

                        BottomBar(
                            navController,
                            currentScreen,
                            listOf(Screen.OVERVIEW, Screen.EXPERIENCE, Screen.EXPERIENCE)
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
private fun TopBar(title: String, withBackNavigation: Boolean, onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = if (withBackNavigation) {
            {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        } else {
            null
        }
    )
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
                onClick = {

                })
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
        startDestination = Screen.OVERVIEW.name,
        modifier = modifier
    ) {
        composable(Screen.OVERVIEW.name) {
            OverviewDestination(navController)
        }

        composable(Screen.EXPERIENCE.name) {
            Text("${Screen.EXPERIENCE.displayName} Screen")
        }

        composable(Screen.CURRENCY.name) {
            Text("${Screen.CURRENCY.displayName} Screen")
        }

        composable(Screen.SETTINGS.name) {
            Text("${Screen.SETTINGS.displayName} Screen")
        }
    }
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
                is OverviewContract.Effect.Navigation.ToCharacterOverview -> {
                    navController.navigate(Screen.OVERVIEW.name)
                }
            }
        }

    )
}

private enum class Screen(val displayName: String, val icon: ImageVector) {
    OVERVIEW("Overview", Icons.Default.Home),
    EXPERIENCE("Experience", Icons.Default.Info),
    CURRENCY("Currency", Icons.Default.AccountBox),
    SETTINGS("Settings", Icons.Default.Settings),
}