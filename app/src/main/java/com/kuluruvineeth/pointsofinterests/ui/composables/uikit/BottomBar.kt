package com.kuluruvineeth.pointsofinterests.ui.composables.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.ui.theme.OrangeMain
import com.kuluruvineeth.pointsofinterests.ui.theme.UnselectedColor


@Composable
fun BottomBar(
    navHostController: NavHostController,
    currentDestination: NavDestination?,
    items: List<Screen>
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.background,
        cutoutShape = CircleShape,
        elevation = 8.dp
    ) {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any{it.route == screen.route} == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = requireNotNull(screen.icon)),
                        contentDescription = stringResource(id = screen.name),
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = screen.name),
                        style = MaterialTheme.typography.labelSmall,
                        color = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                selected = isSelected,
                onClick = {
                    navHostController.navigate(screen.route){
                        popUpTo(navHostController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}