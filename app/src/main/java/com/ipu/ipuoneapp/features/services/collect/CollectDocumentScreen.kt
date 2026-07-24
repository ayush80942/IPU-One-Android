package com.ipu.ipuoneapp.features.services.collect

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.compose.foundation.layout.Arrangement
import coil.compose.rememberAsyncImagePainter
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.ui.components.DocumentViewerDialog
import com.ipu.ipuoneapp.data.model.document.DocumentResponseDto
import com.ipu.ipuoneapp.data.model.document.displayLabel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectDocumentScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val viewModel = remember { CollectViewModel(context) }
    
    // Bottom Sheet State
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Hold the temporary URI where the camera will save the image
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            viewModel.imageUri = tempImageUri
        }
    }

    val launchCamera = {
        val tempFile = File.createTempFile("doc_image", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
        tempImageUri = uri
        cameraLauncher.launch(uri)
    }

    // Auto-close sheet on success
    LaunchedEffect(viewModel.successMessage) {
        if (viewModel.successMessage != null) {
            sheetState.hide()
            showBottomSheet = false
            // Reset success message so it doesn't trigger again immediately
            viewModel.successMessage = null
        }
    }

    // State for viewing/downloading a document
    var viewingDocument by remember { mutableStateOf<DocumentResponseDto?>(null) }
    val imageLoader = remember { ApiClient.provideImageLoader(context) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        
        // Header — same style as "Academic Performance" on Results page
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Student Cell Services",
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                
                Text(
                    text = "Please submit a receiving copy of the document you are collecting to the Student Cell.",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                )
            }
        }

        // Primary Button to Trigger Pop-up
        item {
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7A0000) // Dark Red
                )
            ) {
                Text(
                    "+ Collect New Document",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // List of submitted documents
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Collected Documents",
                    style = MaterialTheme.typography.headlineSmall
                )
                if (!viewModel.isLoadingDocuments) {
                    val count = viewModel.myDocuments.size
                    Text(
                        text = "$count ${if (count == 1) "document" else "documents"}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        if (viewModel.isLoadingDocuments) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (viewModel.myDocuments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No documents submitted yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(viewModel.myDocuments) { doc ->
                DocumentCard(
                    document = doc,
                    onViewDocument = { viewingDocument = doc }
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Submit Document",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Document Type Dropdown
                DropdownField(
                    label = "Document Type",
                    options = listOf(
                        "MARKSHEET" to "Marksheet",
                        "INTERNSHIP_NOC" to "Internship NOC",
                        "PERCENTAGE_CONVERSION_LETTER" to "Percentage Conversion Letter",
                        "BONAFIDE_CERTIFICATE" to "Bonafide Certificate",
                        "PROVISIONAL_CERTIFICATE" to "Provisional Certificate",
                        "CONSOLIDATED_MARKSHEET" to "Consolidated Marksheet",
                        "OTHER" to "Other"
                    ),
                    selectedValue = viewModel.selectedDocumentType,
                    onValueChange = { viewModel.selectedDocumentType = it }
                )

                // Conditional Fields
                if (viewModel.selectedDocumentType == "MARKSHEET") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DropdownField(
                            label = "Semester",
                            options = (1..8).map { it.toString() to "Sem $it" },
                            selectedValue = viewModel.selectedSemester,
                            onValueChange = { viewModel.selectedSemester = it },
                            modifier = Modifier.weight(1f)
                        )
                        
                        DropdownField(
                            label = "Exam Type",
                            options = listOf("Regular" to "Regular", "Reappear" to "Reappear"),
                            selectedValue = viewModel.selectedExamType,
                            onValueChange = { viewModel.selectedExamType = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else if (viewModel.selectedDocumentType == "INTERNSHIP_NOC") {
                    DropdownField(
                        label = "NOC Duration",
                        options = listOf("4-6 weeks" to "4-6 weeks", "6 months" to "6 months"),
                        selectedValue = viewModel.selectedNocDuration,
                        onValueChange = { viewModel.selectedNocDuration = it }
                    )
                }

                // Image Picker Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Document Picture",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable { launchCamera() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.imageUri),
                                contentDescription = "Captured Document",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Tap to Retake", 
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Tap to capture photo",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                // Status Messages
                if (viewModel.errorMessage != null) {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.submitDocument() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Submit", 
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    
    // Document Viewing/Download Dialog
    viewingDocument?.let { doc ->
        DocumentViewerDialog(
            document = doc,
            imageLoader = imageLoader,
            onDismiss = { viewingDocument = null }
        )
    }
}

@Composable
fun DocumentCard(
    document: DocumentResponseDto,
    onViewDocument: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF6EFEF), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFF5E3E3), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Document",
                    tint = Color(0xFF7A0000), // Deeper Red
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Middle Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.displayLabel(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A) // Dark Navy
                )
                
                // Parse date
                val formattedDate = remember(document.submittedAt) {
                    try {
                        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
                        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
                        val date = parser.parse(document.submittedAt)
                        if (date != null) formatter.format(date) else null
                    } catch (_: Exception) {
                        null
                    }
                }

                if (formattedDate != null) {
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF94A3B8), // Medium gray-blue
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // View Document button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onViewDocument() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View",
                        tint = Color(0xFF8B0000),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Document",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF8B0000),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<Pair<String, String>>, // value to display
    selectedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = options.find { it.first == selectedValue }?.second ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, display) ->
                DropdownMenuItem(
                    text = { Text(display) },
                    onClick = {
                        onValueChange(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
