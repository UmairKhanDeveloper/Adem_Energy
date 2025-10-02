package com.example.adem_energy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.R

@Composable
fun HealingScreen(navController: NavController) {

    // 🌸 Purple Theme Gradients
    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFF3E5F5), // Very Light Lavender
            Color(0xFFE1BEE7)  // Light Purple
        )
    )

    val purpleEnergyGradient = listOf(
        Color(0xFF9C27B0), // Primary Purple
        Color(0xFFBA68C8)  // Light Purple Accent
    )

    val primaryTextColor = Color(0xFF212121)
    val accentTextColor = Color(0xFF9C27B0) // Purple for title

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "SHAH-Sr369 Logo",
            modifier = Modifier
                .size(220.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Title
        Text(
            text = "Safety Healing Activation Harmony - Serenity-369",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = accentTextColor,
                fontFamily = FontFamily.SansSerif,
                fontSize = 26.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Revitalise • Energise • Health Way",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = primaryTextColor,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Description
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Mark (Stamp) • Duplicate (Copy) • Initiate Broadcast (Create)",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF555555),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = 22.sp
                ),
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GradientButton(
                text = "Specific Remedies",
                onClick = { navController.navigate(Screen.ProgrammeScreen.route) },
                gradientColors = purpleEnergyGradient // ✅ Purple Gradient
            )

            GradientButton(
                text = "Holistic Remedies",
                onClick = { navController.navigate(Screen.ImprintScreen.route) },
                gradientColors = purpleEnergyGradient.reversed() // ✅ Reverse Purple Gradient
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    gradientColors: List<Color>
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

