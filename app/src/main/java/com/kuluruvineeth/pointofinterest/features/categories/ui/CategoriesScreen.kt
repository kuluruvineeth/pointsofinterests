package com.kuluruvineeth.pointofinterest.features.categories.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuluruvineeth.pointofinterest.features.categories.ui.composable.CategoryTypeHeader
import com.kuluruvineeth.pointofinterest.features.categories.ui.composable.CategoryView
import com.kuluruvineeth.pointofinterest.features.categories.ui.composable.EditableCategoryView
import com.kuluruvineeth.pointofinterest.features.categories.ui.models.CategoryAction
import com.kuluruvineeth.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointofinterest.features.categories.vm.CategoriesViewModel
import com.kuluruvineeth.pointofinterest.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CategoriesScreen(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    viewModel: CategoriesViewModel,
    onNavigate: (Screen, List<Pair<String, Any>>) -> Unit
) {

    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    val itemToDelete by viewModel.itemsToDelete.collectAsStateWithLifecycle()

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        CategoriesContent(
            onNavigate = onNavigate,
            onDeleteItem = viewModel::onDeleteItem,
            onCommitDeleteItem = viewModel::onCommitDeleteItem,
            onUndoDeleteItem = viewModel::onUndoDeleteItem,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            categories = categoriesState,
            itemsToDelete = itemToDelete
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    onNavigate: (Screen, List<Pair<String, Any>>) -> Unit,
    onDeleteItem: (String) -> Unit,
    onCommitDeleteItem: (String) -> Unit,
    onUndoDeleteItem: (String) -> Unit,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    categories: Map<Int, List<CategoryUiModel>>,
    itemsToDelete: List<String>
) {

    Column {
        LazyColumn(
            Modifier
                .weight(1f)
                .testTag("categories_screen_full_list")
        ) {
            categories.entries.forEach { group ->
                stickyHeader(key = group.key) {
                    CategoryTypeHeader(type = stringResource(id = group.key))
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
                items(group.value.filter { it.id !in itemsToDelete }, { it.hashCode() }) { item ->
                    if (item.isMutableCategory) {
                        EditableCategoryView(Modifier.animateItemPlacement(), item) { action, model, displayData ->
                            when (action) {
                                CategoryAction.DELETE -> {
                                    onDeleteItem(item.id)
                                    displayData?.let { snackbarDisplayData ->
                                        coroutineScope.launch {
                                            val snackBarResult = snackbarHostState.showSnackbar(
                                                message = snackbarDisplayData.message,
                                                actionLabel = snackbarDisplayData.action,
                                                duration = SnackbarDuration.Short
                                            )
                                            when (snackBarResult) {
                                                SnackbarResult.Dismissed -> onCommitDeleteItem(model.id)
                                                SnackbarResult.ActionPerformed -> onUndoDeleteItem(model.id)
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
                    } else {
                        CategoryView(Modifier.animateItemPlacement(), item = item)
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