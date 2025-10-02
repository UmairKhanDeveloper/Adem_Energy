package com.example.adem_energy.screen


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.adem_energy.realtime_firebase.RealTimeDbRepository
import com.example.adem_energy.realtime_firebase.RealTimeUser
import com.example.adem_energy.realtime_firebase.RealTimeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {

    // 🎨 Theme Colors
    val primaryPurple = Color(0xFF9C27B0)
    val secondaryPurple = Color(0xFFBA68C8)

    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(primaryPurple, secondaryPurple)
    )

    val context = LocalContext.current
    val databaseReference = FirebaseDatabase.getInstance().reference
    val repository = remember { RealTimeDbRepository(databaseReference, context) }
    val realTimeViewModel = remember { RealTimeViewModel(repository) }

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ✅ Firebase Auth ViewModel
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthRepositoryImpl(FirebaseAuth.getInstance(), context))
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryPurple
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Name Field
            CustomTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email Field
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Field
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                passwordVisible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible },
                tintColor = primaryPurple
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Sign Up Button
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val authUser = AuthUser(email = email, password = password)
                    isLoading = true
                    scope.launch {
                        viewModel.createUser(authUser).collect { result ->
                            when (result) {
                                is ResultState.Success -> {
                                    // ✅ Save in Realtime Database
                                    val realTimeItem = RealTimeUser.RealTimeItems(
                                        userFirstName = name,
                                        email = email,
                                        password = password
                                    )
                                    realTimeViewModel.insert(realTimeItem)


                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "SignUp + DB Save Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                }

                                is ResultState.Error -> {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        result.exception.message ?: "Auth Error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                ResultState.Loading -> {
                                    isLoading = true
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
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
                            "Sign Up",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Already have an account? Login",
                fontSize = 15.sp,
                color = secondaryPurple,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.popBackStack() }
            )
        }
    }
}

/**
 * ✅ Reusable TextField Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null,
    tintColor: Color = Color.Gray
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { onToggleVisibility?.invoke() }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle Password Visibility",
                        tint = tintColor
                    )
                }
            }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

/**
 * ✅ Email Sender Helper
 */
