package com.ipu.ipuoneapp.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        leadingIcon = leadingIcon,
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.height(56.dp),
        colors = OutlinedTextFieldDefaults.colors(

            // 👇 background (important for your UI)
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,

            // 👇 remove border completely
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color.LightGray,

            // 👇 text & icon colors
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    )
}