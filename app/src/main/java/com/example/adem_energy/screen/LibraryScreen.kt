package com.example.adem_energy.screen



import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.realtime_firebase.RealTimeDbRepository
import com.example.adem_energy.realtime_firebase.RealTimeUser
import com.example.adem_energy.realtime_firebase.RealTimeViewModel
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
    val context = LocalContext.current
    val databaseReference = FirebaseDatabase.getInstance().reference
    val repository = remember { RealTimeDbRepository(databaseReference, context) }
    val realTimeViewModel = remember { RealTimeViewModel(repository) }

    val state = realTimeViewModel.res.value

    // 🔄 Fetch data from DB
    LaunchedEffect(Unit) {
        realTimeViewModel.getItems()
    }

    // ✅ Remedies list from Realtime DB
    val items = state.item

    var selectedItem by remember { mutableStateOf<RealTimeUser?>(null) }

    // 🎨 Colors
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF9C27B0), Color(0xFFBA68C8))
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
    val iconColor = Color(0xFF8E24AA)
    val titleColor = Color(0xFF6A1B9A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(8.dp)
    ) {
        // 🔹 Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(professionalEnergyGradient)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "My Library",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Data States
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = iconColor)
                }
            }

            state.error.isNotEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = Color.Red, fontSize = 16.sp)
                }
            }

            items.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No remedies found", color = Color.Gray, fontSize = 16.sp)
                }
            }

            else -> {
                // ✅ Show all remedies
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items.size) { index ->
                        val user = items[index]
                        LibraryItemCard(
                            title = user.items.remedyName,
                            iconColor = iconColor,
                            titleColor = titleColor,
                            onHistoryClick = { selectedItem = user }
                        )
                    }
                }
            }
        }
    }

    // 📌 Remedy Detail Dialog
    if (selectedItem != null) {
        RemedyDialog(item = selectedItem!!, onDismiss = { selectedItem = null })
    }
}

@Composable
fun RemedyDialog(item: RealTimeUser, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color(0xFF7E57C2))
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Remedy: ${item.items.remedyName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Info: ${item.items.remedyInfo}")
                Text("Potency: ${item.items.potency}")
                Text("Scale: ${item.items.scale}")
                Text("Units: ${item.items.units}")
                Text("Timer: ${item.items.timer}")
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFFF3EAFB)
    )
}

@Composable
fun LibraryItemCard(title: String, iconColor: Color, titleColor: Color, onHistoryClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = iconColor,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onHistoryClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = iconColor,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { /* TODO: delete from Firebase */ }
                )
            }
        }
    }
}


