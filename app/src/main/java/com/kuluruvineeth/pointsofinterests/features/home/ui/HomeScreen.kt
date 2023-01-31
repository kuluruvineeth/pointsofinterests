package com.kuluruvineeth.pointsofinterests.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.core.models.UiState
import com.kuluruvineeth.pointsofinterests.features.home.viewmodel.HomeViewModel
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.EmptyView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ErrorView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ProgressView

@Composable
fun HomeScreen(
    navigationController : NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit){
        homeViewModel.onLaunched()
    }

    when(uiState){
        UiState.Loading -> ProgressView()
        UiState.Empty -> EmptyView(message = stringResource(id = R.string.message_ui_state_empty_main_screen))
        is UiState.Error -> {
            val errorState = uiState as UiState.Error
            ErrorView(message = errorState.message){
                homeViewModel.onRetry()
            }
        }
        else -> HomeScreenContent()
    }
}

@Composable
fun HomeScreenContent() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ){
        Text(
            text = "Content",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}