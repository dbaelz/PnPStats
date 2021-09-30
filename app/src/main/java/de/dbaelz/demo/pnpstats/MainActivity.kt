package de.dbaelz.demo.pnpstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
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
            Text("${Screen.OVERVIEW.displayName} Screen")
        }

        composable(Screen.EXPERIENCE.name) {
            Text("${Screen.EXPERIENCE.displayName} Screen")
        }

        composable(Screen.CURRENCY.name) {
            Text("${Screen.CURRENCY.displayName} Screen")
        }
    }
}

enum class Screen(val displayName: String) {
    OVERVIEW("Overview"),
    EXPERIENCE("Experience"),
    CURRENCY("Currency"),
    SETTINGS("Settings")
}