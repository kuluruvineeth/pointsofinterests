package com.kuluruvineeth.pointsofinterests.features.poi.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.poi.viewmodel.PoiViewModel


@Composable
fun CreatePoiScreen(
    navHostController: NavHostController,
    viewModel: PoiViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "This is Create article screen",
            Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CreatePoiWizardScreen() {
    
}