package com.kuluruvineeth.pointsofinterests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.AppBar
import com.kuluruvineeth.pointsofinterests.ui.theme.PointsOfInterestTheme
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.navigation.Navigation
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.BottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            PointsOfInterestTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        AppBar(
                            title = stringResource(id = R.string.app_name),
                            navController
                        )
                    },
                    bottomBar = {
                        BottomBar(navHostController = navController, items = getMainScreens())
                    }
                ) {paddingValues ->
                    Navigation(navHostController = navController, paddingValues = paddingValues)
                }
            }
        }
    }
}
