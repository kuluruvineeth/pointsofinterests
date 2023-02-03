package com.kuluruvineeth.pointsofinterests.features.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.viewmodel.CategoriesViewModel
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ProgressView
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.features.categories.ui.composable.GridColorPalette
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.DetailedCategoriesUiState
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PrimaryButton

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDetailedScreen(
    categoryId: String?,
    viewModel: CategoriesViewModel,
    onBack: () -> Unit
) {

    val uiState by viewModel.detailedCategoriesUiState.collectAsState()
    var textFieldState by remember { mutableStateOf(TextFieldValue(""))}
    var selectedColorState by remember { mutableStateOf(Color.Transparent)}
    val focusRequester = remember {
        FocusRequester()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(categoryId){
        viewModel.onFetchDetailedState(categoryId)
    }

    LaunchedEffect(key1 = uiState){
        if(uiState is DetailedCategoriesUiState.Success){
            val categoryName = (uiState as DetailedCategoriesUiState.Success).categoryUiModel?.title ?: ""
            textFieldState = textFieldState.copy(
                categoryName,
                selection = TextRange(categoryName.length)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        when(uiState){
            is DetailedCategoriesUiState.Loading -> ProgressView()
            is DetailedCategoriesUiState.Success -> {
                val categoryModel = (uiState as DetailedCategoriesUiState.Success).categoryUiModel
                CategoriesDetailedContent(
                    selectedCategory = categoryModel,
                    selectedColor = selectedColorState,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController,
                    textFieldValue = textFieldState,
                    onTextChanged = { textFieldState = it},
                    onColorSelected = {selectedColorState = it},
                    onSave = {name, color ->
                        viewModel.onCreateItem(name,color)
                        onBack()
                    },
                    onUpdate = {item, name, color ->
                        viewModel.onUpdateItem(item,name,color)
                        onBack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDetailedContent(
    selectedCategory: CategoryUiModel? = null,
    selectedColor: Color,
    textFieldValue: TextFieldValue,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onTextChanged: (TextFieldValue) -> Unit,
    onColorSelected: (Color) -> Unit,
    onSave: (String, Color) -> Unit,
    onUpdate: (CategoryUiModel, String, Color) -> Unit
) {
    LaunchedEffect(key1 = true){
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = {value -> onTextChanged(value)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.bodyLarge,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_hash_tag),
                        contentDescription = "",
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                trailingIcon = {
                    if(textFieldValue.text.isNotEmpty()){
                        IconButton(onClick = {
                            onTextChanged(TextFieldValue(""))
                        }) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = CircleShape
                                    ),
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                },
                singleLine = true,
                maxLines = 1,
                shape = RoundedCornerShape(8.dp),
                label = { Text(text = stringResource(id = R.string.title_category_name))},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    keyboardController?.hide()
                })
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
            )

            GridColorPalette(
                modifier = Modifier.padding(16.dp),
                onColorSelected = onColorSelected,
                originalColor = selectedCategory?.color,
                selectedColor = selectedColor
            )
        }

        val isEnabled = if (selectedCategory != null){
            (textFieldValue.text != selectedCategory.title || selectedColor != selectedCategory.color) && textFieldValue.text.isNotEmpty()
        }else{
            textFieldValue.text.isNotEmpty() && selectedColor != Color.Transparent
        }

        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            shadowElevation = 8.dp
        ) {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = if(selectedCategory != null)
                    stringResource(id = R.string.button_update)
                else stringResource(id = R.string.button_save),
                enabled = isEnabled,
                onClick = {
                    if(selectedCategory != null){
                        onUpdate(selectedCategory, textFieldValue.text,selectedColor)
                    }else{
                        onSave(textFieldValue.text, selectedColor)
                    }
                }
            )
        }
    }
}