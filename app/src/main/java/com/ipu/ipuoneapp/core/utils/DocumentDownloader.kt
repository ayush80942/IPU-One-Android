package com.ipu.ipuoneapp.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

/**
 * Wraps a document's (already-uploaded) image in a single-page PDF and saves it to the
 * device's Downloads folder via MediaStore — no storage permission needed since minSdk 30
 * is already past the scoped-storage cutoff.
 */
object DocumentDownloader {

    private const val DOWNLOAD_SUBFOLDER = "IPU One"

    suspend fun downloadAsPdf(
        context: Context,
        imageLoader: ImageLoader,
        imageUrl: String,
        fileBaseName: String
    ): Result<String> {
        return try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                // PdfDocument's Canvas can't draw a HARDWARE-config bitmap; force software.
                .allowHardware(false)
                .build()

            val result = imageLoader.execute(request)
            val drawable = (result as? SuccessResult)?.drawable as? BitmapDrawable
                ?: return Result.failure(IllegalStateException("Could not load the document image"))
            val bitmap = drawable.bitmap

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            val displayName = "$fileBaseName.pdf"
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/$DOWNLOAD_SUBFOLDER")
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: return Result.failure(IllegalStateException("Could not create the download file"))

            resolver.openOutputStream(uri)?.use { out -> pdfDocument.writeTo(out) }
                ?: return Result.failure(IllegalStateException("Could not open the download file"))

            pdfDocument.close()
            Result.success("$DOWNLOAD_SUBFOLDER/$displayName")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
