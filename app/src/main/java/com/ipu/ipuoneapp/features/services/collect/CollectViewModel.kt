package com.ipu.ipuoneapp.features.services.collect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.data.model.document.DocumentRequestDto
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class CollectViewModel(private val context: Context) : ViewModel() {

    var selectedDocumentType by mutableStateOf("MARKSHEET")
    var selectedSemester by mutableStateOf("1")
    var selectedExamType by mutableStateOf("Regular")
    var selectedNocDuration by mutableStateOf("4-6 weeks")
    
    var imageUri by mutableStateOf<Uri?>(null)
    
    var isLoading by mutableStateOf(false)
    var successMessage by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    var myDocuments by mutableStateOf<List<com.ipu.ipuoneapp.data.model.document.DocumentResponseDto>>(emptyList())
    var isLoadingDocuments by mutableStateOf(false)

    private val apiService = ApiClient.provideApi(context)

    init {
        fetchMyDocuments()
    }

    private fun fetchMyDocuments() {
        viewModelScope.launch {
            isLoadingDocuments = true
            try {
                myDocuments = apiService.getMyDocuments()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingDocuments = false
            }
        }
    }

    fun submitDocument() {
        if (imageUri == null) {
            errorMessage = "Please upload an image of the document"
            return
        }

        val base64Image = encodeImageToBase64(imageUri!!)
        if (base64Image == null) {
            errorMessage = "Failed to process the image"
            return
        }

        val request = DocumentRequestDto(
            documentType = selectedDocumentType,
            semester = if (selectedDocumentType == "MARKSHEET") selectedSemester else null,
            examType = if (selectedDocumentType == "MARKSHEET") selectedExamType else null,
            nocDuration = if (selectedDocumentType == "INTERNSHIP_NOC") selectedNocDuration else null,
            imageBase64 = base64Image
        )

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null
            try {
                apiService.submitDocument(request)
                successMessage = "Document submitted successfully!"
                imageUri = null
                fetchMyDocuments() // Refresh the list
            } catch (e: Exception) {
                errorMessage = "Failed to submit document: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            // Compress bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            
            // Encode
            val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
