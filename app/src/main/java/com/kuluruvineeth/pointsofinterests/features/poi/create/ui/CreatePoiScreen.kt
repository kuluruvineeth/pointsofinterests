package com.kuluruvineeth.pointsofinterests.features.poi.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuluruvineeth.pointsofinterests.core.validator.rememberUrlValidator
import com.kuluruvineeth.pointsofinterests.ui.composables.uistates.ProgressView
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.features.poi.create.models.WizardSuggestionUiModel
import com.kuluruvineeth.pointsofinterests.features.poi.create.ui.composable.WizardSuggestionStateCard
import com.kuluruvineeth.pointsofinterests.features.poi.create.viewmodel.CreatePoiScreenState
import com.kuluruvineeth.pointsofinterests.features.poi.create.viewmodel.CreatePoiViewModel
import com.kuluruvineeth.pointsofinterests.features.poi.create.viewmodel.WizardSuggestionUiState
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.ActionButton
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.CrossSlide
import kotlinx.coroutines.flow.collect


@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun CreatePoiScreen(
    navHostController: NavHostController,
    viewModel: CreatePoiViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true){
        viewModel.sharedContentState.collect()
    }

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()


    val focusRequester = remember {
        FocusRequester()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    if(screenState.value !is CreatePoiScreenState.Loading){
        CrossSlide(targetState = screenState.value) {state ->
            if(state is CreatePoiScreenState.Wizard){
                CreatePoiWizardScreen(
                    sharedContent = state.sharedContent.content,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController
                )
            }else if(state is CreatePoiScreenState.Form){
                CreatePoiFormScreen(
                    wizardSuggestionUiModel = state.suggestion,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun CreatePoiWizardScreen(
    sharedContent: String?,
    viewModel: CreatePoiViewModel,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {

    LaunchedEffect(key1 = true){
        viewModel.searchState.collect()
    }

    var wizardTextState by remember {
        mutableStateOf(TextFieldValue(sharedContent ?: ""))
    }
    val wizardSuggestionUiState by viewModel.wizardSuggestionState.collectAsStateWithLifecycle()
    val urlValidator = rememberUrlValidator()

    LaunchedEffect(key1 = sharedContent){
        if(sharedContent != null && urlValidator.validate(sharedContent)){
            viewModel.onFetchWizardSuggestion(sharedContent)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_wizard),
                contentDescription = "Wizard icon",
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            androidx.compose.material3.OutlinedTextField(
                value = wizardTextState,
                onValueChange = {value ->
                    wizardTextState = value
                    if(urlValidator.validate(value.text)){
                        viewModel.onFetchWizardSuggestion(value.text)
                    }
                },
                isError = urlValidator.isValid.not(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.bodyLarge,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_insert_link),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                trailingIcon = {
                    if(wizardTextState.text.isNotEmpty()){
                        IconButton(
                            onClick = {
                                wizardTextState = TextFieldValue("")
                                urlValidator.validate(wizardTextState.text)
                                viewModel.onFetchWizardSuggestion(wizardTextState.text)
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
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
                label = { Text(text = stringResource(id = R.string.title_wizard_link_title))},
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_wizard_link_hint),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    )
                },
                supportingText = {
                    if(urlValidator.isValid.not()){
                        Text(
                            text = urlValidator.getErrorMessage(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if(urlValidator.validate(wizardTextState.text)){
                        viewModel.onFetchWizardSuggestion(wizardTextState.text)
                    }
                })
            )

            Spacer(modifier = Modifier.size(8.dp))

            WizardSuggestionStateCard(wizardSuggestionUiState = wizardSuggestionUiState)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(color = MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                text = stringResource(id = R.string.button_skip),
                fontSize = 18.sp,
                onClick = { viewModel.onSkip() }
            )
            ActionButton(
                text = stringResource(id = R.string.button_apply),
                enabled = wizardSuggestionUiState is WizardSuggestionUiState.Success,
                fontSize = 18.sp,
                onClick = { viewModel.onApplyWizardSuggestion() })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePoiFormScreen(
    wizardSuggestionUiModel: WizardSuggestionUiModel,
    viewModel: CreatePoiViewModel,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {

}