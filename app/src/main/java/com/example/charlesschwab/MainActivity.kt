package com.example.charlesschwab

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.charlesschwab.ui.MainScaffold
import com.example.charlesschwab.ui.auth.ApexLoginScreen
import com.example.charlesschwab.ui.theme.CharlesSchwabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharlesSchwabTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                
                if (isLoggedIn) {
                    MainScaffold(onLogout = { isLoggedIn = false })
                } else {
                    ApexLoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}
