package com.example.adem_energy.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
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
fun ProgrammeScreen(navController: NavController) {

    val purpleEnergyGradient = Brush.verticalGradient(
        listOf(Color(0xFF9C27B0), Color(0xFFBA68C8))
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
    val purpleDark = Color(0xFF8E24AA)
    val scope = rememberCoroutineScope()

    // Firebase setup
    val context = LocalContext.current
    val databaseReference = FirebaseDatabase.getInstance().reference.child("Programmes")
    val repository = remember { RealTimeDbRepository(databaseReference, context) }
    val realTimeViewModel = remember { RealTimeViewModel(repository) }

    // States
    var textField by remember { mutableStateOf("") }
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

    var isLoading by remember { mutableStateOf(false) }

    // ✅ Dialog State
    var showSaveDialog by remember { mutableStateOf(false) }
    var remedyName by remember { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(backgroundGradient)
            ) {
                AppDrawerProgramme(navController = navController) { key ->
                    coroutineScope.launch { drawerState.close() }
                    when (key) {
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
                .background(backgroundGradient)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ✅ TopAppBar
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.applogo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(4.dp)
                            )
                            Text(
                                "Programme",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    modifier = Modifier.background(purpleEnergyGradient)
                )

                // ✅ Main Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    // Programme Name
                    OutlinedTextField(
                        value = textField,
                        onValueChange = { textField = it },
                        placeholder = { Text("Enter Programme Name") },
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

                    // Potency + Scale
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("Potency", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            OutlinedTextField(
                                value = potency,
                                onValueChange = { potency = it },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
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
                                onExpandedChange = { scaleExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedScale,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(scaleExpanded) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                                ExposedDropdownMenu(expanded = scaleExpanded, onDismissRequest = { scaleExpanded = false }) {
                                    scales.forEach {
                                        DropdownMenuItem(
                                            text = { Text(it) },
                                            onClick = {
                                                selectedScale = it
                                                scaleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Units + Timer
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("Units", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = unitsExpanded,
                                onExpandedChange = { unitsExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedUnits,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(unitsExpanded) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                                ExposedDropdownMenu(expanded = unitsExpanded, onDismissRequest = { unitsExpanded = false }) {
                                    units.forEach {
                                        DropdownMenuItem(
                                            text = { Text(it) },
                                            onClick = {
                                                selectedUnits = it
                                                unitsExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(Modifier.weight(1f)) {
                            Text("Timer", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = purpleDark)
                            ExposedDropdownMenuBox(
                                expanded = timerExpanded,
                                onExpandedChange = { timerExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedTimer,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(timerExpanded) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth().height(54.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                                ExposedDropdownMenu(expanded = timerExpanded, onDismissRequest = { timerExpanded = false }) {
                                    timers.forEach {
                                        DropdownMenuItem(
                                            text = { Text(it) },
                                            onClick = {
                                                selectedTimer = it
                                                timerExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GradientButton("RESONATE", purpleEnergyGradient, Modifier.weight(1f)) { }
                        GradientButton("IMPRINT", purpleEnergyGradient, Modifier.weight(1f)) { }
                    }

                    GradientButton(
                        text = "Save as",
                        gradient = purpleEnergyGradient,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        icon = Icons.Default.Save
                    ) {
                        showSaveDialog = true
                    }

                    Text(
                        "This screen is programmed to receive all vibrations to make remedies or to pass the vibrations to patients via witness and will not store any vibrations that are passed through it.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    )
                }
            }
        }
    }

    // ✅ Save Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
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
                                userFirstName = "",
                                email = "",
                                password = "",
                                remedyName = remedyName,
                                remedyInfo = "Programme Remedy",
                                potency = potency,
                                scale = selectedScale,
                                units = selectedUnits,
                                timer = selectedTimer,
                                color = ""
                            )

                            // ✅ Direct call, no collect
                            realTimeViewModel.insert(item)

                            // UI reset (success/error observe state se karna hai)
                            showSaveDialog = false
                            remedyName = ""
                            isLoading = false
                        } else {
                            Toast.makeText(context, "Please enter remedy name", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            }
            ,
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}



// ✅ Reusable Gradient Button
@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.background(gradient, RoundedCornerShape(50)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        icon?.let {
            Spacer(Modifier.width(6.dp))
            Icon(it, contentDescription = null, tint = Color.White)
        }
    }
}


@Composable
fun AppDrawerProgramme(
    navController: NavController,
    onItemClick: (String) -> Unit
) {
    // 🌸 Purple Theme
    val purpleEnergyGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF9C27B0), // Purple
            Color(0xFFBA68C8)  // Light Purple
        )
    )
    val backgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )

    val iconColor = Color(0xFF8E24AA) // Deep purple
    val textColor = Color(0xFF6A1B9A) // Dark purple text

    Column(
        modifier = Modifier.fillMaxHeight(0.9f).background(backgroundGradient)
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().background(purpleEnergyGradient).padding(20.dp),
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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("SHAH-Sr369", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        "Safety Healing Activation Harmony - Serenity-369",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Drawer Items
        DrawerItemProgramme(Icons.Default.Person, "Profile", iconColor, textColor) { onItemClick("profile") }
        DrawerItemProgramme(Icons.Default.Home, "Home", iconColor, textColor) { onItemClick("home") }
        DrawerItemProgramme(Icons.Default.Folder, "My Library", iconColor, textColor) { onItemClick("library") }

        Spacer(modifier = Modifier.weight(1f))

        // Help Center
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .clickable { }
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = "Help", tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Help Center", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = iconColor)
            }
        }
    }
}


@Composable
fun DrawerItemProgramme(
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