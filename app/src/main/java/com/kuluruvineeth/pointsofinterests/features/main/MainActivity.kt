package com.kuluruvineeth.pointsofinterests.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material3.*
import androidx.compose.material.Scaffold
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.AppBar
import com.kuluruvineeth.pointsofinterests.ui.theme.PointsOfInterestTheme
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.navigation.Navigation
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.navigation.routeToScreen
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.BottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointsOfInterestTheme {
                // A surface container using the 'background' color from the theme
                PoiMainScreen()
            }
        }
    }
}