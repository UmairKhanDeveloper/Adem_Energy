package com.example.adem_energy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.R
import com.example.adem_energy.screen.Screen

// ✅ Background gradients (your professional colors)
private val BackgroundGradient = Brush.verticalGradient(
    listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)) // Light blueish background
)

private val ProfessionalEnergyGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3F51B5), // Indigo
        Color(0xFF2196F3)  // Blue
    )
)

// ✅ Button gradient (kept same but can be aligned with ProfessionalEnergyGradient if needed)
private val ButtonPrimaryGradient = Brush.horizontalGradient(
    listOf(Color(0xFF3F51B5), Color(0xFF2196F3)) // Indigo → Blue
)

private val LightText = Color.White
private val DarkText = Color(0xFF222222)

private val AppFont = FontFamily(
    Font(resId = R.font.regular, weight = FontWeight.Normal),
    Font(resId = R.font.medium, weight = FontWeight.Medium),
    Font(resId = R.font.bold, weight = FontWeight.Bold)
)

// Text styles
private val HeadlineStyle = TextStyle(
    fontFamily = AppFont,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    color = DarkText,
    textAlign = TextAlign.Center
)

private val SubHeadlineStyle = TextStyle(
    fontFamily = AppFont,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    color = DarkText.copy(alpha = 0.9f),
    textAlign = TextAlign.Center
)

private val ButtonTextStyle = TextStyle(
    fontFamily = AppFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = LightText
)

private val DisclaimerTitleStyle = TextStyle(
    fontFamily = AppFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = DarkText
)

private val DisclaimerBodyStyle = TextStyle(
    fontFamily = AppFont,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = DarkText.copy(alpha = 0.75f),
    lineHeight = 22.sp
)

@Composable
fun SubscriptionScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient) // ✅ applied your background gradient
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 🔹 App Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Ensure you have a suitable logo
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(220.dp)
                    )

                    // 🔹 Title & Subtitle
                    Text(
                        text = "Safety Healing",
                        style = HeadlineStyle
                    )
                    Text(
                        text = "Activation Harmony · Serenity 369",
                        style = SubHeadlineStyle,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // 🔹 Subscription Button
                    Button(
                        onClick = { navController.navigate(Screen.Subscription.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(0.75f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    ButtonPrimaryGradient, // ✅ Indigo → Blue
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "START FREE TRIAL", style = ButtonTextStyle)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "$9.99 / Month after trial",
                        style = SubHeadlineStyle.copy(
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.None
                        )
                    )
                }

                // 🔹 Disclaimer box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LightText)
                        .padding(20.dp)
                ) {
                    Text("Disclaimer:", style = DisclaimerTitleStyle)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "SHAH-Sr369 is not intended to replace professional medical advice or approved treatments. It is not intended to diagnose or treat any health condition.",
                        style = DisclaimerBodyStyle
                    )


                }
                Spacer(modifier = Modifier.height(10.dp))
                // 🔹 Open App Button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { navController.navigate(Screen.AdemEnergyLoginScreen.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    ButtonPrimaryGradient, // ✅ Indigo → Blue
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "OPEN APP", style = ButtonTextStyle)
                        }
                    }
                }
            }
        }
    }
}
