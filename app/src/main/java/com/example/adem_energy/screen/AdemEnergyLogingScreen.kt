package com.example.adem_energy.screen

import SharedPrefManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.adem_energy.R
import com.example.adem_energy.firebase.AuthRepositoryImpl
import com.example.adem_energy.firebase.AuthUser
import com.example.adem_energy.firebase.AuthViewModel
import com.example.adem_energy.firebase.AuthViewModelFactory
import com.example.adem_energy.firebase.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdemEnergyLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val primaryPurple = Color(0xFF9C27B0)
    val secondaryPurple = Color(0xFFBA68C8)

    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )

    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(primaryPurple, secondaryPurple)
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // NEW: states for reset-password dialog
    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var isResetSending by remember { mutableStateOf(false) }

    // ✅ ViewModel with factory (unchanged)
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            AuthRepositoryImpl(FirebaseAuth.getInstance(), context)
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Logo (unchanged)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            // Email (unchanged)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password (unchanged)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = primaryPurple
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Remember Me + Forgot (modified: clicking Forgot opens dialog OR uses current email)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = primaryPurple)
                    )
                    Text("Remember Me", fontSize = 14.sp)
                }

                // Clicking forgot opens a dialog to confirm / enter email
                Text(
                    "Forgot Password?",
                    fontSize = 14.sp,
                    color = secondaryPurple,
                    modifier = Modifier
                        .clickable {
                            // If email field already has a value, prefill dialog with it
                            resetEmail = email
                            showResetDialog = true
                        }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            val prefManager = remember { SharedPrefManager(context) }

            // Login Button (unchanged)
            Button(
                onClick = {
                    isLoading = true
                    val user = AuthUser(email = email, password = password)
                    scope.launch {
                        viewModel.loginUser(user).collect { result ->
                            when (result) {
                                is ResultState.Success -> {
                                    isLoading = false
                                    Toast.makeText(context, result.response, Toast.LENGTH_SHORT).show()
                                    prefManager.setLoginStatus(true)
                                    navController.navigate(Screen.HealingScreen.route) {
                                        popUpTo(Screen.AdemEnergyLoginScreen.route) { inclusive = true }
                                    }
                                }

                                is ResultState.Error -> {
                                    isLoading = false
                                    Toast.makeText(context, result.exception.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                                    prefManager.setLoginStatus(false)
                                }

                                is ResultState.Loading -> {
                                    isLoading = true
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(professionalEnergyGradient, shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Login",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("or", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button (unchanged)
            OutlinedButton(
                onClick = { navController.navigate(Screen.SignUpScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = primaryPurple
                )
            ) {
                Text(
                    "Sign Up",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Don’t have an account? Create Account",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // ---- Reset Password AlertDialog ----
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Validate email
                        val emailToSend = resetEmail.trim()
                        if (emailToSend.isEmpty()) {
                            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                            return@TextButton
                        }
                        isResetSending = true

                        FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(emailToSend)
                            .addOnCompleteListener { task ->
                                isResetSending = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Password reset email sent to $emailToSend", Toast.LENGTH_LONG).show()
                                    showResetDialog = false
                                } else {
                                    val message = task.exception?.localizedMessage ?: "Failed to send reset email"
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                ) {
                    if (isResetSending) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Send")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("Enter the email address associated with your account. We will send a link to reset your password.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        placeholder = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }
        )
    }
}
