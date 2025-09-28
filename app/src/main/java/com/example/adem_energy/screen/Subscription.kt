package com.example.adem_energy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.R

@Composable
fun Subscription(navController: NavController) {
    var selectedPlan by remember { mutableStateOf("Monthly") }

    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3F51B5), // Indigo
            Color(0xFF2196F3)  // Blue
        )
    )

    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)) // Light blueish background for a professional feel
    )

    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .background(backgroundGradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo), // Ensure you have a suitable logo
            contentDescription = "App Logo",
            modifier = Modifier
                .size(250.dp)
        )
        Text(
            text = "Safety Healing Activation ", // More professional title
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center,
            color = Color(0xFF1A237E) // Darker, more professional blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Harmony - Serenity-369",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Subscribe now to gain instant and unlimited access to all premium features and insights.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subscription Options
        SubscriptionOption(
            price = "$14.99",
            duration = "Monthly",
            isSelected = selectedPlan == "Monthly",
            gradient = professionalEnergyGradient,
            onClick = { selectedPlan = "Monthly" }
        )
        SubscriptionOption(
            price = "$29.99",
            duration = "3 Months",
            isSelected = selectedPlan == "3 Months",
            gradient = professionalEnergyGradient,
            onClick = { selectedPlan = "3 Months" }
        )
        SubscriptionOption(
            price = "$59.99",
            duration = "1 Year",
            isSelected = selectedPlan == "1 Year",
            gradient = professionalEnergyGradient,
            onClick = { selectedPlan = "1 Year" }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Flexible billing, cancel anytime.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "By subscribing, you agree to our Terms of Service and Privacy Policy. Elevate your energy management with Adem Energy.",
            fontSize = 13.sp,
            color = Color(0xFF616161),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun SubscriptionOption(
    price: String,
    duration: String,
    isSelected: Boolean,
    gradient: Brush,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        gradient
    } else {
        Brush.horizontalGradient(listOf(Color.White, Color.White)) // Solid white for unselected
    }

    val textColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) Color.Transparent else Color(0xFFE0E0E0) // Light gray border for unselected

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor, shape = RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = price,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
            Text(
                text = duration,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}