package com.kuluruvineeth.pointsofinterests.features.home.ui


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.home.viewmodel.HomeViewModel
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.core.utils.containsId
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.CategoryFilterChips
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.PoiCard
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.AddMoreButton
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.EmptyView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ErrorView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ProgressView
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navigationController : NavHostController,
    searchState: MutableState<TextFieldValue>,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeContentState by homeViewModel.homeUiContentState.collectAsState()
    val categoriesState by homeViewModel.categoriesState.collectAsState()
    var selectedFiltersState by rememberSaveable{
        mutableStateOf<List<String>>(emptyList())
    }

    LaunchedEffect(key1 = searchState.value){
        homeViewModel.onSearch(searchState.value.text)
    }

    Column(
        modifier = Modifier
            .padding(PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = categoriesState.isEmpty().not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HomeScreenFilterContent(
                navigationController,
                selectedFilters = selectedFiltersState,
                categories = categoriesState
            ){filterId ->
                selectedFiltersState = selectedFiltersState.toMutableList().apply {
                    if(filterId in selectedFiltersState) remove(filterId)
                    else add(filterId)
                }
            }
        }
        Box(modifier = Modifier.weight(1f)){
            when(homeContentState){
                is HomeViewModel.HomeUiContentState.Loading -> ProgressView()
                HomeViewModel.HomeUiContentState.Empty -> EmptyView(
                    message = stringResource(id = R.string.message_ui_state_empty_main_screen)
                )
                is HomeViewModel.HomeUiContentState.Error -> {
                    val errorState = homeContentState as HomeViewModel.HomeUiContentState.Error
                    ErrorView(message = errorState.message){
                        homeViewModel.onRetry()
                    }
                }
                is HomeViewModel.HomeUiContentState.Result -> {
                    val filteredList = (homeContentState as HomeViewModel.HomeUiContentState.Result)
                        .poiList.filter{poi ->
                            selectedFiltersState.isEmpty() || selectedFiltersState.all {
                                filterId -> poi.categories.containsId(filterId)
                            }
                        }

                    AnimatedContent(targetState = filteredList.isEmpty()) { targetState ->
                        if (targetState) {
                            EmptyView(message = stringResource(id = R.string.message_ui_state_empty_main_screen_no_filters))
                        } else {
                            HomeScreenContent(poiItems = filteredList, navigationController = navigationController)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    poiItems: List<PoiListItem>,
    navigationController: NavHostController
) {
    Column {
        LazyColumn{
            poiItems.forEachIndexed { index, interests ->
                item(key = interests.hashCode()) {
                    PoiCard(poiListItem = interests, onClick = {
                        navigationController.navigate(Screen.CreatePoi.route)
                    })
                }
                    if(index < poiItems.size - 1){
                        item{
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
            }
        }
    }
}

@Composable
fun HomeScreenFilterContent(
    navigationController: NavHostController,
    categories: List<CategoryUiModel>,
    selectedFilters: List<String>,
    onClick: (String) -> Unit
) {
    Column {
        LazyRow{
            categories.forEach { category ->
                item(key = category.hashCode()){
                    CategoryFilterChips(
                        categoryListItem = category,
                        onClick = {onClick(category.id)},
                        isSelected = category.id in selectedFilters
                    )
                }
                item { Spacer(modifier = Modifier.width(8.dp)) }
            }
            item {
                AddMoreButton{
                    navigationController.navigate(Screen.Categories.route)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
