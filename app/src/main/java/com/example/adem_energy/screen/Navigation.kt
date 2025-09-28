package com.example.adem_energy.screen

import SplashScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.AdemEnergyLoginScreen.route) { AdemEnergyLoginScreen(navController) }
        composable(Screen.SubscriptionScreen.route) { SubscriptionScreen(navController) }
        composable(Screen.Subscription.route) { Subscription(navController) }
        composable(Screen.SignUpScreen.route) { SignUpScreen(navController) }
        composable(Screen.HealingScreen.route) { HealingScreen(navController) }
        composable(Screen.ImprintScreen.route) { ImprintScreen(navController) }
        composable(Screen.AccountScreen.route) { AccountScreen(navController) }
        composable(Screen.LibraryScreen.route) { LibraryScreen(navController) }
        composable(Screen.RemedyScreen.route) { RemedyScreen(navController) }
        composable(Screen.ProgrammeScreen.route) { ProgrammeScreen(navController) }
        composable(Screen.SpectroChromeScreen.route) { SpectroChromeScreen(navController) }

    }
}

sealed class Screen(val route: String, val title: String,val icon: ImageVector) {
    object SplashScreen : Screen("SplashScreen", "SplashScreen", icon = Icons.Default.Person)
    object AdemEnergyLoginScreen : Screen("AdemEnergyLoginScreen", "AdemEnergyLoginScreen", icon = Icons.Default.Person)
    object SubscriptionScreen : Screen("SubscriptionScreen", "SubscriptionScreen", icon = Icons.Default.Person)
    object Subscription : Screen("Subscription", "Subscription", icon = Icons.Default.Person)
    object SignUpScreen : Screen("SignUpScreen", "SignUpScreen", icon = Icons.Default.Person)
    object HealingScreen : Screen("HealingScreen", "HealingScreen", icon = Icons.Default.Person)
    object ImprintScreen : Screen("ImprintScreen", "ImprintScreen", icon = Icons.Default.Person)
    object AccountScreen : Screen("AccountScreen", "AccountScreen", icon = Icons.Default.Person)
    object LibraryScreen : Screen("LibraryScreen", "LibraryScreen", icon = Icons.Default.Person)
    object RemedyScreen : Screen("RemedyScreen", "Remedy", icon = Icons.Default.BrightnessMedium)
    object ProgrammeScreen : Screen("ProgrammeScreen", "Programme", icon = Icons.Default.Bookmarks)
    object SpectroChromeScreen : Screen("SpectroChromeScreen", "SpectroChrome", icon = Icons.Default.Palette)
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val purpleDark = Color(0xFF3F2A5F)
    val lightPurple = Color(0xFFEDE7F6)
    val greyText = Color(0xFFB0B0B0)
    val navBarBackground = Color(0xFFF8F5FF)

    val items = listOf(
        Screen.RemedyScreen,
        Screen.ProgrammeScreen,
        Screen.SpectroChromeScreen
    )

    NavigationBar(
        containerColor = navBarBackground,
        tonalElevation = 6.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp),
                        tint = if (isSelected) purpleDark else greyText
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        color = if (isSelected) purpleDark else greyText,
                        style = if (isSelected) {
                            MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal
                            )
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = purpleDark,
                    selectedTextColor = purpleDark,
                    unselectedIconColor = greyText,
                    unselectedTextColor = greyText,
                    indicatorColor = lightPurple // Use your consistent light purple
                )
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navController = rememberNavController()
    var showBottomNav by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    showBottomNav = when (currentRoute) {
        Screen.SplashScreen.route,
        Screen.AdemEnergyLoginScreen.route,
        Screen.SubscriptionScreen.route,
        Screen.Subscription.route,
        Screen.SignUpScreen.route,
        Screen.HealingScreen.route,
        Screen.ImprintScreen.route,
        Screen.AccountScreen.route,
        Screen.LibraryScreen.route -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigation(navController = navController)
            }
        },
        containerColor = Color(0xFFFDFDFE)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController = navController)
        }
    }
}
