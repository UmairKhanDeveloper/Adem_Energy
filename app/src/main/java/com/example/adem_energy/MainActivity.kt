package com.example.adem_energy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.adem_energy.screen.NavEntry
import com.example.adem_energy.ui.theme.Adem_EnergyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Adem_EnergyTheme {
                NavEntry()
            }
        }
    }
}

