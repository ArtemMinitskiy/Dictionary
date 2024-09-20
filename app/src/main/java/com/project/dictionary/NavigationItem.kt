package com.project.dictionary

sealed class NavigationItem(val route: String, val name: String = "") {
    data object List : NavigationItem(Screen.LIST.name)
    data object Definition : NavigationItem(Screen.DEFINITION.name)
}