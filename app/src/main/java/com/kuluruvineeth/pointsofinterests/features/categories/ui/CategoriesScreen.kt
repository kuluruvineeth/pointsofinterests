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
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryTypeHeader
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryView
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryAction
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.viewmodel.CategoriesViewModel
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PrimaryButton
import com.kuluruvineeth.pointsofinterests.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoriesState by categoriesViewModel.categoriesState.collectAsState()
    val itemsToDelete by categoriesViewModel.itemsToDelete.collectAsState()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        CategoriesContent(
            viewModel = categoriesViewModel,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            navigationController = navHostController,
            categories = categoriesState,
            itemsToDelete = itemsToDelete
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    viewModel: CategoriesViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navigationController: NavHostController,
    categories: Map<String,List<CategoryUiModel>>,
    itemsToDelete: List<String>
) {
    val context = LocalContext.current

    Column {
        LazyColumn(Modifier.weight(1f)){
            categories.entries.forEach { group ->
                stickyHeader {
                    CategoryTypeHeader(type = group.key)
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    )
                }
                items(group.value.filter{it.id !in itemsToDelete}, {it.hashCode()}){category ->
                    CategoryView(Modifier.animateItemPlacement(),category){action,model, displayData ->
                        when(action){
                            CategoryAction.DELETE -> {
                                viewModel.onDeleteItem(model.id)
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
                            CategoryAction.EDIT -> Toast.makeText(context,"Edit ${model.id}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            PrimaryButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                text = stringResource(id = R.string.button_create_new),
                onClick = {
                    Toast.makeText(context,"Create new category",Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}