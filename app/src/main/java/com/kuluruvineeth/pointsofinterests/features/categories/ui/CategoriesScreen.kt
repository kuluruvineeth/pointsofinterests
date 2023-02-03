package com.kuluruvineeth.pointsofinterests.features.categories.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    categories: Map<String,List<CategoryUiModel>>
) {
    val context = LocalContext.current

    Column {
        LazyColumn(Modifier.weight(1f)){
            categories.entries.forEach { group ->
                stickyHeader {
                    CategoryTypeHeader(type = group.key)
                }
                items(group.value, {it.hashCode()}){category ->
                    CategoryView(Modifier.animateItemPlacement(),category){action,model ->
                        when(action){
                            CategoryAction.DELETE -> viewModel.onDeleteItem(model.id)
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