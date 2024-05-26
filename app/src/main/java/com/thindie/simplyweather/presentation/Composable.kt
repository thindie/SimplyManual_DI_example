package com.thindie.simplyweather.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    ElevatedButton(
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth(0.8f),
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium
    ) {
        leadingIcon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun SimpleSnackBar(
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
    message: String?,
    onDismiss: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (message != null) {
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            onDismiss()
        }
    }

    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { snackBarData ->
            if (isSuccess) {
                SuccessSnackbar(
                    modifier = Modifier.padding(16.dp),
                    message = snackBarData.visuals.message
                )
            } else {
                ErrorSnackbar(
                    modifier = Modifier.padding(16.dp),
                    message = snackBarData.visuals.message
                )
            }

        }
    )
}

@Composable
private fun ErrorSnackbar(
    modifier: Modifier = Modifier,
    message: String,
) {
    Snackbar(
        modifier = modifier,
        shape = RoundedCornerShape(13.dp),
        containerColor = Color.Red
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = message,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun SuccessSnackbar(
    modifier: Modifier = Modifier,
    message: String,
) {
    Snackbar(
        modifier = modifier,
        shape = RoundedCornerShape(13.dp),
        containerColor = Color.Blue
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = message,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}