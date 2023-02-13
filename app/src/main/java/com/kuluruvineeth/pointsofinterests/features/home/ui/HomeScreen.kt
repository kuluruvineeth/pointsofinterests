package com.kuluruvineeth.pointsofinterests.features.home.ui


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.home.viewmodel.HomeViewModel
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.core.utils.containsId
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.CategoryFilterChips
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.PoiCard
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.AddMoreButton
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiSortByUiOption
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toSubTitle
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toTitle
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.ActionButton
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.EmptyView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ErrorView
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ProgressView
import kotlinx.coroutines.flow.MutableStateFlow
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.ActionButton


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    showSortDialogState: Boolean,
    onCloseSortDialog: () -> Unit,
    onNavigate: (Screen, List<Pair<String, Any>>) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeContentState by homeViewModel.homeUiContentState.collectAsStateWithLifecycle()
    val categoriesState by homeViewModel.categoriesState.collectAsStateWithLifecycle()

    val selectedSortByOption by
        homeViewModel.displaySortOptionUiState.collectAsStateWithLifecycle()
    val selectedFiltersState = remember {
        mutableStateListOf<String>()
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = categoriesState.isEmpty().not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HomeScreenFilterContent(
                onAddCategories = {onNavigate(Screen.Categories, emptyList())},
                selectedFilters = selectedFiltersState,
                categories = categoriesState
            ){filterId ->
                if(filterId in selectedFiltersState)
                    selectedFiltersState.remove(filterId)
                else selectedFiltersState.add(filterId)
            }
        }
        Box(
            modifier = Modifier
                .padding(PaddingValues(start = 16.dp, end = 16.dp))
                .weight(1f)){
            when(homeContentState){
                is HomeViewModel.HomeUiContentState.Loading -> ProgressView()
                is HomeViewModel.HomeUiContentState.Empty -> EmptyView(
                    message = stringResource(id = R.string.message_ui_state_empty_main_screen)
                )
                is HomeViewModel.HomeUiContentState.Error -> {
                    val errorState = homeContentState as HomeViewModel.HomeUiContentState.Error
                    ErrorView(displayObject = errorState.displayObject){
                        homeViewModel.onRetry()
                    }
                }
                else -> {
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
                            HomeScreenContent(poiItems = filteredList){id ->
                                onNavigate(
                                    Screen.ViewPoiDetailed,
                                    listOf(Screen.ViewPoiDetailed.ARG_POI_ID to id)
                                )
                            }
                        }
                    }

                }
            }
            if(showSortDialogState){
                Dialog(
                    onDismissRequest = onCloseSortDialog,
                    content = {
                        Card(
                            modifier = Modifier.fillMaxWidth(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Column(
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.3f
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        end = 24.dp,
                                        top = 24.dp,
                                        start = 24.dp,
                                        bottom = 8.dp
                                    ),
                                    text = stringResource(id = R.string.title_dialog_sort_by),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                PoiSortByUiOption.values().forEach{option ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                homeViewModel.onApplySortBy(option)
                                                onCloseSortDialog()
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        RadioButton(
                                            modifier = Modifier.padding(
                                                vertical = 8.dp, horizontal = 24.dp
                                            ),
                                            selected = (option == selectedSortByOption),
                                            onClick = null,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colorScheme.secondary,
                                                unselectedColor = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.2f
                                                )
                                            )
                                        )
                                        Column(
                                            modifier = Modifier.padding(
                                                end = 24.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(id = option.toTitle()),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                            Text(
                                                text = stringResource(id = option.toSubTitle()),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.size(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 24.dp,
                                            end = 8.dp,
                                            bottom = 8.dp
                                        ),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ActionButton(
                                        text = stringResource(id = R.string.cancel),
                                        containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                            alpha = 0f
                                        ),
                                        onClick = {onCloseSortDialog()}
                                    )
                                }
                            }
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    poiItems: List<PoiListItem>,
    onPoiSelected: (String) -> Unit
) {
    LazyColumn{
        items(poiItems, key = {item -> item.id}){item ->
            Column(modifier = Modifier.animateItemPlacement()) {
                PoiCard(poiListItem = item, onClick = onPoiSelected)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun HomeScreenFilterContent(
    onAddCategories: () -> Unit,
    categories: List<CategoryUiModel>,
    selectedFilters: List<String>,
    onClick: (String) -> Unit
) {
    Column {
        LazyRow{
            itemsIndexed(categories, key = {_,item -> item.id}){index,item ->
                if(index==0) Spacer(modifier = Modifier.width(16.dp))
                CategoryFilterChips(
                    categoryListItem = item,
                    onClick = onClick,
                    isSelected = item.id in selectedFilters
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            item(key = "AddMoreButton") {
                AddMoreButton(
                    onClick = onAddCategories
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
