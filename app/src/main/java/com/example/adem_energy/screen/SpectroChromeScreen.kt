package com.example.adem_energy.screen

import android.R.attr.textColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpectroChromeScreen(navController: NavController) {

    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3F51B5), // Indigo
            Color(0xFF2196F3)  // Blue
        )
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)) // Light bluish background for a professional feel
    )


    // Original colors (some might be replaced or adjusted)
    val purpleDark = Color(0xFF3F51B5) // Using the indigo from your gradient for consistency
    val surfaceLight = Color(0xFFF0F8FF) // A very light blue for surface elements
    val borderLight = Color(0xFF90CAF9) // A light blue for borders

    var textField by remember { mutableStateOf("") }
    var potency by remember { mutableStateOf("") }

    val scales = listOf("Q", "X", "C", "D", "M", "CM", "MM", "LM")
    var selectedScale by remember { mutableStateOf("Q") }
    var scaleExpanded by remember { mutableStateOf(false) }

    val units = listOf("130", "180", "230", "247", "248", "249")
    var selectedUnits by remember { mutableStateOf("130") }
    var unitsExpanded by remember { mutableStateOf(false) }

    val timers = listOf("10 Sec", "20 Sec", "30 Sec", "60 Sec")
    var selectedTimer by remember { mutableStateOf("30 Sec") }
    var timerExpanded by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Apply background gradient to the drawer surface
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(backgroundGradient) // Apply gradient here
            ) {
                AppDrawerSpectroChrome(navController = navController) { drawerItemKey ->
                    coroutineScope.launch { drawerState.close() }
                    when (drawerItemKey) {
                        "profile" -> navController.navigate(Screen.AccountScreen.route)
                        "home" -> navController.navigate(Screen.HealingScreen.route)
                        "library" -> navController.navigate(Screen.LibraryScreen.route)
                    }
                }
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize().background(backgroundGradient)) { // Main background gradient
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.2f)), // Slightly more visible white
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.applogo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(55.dp)
                                )
                            }
                            Text(
                                text = "SpectroChrome",
                                color = Color.White, // Text color on top app bar should be white
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White // Icon color on top app bar should be white
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White) // Icon color white
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent), // Set to transparent
                    modifier = Modifier.background(professionalEnergyGradient) // Apply gradient directly
                )

                Column(
                    modifier = Modifier
                        .background(backgroundGradient) // Apply gradient to content column
                        .fillMaxWidth().verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Pick a color",
                        fontSize = 14.sp,
                        color = Color(0xFF3F51B5) // Keeping the text color consistent with other labels
                    )

                    Spacer(modifier = Modifier.height(8.dp))

// Bordered Box with Icon inside - now with gradient border and icon tint
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(
                                width = 1.dp,
                                // Use the professional gradient for the border, or the last color of it
                                color = Color(0xFF90CAF9), // Vibrant blue border
                                shape = RoundedCornerShape(6.dp)
                            )
                            .background(Color.White, shape = RoundedCornerShape(6.dp)), // White background
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Color Picker",
                            tint = Color.Gray, // Vibrant blue icon tint
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Potency", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            OutlinedTextField(
                                value = potency,
                                onValueChange = { potency = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = surfaceLight,
                                    unfocusedContainerColor = surfaceLight,
                                    focusedIndicatorColor = purpleDark,
                                    unfocusedIndicatorColor = borderLight,
                                    focusedLabelColor = purpleDark,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Scale", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = scaleExpanded,
                                onExpandedChange = { scaleExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedScale,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = scaleExpanded)
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = surfaceLight,
                                        unfocusedContainerColor = surfaceLight,
                                        focusedIndicatorColor = purpleDark,
                                        unfocusedIndicatorColor = borderLight,
                                        focusedLabelColor = purpleDark,
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = scaleExpanded,
                                    onDismissRequest = { scaleExpanded = false }
                                ) {
                                    scales.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = {
                                                selectedScale = value
                                                scaleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Units", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = unitsExpanded,
                                onExpandedChange = { unitsExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedUnits,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitsExpanded)
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = surfaceLight,
                                        unfocusedContainerColor = surfaceLight,
                                        focusedIndicatorColor = purpleDark,
                                        unfocusedIndicatorColor = borderLight,
                                        focusedLabelColor = purpleDark,
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = unitsExpanded,
                                    onDismissRequest = { unitsExpanded = false }
                                ) {
                                    units.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = {
                                                selectedUnits = value
                                                unitsExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Timer", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = timerExpanded,
                                onExpandedChange = { timerExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedTimer,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = timerExpanded)
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = surfaceLight,
                                        unfocusedContainerColor = surfaceLight,
                                        focusedIndicatorColor = purpleDark,
                                        unfocusedIndicatorColor = borderLight,
                                        focusedLabelColor = purpleDark,
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = timerExpanded,
                                    onDismissRequest = { timerExpanded = false }
                                ) {
                                    timers.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = {
                                                selectedTimer = value
                                                timerExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { /* TODO: Resonante Action */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .background(professionalEnergyGradient, RoundedCornerShape(8.dp)), // Apply gradient
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Make button transparent to show gradient
                                contentColor = Color.White
                            )
                        ) {
                            Text("RESONATE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { /* TODO: Imprint Action */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .background(professionalEnergyGradient, RoundedCornerShape(8.dp)), // Apply gradient
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Make button transparent to show gradient
                                contentColor = Color.White
                            )
                        ) {
                            Text("IMPRINT", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(professionalEnergyGradient, RoundedCornerShape(50)), // Apply gradient
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, // Make button transparent to show gradient
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "Save as",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = Color.White
                        )
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "This screen is programmed to receive all vibrations to make remedies or to pass the vibrations to patients via witness and will not store any vibrations that are passed through it.",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center, // center text align
                            color = Color.Gray, // Keep text gray for information
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun AppDrawerSpectroChrome(
    navController: NavController,
    onItemClick: (String) -> Unit
) {
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3F51B5), // Indigo
            Color(0xFF2196F3)  // Blue
        )
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)) // Light bluish background for a professional feel
    )

    // Using colors from the gradient for consistency
    val iconColor = Color(0xFF3F51B5) // Indigo from the professional gradient
    val textColor = Color(0xFF2C3E50) // Darker text for professionalism

    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .background(backgroundGradient) // Apply background gradient to the drawer
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(professionalEnergyGradient) // Apply professional energy gradient to header
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SHAH-Sr369",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Safety Healing Activation Harmony - Serenity-369",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Drawer Items
        DrawerItemSpectroChrome(
            icon = Icons.Default.Person,
            label = "Profile",
            iconTint = iconColor,
            textColor = textColor,
            onClick = { onItemClick("profile") }
        )
        DrawerItemSpectroChrome(
            icon = Icons.Default.Home,
            label = "Home",
            iconTint = iconColor,
            textColor = textColor,
            onClick = { onItemClick("home") }
        )
        DrawerItemSpectroChrome(
            icon = Icons.Default.Folder,
            label = "My Library",
            iconTint = iconColor,
            textColor = textColor,
            onClick = { onItemClick("library") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Help Center Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.8f)) // Slightly transparent white for a modern look
                .clickable { }
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Whatsapp,
                    contentDescription = "Help",
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Help Center",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go",
                    tint = iconColor
                )
            }
        }
    }
}


@Composable
fun DrawerItemSpectroChrome(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}
