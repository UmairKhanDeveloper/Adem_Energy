package com.example.adem_energy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun ImprintScreen(navController: NavController) {
    // Professional color palette based on your gradients
    val primaryBlue = Color(0xFF3F51B5) // Indigo from your gradient
    val secondaryBlue = Color(0xFF2196F3) // Blue from your gradient
    val lightBackground = Color(0xFFE3F2FD) // Light blueish background start
    val lighterBackground = Color(0xFFBBDEFB) // Light blueish background end
    val textGray = Color(0xFF505050) // Darker gray for text for better contrast
    val backgroundGradient = Brush.verticalGradient( listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)))

        val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            primaryBlue, // Indigo
            secondaryBlue // Blue
        )
    )

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
            // The drawer content will use updated professional colors
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
            ) {
                AppDrawer(navController = navController) { drawerItemKey ->
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient) // ⬅️ Gradient applied here
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent // ⬅️ Keep surface transparent
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // ✅ Top Bar
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
                                        .background(Color.White.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.applogo),
                                        contentDescription = "Logo",
                                        modifier = Modifier.size(55.dp)
                                    )
                                }
                                Text(
                                    text = "Holistic Remedies",
                                    color = Color.White, // White text looks better on gradient
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
                                    tint = Color.White // White icons on gradient
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                coroutineScope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent, // Transparent so gradient shows
                            titleContentColor = Color.White
                        )
                    )

                    // ✅ Content Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Remedy Name
                        OutlinedTextField(
                            value = textField,
                            onValueChange = { textField = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),

                            placeholder = {
                                Text(
                                    "Enter remedy name",
                                    color = textGray.copy(alpha = 0.6f)
                                )
                            }
                        )

                        // Potency + Scale Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Potency",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textGray
                                )
                                OutlinedTextField(
                                    value = potency,
                                    onValueChange = { potency = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Scale",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textGray
                                )
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
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = scaleExpanded,
                                        onDismissRequest = { scaleExpanded = false }
                                    ) {
                                        scales.forEach { value ->
                                            DropdownMenuItem(
                                                text = { Text(value, color = textGray) },
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

                        // Units + Timer Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Units",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textGray
                                )
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
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = unitsExpanded,
                                        onDismissRequest = { unitsExpanded = false }
                                    ) {
                                        units.forEach { value ->
                                            DropdownMenuItem(
                                                text = { Text(value, color = textGray) },
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
                                Text(
                                    "Timer",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textGray
                                )
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
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = timerExpanded,
                                        onDismissRequest = { timerExpanded = false }
                                    ) {
                                        timers.forEach { value ->
                                            DropdownMenuItem(
                                                text = { Text(value, color = textGray) },
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

                        // Imprint Button
                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(
                                    brush = professionalEnergyGradient,
                                    shape = RoundedCornerShape(12.dp)
                                ), // Apply professional gradient to button
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Make container transparent as background handles color
                                contentColor = Color.White
                            )
                        ) {
                            Text("IMPRINT", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Info Text
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "This screen is programmed to receive all vibrations to make remedies or to pass the vibrations to patients via witness and will not store any vibrations that are passed through it.",
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = textGray.copy(alpha = 0.7f), // Slightly lighter gray for info text
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun AppDrawer(
    navController: NavController,
    onItemClick: (String) -> Unit
) {
    // Professional color palette for drawer
    val drawerBackgroundLight = Color(0xFFF0F6FC) // Very light blue for drawer background
    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3F51B5), // Indigo
            Color(0xFF2196F3) // Blue
        )
    )
    val drawerIconColor = Color(0xFF1976D2) // A strong blue for icons
    val drawerTextColor = Color(0xFF303F9F) // A deep blue for text

    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .background(drawerBackgroundLight) // Apply light blue background
    ) {
        // ✅ Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient( // ✅ Horizontal gradient for modern look
                        colors = listOf(
                            Color(0xFF3F51B5), // Indigo
                            Color(0xFF2196F3)  // Blue
                        )
                    )
                )
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ✅ App Logo with rounded background
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.applogo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(55.dp)
                    )
                }

                // ✅ Text section
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

        // ✅ Drawer Items
        DrawerItem(
            icon = Icons.Default.Person,
            label = "Profile",
            iconTint = drawerIconColor,
            textColor = drawerTextColor,
            onClick = { onItemClick("profile") }
        )
        DrawerItem(
            icon = Icons.Default.Home,
            label = "Home",
            iconTint = drawerIconColor,
            textColor = drawerTextColor,
            onClick = { onItemClick("home") }
        )
        DrawerItem(
            icon = Icons.Default.Folder,
            label = "My Library",
            iconTint = drawerIconColor,
            textColor = drawerTextColor,
            onClick = { onItemClick("library") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // ✅ Help Center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .clickable { /* TODO: Open WhatsApp or support */ }
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.logo), // ✅ custom WhatsApp icon from drawable
                    contentDescription = "Help",
                    tint = drawerIconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Help Center",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = drawerTextColor,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go",
                    tint = drawerIconColor
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
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