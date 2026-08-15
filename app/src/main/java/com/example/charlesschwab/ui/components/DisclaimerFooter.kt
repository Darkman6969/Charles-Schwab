package com.example.charlesschwab.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.charlesschwab.ui.theme.NeutralGray

@Composable
fun DisclaimerFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalDivider(color = NeutralGray.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quotes are delayed by at least 15 minutes. Market data provided by ICE Data Services. Member SIPC. Not FDIC Insured. No Bank Guarantee. May Lose Value.",
            style = MaterialTheme.typography.labelSmall,
            color = NeutralGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "© 2026 Charles Schwab & Co., Inc. All rights reserved.",
            style = MaterialTheme.typography.labelSmall,
            color = NeutralGray
        )
    }
}
