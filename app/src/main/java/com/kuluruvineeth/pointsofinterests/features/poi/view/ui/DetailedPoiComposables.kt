package com.kuluruvineeth.pointsofinterests.features.poi.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.kuluruvineeth.pointsofinterests.core.utils.isRemoteImageUri
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.core.utils.extractSource
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.composable.CategoryFilterChips
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PulsingProgressBar

@Composable
fun PoiDetailedImage(imagePath: String) {

    val source = if (imagePath.isRemoteImageUri()){
        stringResource(id = R.string.title_source_remote_image,imagePath.extractSource().toString())
    }else{
        stringResource(id = R.string.title_source_local_image)
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model = imagePath,
            contentScale = ContentScale.FillWidth,
            contentDescription ="Poi image preview",
            loading = { PulsingProgressBar()},
            error = {
                Box {
                    Icon(
                        modifier = Modifier
                            .size(128.dp)
                            .align(Alignment.Center),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_loading_failed),
                        contentDescription = "Wizard suggestion image preview - error",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                    )
                }
            }
        )
        val shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = shape
                )
                .align(Alignment.BottomStart)
                .clip(shape = shape)
                .padding(8.dp),
            text = source,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.background,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun PoiMetadata(dataTime: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = dataTime,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun PoiCategories(categories: List<CategoryUiModel>) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 2.dp
    ) {
        categories.forEach { model -> CategoryFilterChips(categoryListItem = model) }
    }
}

@Composable
fun PoiTitle(title: String) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 3
    )
}

@Composable
fun PoiDescription(body: String) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiContentLink(source: String, contentLink: String, onClick: (String) -> Unit) {

    ElevatedCard(
        onClick = {onClick(contentLink)},
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(64.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            )
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                text = source,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textDecoration = TextDecoration.Underline
            )

            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_open_link),
                contentDescription = "Open web url icon"
            )
        }
    }
}

@Composable
fun PoiCommentsCount(count: Int) {
    Row(
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.title_comments_count),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }

    Divider(
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.2f
        )
    )
}

@Composable
fun PoiComment(
    modifier: Modifier = Modifier,
    id: String,
    message: String,
    dateTime: String,
    shouldShowDivider: Boolean,
    onDeleteComment: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = dateTime,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.4f
            )
        )
    }

    if(shouldShowDivider)
        Divider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
        )
}