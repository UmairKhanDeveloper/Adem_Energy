package com.example.adem_energy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
    val items = listOf(
        ItemData("Chenelised", "Crotalus H"),
        ItemData("Eagle Marm"),
        ItemData("Selenium 1000x")
    )

    // 🎨 Professional gradients
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3F51B5), // Indigo
            Color(0xFF2196F3)  // Blue
        )
    )

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE3F2FD), // Light blue
            Color(0xFFBBDEFB)  // Softer blue
        )
    )

    val iconColor = Color(0xFF1976D2)   // Deep Blue
    val titleColor = Color(0xFF0D47A1)  // Navy

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient) // ✅ Gradient background
    ) {
        Column {
            TopAppBar(
                title = {
                    Text("My Library", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(professionalEnergyGradient) // ✅ Gradient TopBar
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items.size) { index ->
                    LibraryItem(
                        item = items[index],
                        iconColor = iconColor,
                        titleColor = titleColor
                    )
                }
            }
        }
    }
}

data class ItemData(val title: String, val subtitle: String? = null)

@Composable
fun LibraryItem(item: ItemData, iconColor: Color, titleColor: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
                item.subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = iconColor,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.8f) // ✅ Delete icon red for clarity
                )
            }
        }
    }
}
