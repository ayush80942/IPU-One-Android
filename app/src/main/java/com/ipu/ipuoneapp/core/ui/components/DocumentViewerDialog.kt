package com.ipu.ipuoneapp.core.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.utils.DocumentDownloader
import com.ipu.ipuoneapp.data.model.document.DocumentResponseDto
import com.ipu.ipuoneapp.data.model.document.displayLabel
import kotlinx.coroutines.launch

/**
 * Full-screen viewer for a submitted document's image, with a download-as-PDF action.
 * Shared by CollectDocumentScreen and RecentDocumentsSection so the download logic
 * (and its loading/error handling) only lives in one place.
 */
@Composable
fun DocumentViewerDialog(
    document: DocumentResponseDto,
    imageLoader: ImageLoader,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var downloading by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }

                IconButton(
                    enabled = !downloading,
                    onClick = {
                        downloading = true
                        scope.launch {
                            val result = DocumentDownloader.downloadAsPdf(
                                context = context,
                                imageLoader = imageLoader,
                                imageUrl = ApiClient.resolveUrl(document.fileUrl),
                                fileBaseName = buildFileName(document)
                            )
                            downloading = false
                            val message = result.fold(
                                onSuccess = { savedAs -> "Saved to $savedAs" },
                                onFailure = { "Download failed. Please try again." }
                            )
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    if (downloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Download, contentDescription = "Download as PDF", tint = Color.White)
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ApiClient.resolveUrl(document.fileUrl),
                        imageLoader = imageLoader
                    ),
                    contentDescription = "Viewed Document",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private fun buildFileName(document: DocumentResponseDto): String {
    val label = document.displayLabel()
        .replace(Regex("[^A-Za-z0-9]+"), "_")
        .trim('_')
    val datePart = document.submittedAt.take(10) // yyyy-MM-dd prefix of the ISO timestamp
    return "${label}_$datePart"
}
