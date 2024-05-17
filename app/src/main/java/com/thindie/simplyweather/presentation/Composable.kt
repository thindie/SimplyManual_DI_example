package com.thindie.simplyweather.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .height(72.dp)
            .fillMaxWidth(0.8f),
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium
    ) {
        leadingIcon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MVIScaffold(
    topBar: @Composable () -> Unit,
    topBarPlaceHolder: @Composable () -> Unit,
    shouldShowTopBar: Boolean,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (shouldShowTopBar) {
                    topBar()
            } else {
                topBarPlaceHolder()
            }
        },
    ) {
        content(Modifier.padding(it))
    }
}