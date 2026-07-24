package com.ipu.ipuoneapp.features.notices

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ipu.ipuoneapp.core.ui.components.NoticeBadgePill
import com.ipu.ipuoneapp.core.ui.components.NoticeCategoryChip
import com.ipu.ipuoneapp.data.model.notice.NOTICE_CATEGORY_FILTERS
import com.ipu.ipuoneapp.data.model.notice.NoticeResponseDto

@Composable
fun NoticesScreen() {
    val context = LocalContext.current
    val viewModel = remember { NoticesViewModel(context) }
    val state = viewModel.state
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        // ================= HEADER =================
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notices",
                    style = typography.headlineLarge.copy(color = colors.primary)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search
            NoticeSearchField(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category filter chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(NOTICE_CATEGORY_FILTERS) { (value, label) ->
                    val isSelected = state.selectedCategory == value
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onCategorySelected(value) },
                        label = {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.primary,
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= CONTENT =================
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            state.error != null -> {
                Box(
                    Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Failed to load notices",
                            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            state.error,
                            style = typography.bodySmall.copy(color = colors.onBackground.copy(alpha = 0.6f)),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchNotices() },
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                        ) { Text("Retry") }
                    }
                }
            }
            state.notices.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No notices found",
                        style = typography.bodyMedium.copy(color = colors.onBackground.copy(alpha = 0.5f))
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.notices, key = { it.id }) { notice ->
                        NoticeCard(
                            notice = notice,
                            onActionClick = {
                                val intent = Intent(Intent.ACTION_VIEW, notice.actionUrl.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun NoticeSearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) colors.primary.copy(alpha = 0.6f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "searchBorderColor"
    )

    val shadowElevation by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isFocused) 6f else 2f,
        animationSpec = tween(durationMillis = 200),
        label = "searchShadow"
    )

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = colors.onSurface,
            fontWeight = FontWeight.Normal
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .shadow(elevation = shadowElevation.dp, shape = RoundedCornerShape(20.dp))
            .background(
                color = if (isFocused) colors.surface else Color(0xFFF4F4F6),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = if (isFocused) colors.primary else colors.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search notices, circulars…",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = colors.onSurfaceVariant.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = colors.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NoticeCard(
    notice: NoticeResponseDto,
    onActionClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val isUrgent = notice.badge?.uppercase() == "URGENT"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = if (isUrgent) colors.primary.copy(alpha = 0.4f) else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Top row: category + badge + date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NoticeCategoryChip(category = notice.category)
                NoticeBadgePill(badge = notice.badge)
            }
            Text(text = notice.date, style = typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = notice.title,
            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = notice.description,
            style = typography.bodyMedium.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (notice.isPdf) Icons.Default.PictureAsPdf else Icons.Default.Link,
                    contentDescription = null,
                    tint = colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = notice.actionText,
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = typography.bodySmall
                )
            }

            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = if (notice.isPdf) Icons.Default.Download else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.primary
                )
            }
        }
    }
}