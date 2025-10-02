package com.example.adem_energy.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adem_energy.R
import com.example.adem_energy.firebase.ResultState
import com.example.adem_energy.realtime_firebase.RealTimeDbRepository
import com.example.adem_energy.realtime_firebase.RealTimeUser
import com.example.adem_energy.realtime_firebase.RealTimeViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemedyScreen(navController: NavController) {

    // 🌌 PURPLE THEME
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF8E24AA), Color(0xFFBA68C8))
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
    val textColor = Color(0xFF4A148C)
    val purpleDark = Color(0xFF6A1B9A)
    val scope = rememberCoroutineScope()
    // Firebase
    val context = LocalContext.current
    val databaseReference = FirebaseDatabase.getInstance().reference.child("remedies")
    val repository = remember { RealTimeDbRepository(databaseReference, context) }
    val realTimeViewModel = remember { RealTimeViewModel(repository) }

    // States
    val remedies = remember { mutableStateListOf<String>() }
    LaunchedEffect(Unit) { if (remedies.isEmpty()) remedies.add("") }

    var potency by remember { mutableStateOf("") }
    val scales = listOf("Q", "X", "C", "D", "M", "CM", "MM", "LM")
    var selectedScale by remember { mutableStateOf(scales.first()) }
    var scaleExpanded by remember { mutableStateOf(false) }

    val units = listOf("130", "180", "230", "247", "248", "249")
    var selectedUnits by remember { mutableStateOf(units.first()) }
    var unitsExpanded by remember { mutableStateOf(false) }

    val timers = listOf("10 Sec", "20 Sec", "30 Sec", "60 Sec")
    var selectedTimer by remember { mutableStateOf(timers[2]) }
    var timerExpanded by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var remedyName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // 🟣 Drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(backgroundGradient)
            ) {
                AppDrawerRemedy(navController = navController) { drawerItemKey ->
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // 🟣 Top Bar
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.applogo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(55.dp)
                                )
                            }
                            Text(
                                text = "Specific Remedies",
                                color = Color.White,
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
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    modifier = Modifier.background(professionalEnergyGradient)
                )

                // 🟣 Main Content
                Column(
                    modifier = Modifier
                        .background(backgroundGradient)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        "Enter Remedy Information",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )

                    // Remedy List
                    remedies.forEachIndexed { index, remedy ->
                        OutlinedTextField(
                            value = remedy,
                            onValueChange = { remedies[index] = it },
                            placeholder = { Text("Enter Remedy") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    // ✅ Limit to 4 remedies max
                    if (remedies.size < 4) {
                        OutlinedButton(
                            onClick = { remedies.add("") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = textColor,
                                containerColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = textColor)
                            Spacer(Modifier.width(6.dp))
                            Text("Add Remedy", fontWeight = FontWeight.Medium, color = textColor)
                        }
                    }

                    // Potency + Scale Row
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Potency", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
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
                        Column(Modifier.weight(1f)) {
                            Text("Scale", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = scaleExpanded,
                                onExpandedChange = { scaleExpanded = it }) {
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
                                    onDismissRequest = { scaleExpanded = false }) {
                                    scales.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = { selectedScale = value; scaleExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Units + Timer Row
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Units", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = unitsExpanded,
                                onExpandedChange = { unitsExpanded = it }) {
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
                                    onDismissRequest = { unitsExpanded = false }) {
                                    units.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = { selectedUnits = value; unitsExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Timer", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = timerExpanded,
                                onExpandedChange = { timerExpanded = it }) {
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
                                    onDismissRequest = { timerExpanded = false }) {
                                    timers.forEach { value ->
                                        DropdownMenuItem(
                                            text = { Text(value) },
                                            onClick = { selectedTimer = value; timerExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Buttons Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GradientButton(
                            text = "RESONATE",
                            gradient = professionalEnergyGradient,
                            modifier = Modifier.weight(1f)
                        ) { /* TODO */ }

                        GradientButton(
                            text = "IMPRINT",
                            gradient = professionalEnergyGradient,
                            modifier = Modifier.weight(1f)
                        ) { /* TODO */ }
                    }

                    // Save Button
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(professionalEnergyGradient, RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Save as",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.Save,
                                    contentDescription = "Save",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Info Text
                    Text(
                        text = "This screen is programmed to receive all vibrations to make remedies or to pass the vibrations to patients via witness and will not store any vibrations that are passed through it.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        // 🟣 Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "Enter Remedy Name",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    OutlinedTextField(
                        value = remedyName,
                        onValueChange = { remedyName = it },
                        placeholder = { Text("Remedy Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF2F2F2)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF2F2F2)
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (remedyName.isNotBlank()) {
                                isLoading = true
                                val item = RealTimeUser.RealTimeItems(
                                    userFirstName = "Demo",
                                    email = "",
                                    password = "",
                                    remedyName = remedyName,
                                    remedyInfo = remedies.joinToString(", "),
                                    potency = potency,
                                    scale = selectedScale,
                                    units = selectedUnits,
                                    timer = selectedTimer,
                                    color = ""
                                )

                                // ✅ Just call insert, no collect here
                                realTimeViewModel.insert(item)

                                // Reset UI immediately, observe success/error from state
                                isLoading = false
                                showDialog = false
                                remedyName = ""
                            }
                        }
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
                ,
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}


// 🟣 Reusable Gradient Button

@Composable
fun AppDrawerRemedy(
    navController: NavController,
    onItemClick: (String) -> Unit
) {
    // Professional Gradient Definitions
    val professionalEnergyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9C27B0), // Indigo
            Color(0xFFBA68C8)  // Blue
        )
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFF3E5F5),
            Color(0xFFE1BEE7)
        ) // Light bluish background for a professional feel
    )

    // Using colors from the gradient for consistency
    val iconColor = Color(0xFF8E24AA) // Indigo from the professional gradient
    val textColor = Color(0xFF6A1B9A) // Darker text for professionalism




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
        DrawerItemRemedy(
            icon = Icons.Default.Person,
            label = "Profile",
            iconTint = iconColor,
            textColor = textColor,
            onClick = { onItemClick("profile") }
        )
        DrawerItemRemedy(
            icon = Icons.Default.Home,
            label = "Home",
            iconTint = iconColor,
            textColor = textColor,
            onClick = { onItemClick("home") }
        )
        DrawerItemRemedy(
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
fun DrawerItemRemedy(
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

