package com.kuluruvineeth.pointsofinterests.features.categories.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryTypeHeader
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryView
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryAction
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.viewmodel.CategoriesViewModel
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PrimaryButton
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.EditableCategoryView
import com.kuluruvineeth.pointsofinterests.features.main.PoiAppState
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CategoriesScreen(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    onNavigate: (Screen, List<Pair<String,Any>>) -> Unit
) {
    val categoriesState by categoriesViewModel.categoriesState.collectAsStateWithLifecycle()
    val itemsToDelete by categoriesViewModel.itemsToDelete.collectAsStateWithLifecycle()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        CategoriesContent(
            viewModel = categoriesViewModel,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            onNavigate = onNavigate,
            categories = categoriesState,
            itemsToDelete = itemsToDelete
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    onNavigate: (Screen, List<Pair<String,Any>>) -> Unit,
    viewModel: CategoriesViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    categories: Map<Int,List<CategoryUiModel>>,
    itemsToDelete: List<String>
) {

    Column {
        LazyColumn(Modifier.weight(1f)){
            categories.entries.forEach { group ->
                stickyHeader(key = group.key) {
                    CategoryTypeHeader(type = stringResource(id = group.key))
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    )
                }
                items(group.value.filter{it.id !in itemsToDelete}, {it.hashCode()}){category ->
                    if(category.isMutableCategory){
                        EditableCategoryView(Modifier.animateItemPlacement(),category){action,model, displayData ->
                            when(action){
                                CategoryAction.DELETE -> {
                                    viewModel.onDeleteItem(category.id)
                                    displayData?.let{snackbarDisplayData ->
                                        coroutineScope.launch {
                                            val snackBarResult = snackbarHostState.showSnackbar(
                                                message = snackbarDisplayData.message,
                                                actionLabel = snackbarDisplayData.action,
                                                duration = SnackbarDuration.Short
                                            )
                                            when(snackBarResult){
                                                SnackbarResult.Dismissed -> {
                                                    viewModel.onCommitDeleteItem(model.id)
                                                }
                                                SnackbarResult.ActionPerformed -> {
                                                    viewModel.onUndoDeleteItem(model.id)
                                                }
                                            }
                                        }
                                    }
                                }
                                CategoryAction.EDIT -> onNavigate(
                                    Screen.CategoriesDetailed,
                                    listOf(Screen.CategoriesDetailed.ARG_CATEGORY_ID to model.id)
                                )
                            }
                        }
                    }else{
                        CategoryView(Modifier.animateItemPlacement(), itemModel = category)
                    }
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}