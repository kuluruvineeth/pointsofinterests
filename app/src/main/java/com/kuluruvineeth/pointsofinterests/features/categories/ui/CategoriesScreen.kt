package com.kuluruvineeth.pointsofinterests.features.categories.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryTypeHeader
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.CategoryView
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryAction
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.viewmodel.CategoriesViewModel


@Composable
fun CategoriesScreen(
    navHostController: NavHostController,
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoriesState by categoriesViewModel.categoriesState.collectAsState()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        CategoriesContent(
            viewModel = categoriesViewModel,
            navigationController = navHostController,
            categories = categoriesState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    viewModel: CategoriesViewModel,
    navigationController: NavHostController,
    categories: List<CategoryUiModel>
) {
    val context = LocalContext.current

    Column {
        LazyColumn{
            stickyHeader {
                CategoryTypeHeader(type = "Test Header")
            }
            categories.forEach { category ->
                item(key = category.hashCode()){
                    CategoryView(Modifier.animateItemPlacement(),category){action,model ->
                        when(action){
                            CategoryAction.DELETE -> viewModel.onDeleteItem(model.id)
                            CategoryAction.EDIT -> Toast.makeText(context,"Edit ${model.id}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                item("Divider${category.id}"){
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}