package com.example.charlesschwab.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charlesschwab.ui.theme.SchwabNavy

@Composable
fun MoreScreen(
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // --- Header ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Text(
                text = "More",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                color = SchwabNavy
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Options Grid ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val menuItems = listOf(
                MoreMenuItem("Accounts", Icons.Outlined.AccountBalance),
                MoreMenuItem("Transfers", Icons.Outlined.SwapHoriz),
                MoreMenuItem("Deposits", Icons.Outlined.AccountBalanceWallet),
                MoreMenuItem("Positions", Icons.Outlined.PieChart),
                MoreMenuItem("History", Icons.Outlined.History),
                MoreMenuItem("Statements", Icons.Outlined.Description),
                MoreMenuItem("Messages", Icons.AutoMirrored.Outlined.Message),
                MoreMenuItem("Settings", Icons.Outlined.Settings),
                MoreMenuItem("Help", Icons.AutoMirrored.Outlined.HelpOutline)
            )

            items(menuItems) { item ->
                MoreGridItem(item)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Log Out Button ---
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(48.dp),
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Red
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
        ) {
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        Text(
            text = "App Version 4.12.0",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun MoreGridItem(item: MoreMenuItem) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { /* Navigate */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = SchwabNavy,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = SchwabNavy
            )
        }
    }
}

data class MoreMenuItem(
    val label: String,
    val icon: ImageVector
)
