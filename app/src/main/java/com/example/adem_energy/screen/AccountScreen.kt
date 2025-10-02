package com.example.adem_energy.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.realtime_firebase.RealTimeDbRepository
import com.example.adem_energy.realtime_firebase.RealTimeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val databaseReference = FirebaseDatabase.getInstance().reference
    val repository = remember { RealTimeDbRepository(databaseReference, context) }
    val realTimeViewModel = remember { RealTimeViewModel(repository) }

    val state = realTimeViewModel.res.value

    LaunchedEffect(Unit) {
        realTimeViewModel.getItems()
    }

    val userItem = state.item.firstOrNull()?.items

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val primaryPurple = Color(0xFF9C27B0)
    val secondaryPurple = Color(0xFFBA68C8)
    val textDark = Color(0xFF212121)
    val textMedium = Color(0xFF757575)

    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
    val professionalEnergyGradient = Brush.verticalGradient(
        listOf(primaryPurple, secondaryPurple)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Account",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryPurple
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(backgroundGradient)
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 👤 Profile icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Icon",
                    tint = secondaryPurple,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Username (from DB → FirebaseAuth → default)
            Text(
                text = userItem?.userFirstName?.takeIf { it.isNotBlank() }
                    ?: currentUser?.displayName
                    ?: "Guest User",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            // ✅ Email (from DB → FirebaseAuth → default)
            Text(
                text = userItem?.email?.takeIf { it.isNotBlank() }
                    ?: currentUser?.email
                    ?: "No Email",
                fontSize = 15.sp,
                color = textMedium,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // 🔴 Delete Account Button
            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryPurple,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                Text("Delete Account", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 🔵 Logout Button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = secondaryPurple
                ),
                border = BorderStroke(1.8.dp, professionalEnergyGradient),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                Text("Log out", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }

    // 🟣 Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    val user = auth.currentUser
                    user?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.AdemEnergyLoginScreen.route) {
                                popUpTo("account") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    // 🟣 Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out") },
            text = { Text("Do you really want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    auth.signOut()
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.AdemEnergyLoginScreen.route) {
                        popUpTo("account") { inclusive = true }
                    }
                }) {
                    Text("Yes", color = primaryPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}