package com.kuluruvineeth.pointsofinterests.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.AppBar
import com.kuluruvineeth.pointsofinterests.ui.theme.PointsOfInterestTheme
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.features.main.viewmodel.MainScreenState
import com.kuluruvineeth.pointsofinterests.features.main.viewmodel.MainViewModel
import com.kuluruvineeth.pointsofinterests.features.main.viewmodel.SyncStateState
import com.kuluruvineeth.pointsofinterests.navigation.Navigation
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.navigation.routeToScreen
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.BottomBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var navHostController: NavHostController? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var mainState: MainScreenState by mutableStateOf(MainScreenState.Loading)
        //Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.mainScreenState
                    .onEach{
                        mainState = it
                    }
                    .collect()
            }
        }
        splashScreen.setKeepOnScreenCondition{
            mainViewModel.syncState.value == SyncStateState.Loading && mainState == MainScreenState.Loading
        }
        lifecycle.addObserver(mainViewModel)
        setContent {
            PointsOfInterestTheme(
                useSystemTheme = mainState.userSettings()?.isUseSystemTheme ?: true,
                darkTheme = mainState.userSettings()?.isDarkMode ?: true
            ) {
                navHostController = rememberNavController()
                val appState = rememberPoiAppState(
                    navHostController = requireNotNull(navHostController)
                )
                PoiMainScreen(appState)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostController?.handleDeepLink(intent)
    }

    private fun MainScreenState.userSettings() = if(this is MainScreenState.Result) userSettings else null
}
