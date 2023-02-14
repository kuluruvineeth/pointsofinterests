package com.kuluruvineeth.pointsofinterests.ui.composables.uikit


import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuluruvineeth.pointsofinterests.features.main.PoiAppState
import com.kuluruvineeth.pointsofinterests.navigation.*
import com.kuluruvineeth.pointsofinterests.ui.theme.DarkMainColor


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AppBar(
    title: String,
    appState: PoiAppState
) {
    val screenTitle = appState.currentScreen?.name?.let { stringResource(id = it) } ?: title

    val focusRequester = remember {
        FocusRequester()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = appState.showSearchBarState){
        if(appState.showSearchBarState) focusRequester.requestFocus()
        else keyboardController?.hide()
    }

    AnimatedContent(targetState = appState.showSearchBarState) {targetState ->
        if(targetState){
            SearchView(
                appState = appState,
                state = appState.searchState.value,
                focusRequester = focusRequester,
                keyboardController = keyboardController
            ){updateState ->
                appState.searchState.value = updateState
            }
        }else{
            TopAppBar(
                title = {
                    Text(text = screenTitle, color = MaterialTheme.colorScheme.onBackground)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    if(appState.navHostController.previousBackStackEntry != null && !appState.isRootScreen){
                        with(MenuItem.Back){
                            TopAppBarAction(
                                actionType = action,
                                imageVector = ImageVector.vectorResource(id = icon),
                                contentDescription = action.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                action = {appState.onBackClick()}
                            )
                        }
                    }
                },
                actions = {
                    appState.currentScreen?.menuItems?.forEach { 
                        TopAppBarAction(
                            actionType = it.action,
                            imageVector = ImageVector.vectorResource(id = it.icon),
                            contentDescription = it.action.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            action = {action ->
                                appState.onMenuItemClicked(action)
                            }
                        )
                    }
                }
            )
        }
        
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    appState: PoiAppState,
    state: TextFieldValue,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onTextChanged: (TextFieldValue) -> Unit
) {
    TextField(
        value = state, 
        onValueChange = {value ->
            onTextChanged(value)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.titleSmall,
        leadingIcon = {
            IconButton(onClick = {
                appState.showSearchBarState = false
                appState.onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        placeholder = { Text(text = "Search...")},
        trailingIcon = {
            if(state.text.isNotEmpty()){
                IconButton(onClick = { onTextChanged(TextFieldValue(""))}) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            ),
            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.1f
            ),
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.1f
            ),
            disabledIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.1f
            ),
            placeholderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            )
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onTextChanged(state)
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun TopAppBarAction(
    actionType: MenuActionType,
    imageVector: ImageVector,
    contentDescription: String,
    color: Color,
    action: (MenuActionType) -> Unit
) {
    IconButton(
        onClick = { action(actionType) }) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color
        )
    }
}