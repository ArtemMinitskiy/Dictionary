package com.project.dictionary.notifications

import com.project.dictionary.utils.Screen

sealed class NavigationItem(val route: String, val name: String = "") {
    data object List : NavigationItem(Screen.LIST.name)
    data object Definition : NavigationItem(Screen.DEFINITION.name)
    data object Settings : NavigationItem(Screen.SETTINGS.name)
}