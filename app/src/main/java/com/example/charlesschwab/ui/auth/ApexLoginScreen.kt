package com.example.charlesschwab.ui.auth

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat

/**
 * Apex Wealth Management - Enterprise Login Screen
 *
 * Designed to mimic tier-1 institutional brokerage apps with strict data-dense layouts,
 * tabular numerals, and high-contrast navy/teal palette.
 */

// --- Institutional Design Tokens ---
private val ApexNavy = Color(0xFF041424)
private val ApexTeal = Color(0xFF007A99)
private val ApexBorder = Color(0xFF2C3E50)
private val ApexTextPrimary = Color.White
private val ApexTextSecondary = Color(0xFF95A5A6)
private val ApexDivider = Color(0xFF34495E)

private val InstitutionalTypography = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontFeatureSettings = "tnum", // Tabular Numerals for data entry alignment
    color = ApexTextPrimary
)

@Composable
fun ApexLoginScreen(
    onLoginSuccess: () -> Unit = {},
    onAccountOpen: () -> Unit = {}
) {
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    
    val context = LocalContext.current

    // Automatically trigger biometrics if "Remember Me" is enabled (mimics launch behavior)
    // NOTE: This requires the host Activity to be a FragmentActivity.
    LaunchedEffect(Unit) {
        if (rememberMe && context is FragmentActivity) {
            showBiometricPrompt(context) {
                onLoginSuccess()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApexNavy)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // --- Brand Header ---
        Text(
            text = "APEX WEALTH MANAGEMENT",
            style = InstitutionalTypography.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(60.dp))

        // --- Secure Form Container (Single Bordered Block) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, ApexBorder), RoundedCornerShape(2.dp))
                .background(Color.Black.copy(alpha = 0.2f))
        ) {
            ApexTextField(
                value = loginId,
                onValueChange = { loginId = it },
                placeholder = "Login ID",
                modifier = Modifier.fillMaxWidth()
            )
            
            HorizontalDivider(color = ApexDivider, thickness = 1.dp)
            
            ApexTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth(),
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityToggle = { isPasswordVisible = !isPasswordVisible }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Custom Checkbox & Biometric Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { rememberMe = !rememberMe }
            ) {
                ApexSquareCheckbox(checked = rememberMe)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Remember Login ID",
                    style = InstitutionalTypography.copy(
                        fontSize = 14.sp,
                        color = ApexTextSecondary
                    )
                )
            }

            IconButton(
                onClick = { 
                    if (context is FragmentActivity) {
                        showBiometricPrompt(context, onLoginSuccess)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Biometric Login",
                    tint = ApexTeal,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Primary Action Button ---
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ApexTeal,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "LOG IN",
                style = InstitutionalTypography.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Footer Links ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(onClick = { /* Handle forgot creds */ }) {
                Text(
                    text = "Forgot Login ID or Password?",
                    style = InstitutionalTypography.copy(
                        fontSize = 14.sp,
                        color = ApexTeal,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            
            TextButton(onClick = onAccountOpen) {
                Text(
                    text = "Open a Brokerage Account",
                    style = InstitutionalTypography.copy(
                        fontSize = 14.sp,
                        color = ApexTeal,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Regulatory Disclaimer (Crucial for Institutional Authenticity) ---
        Text(
            text = "Brokerage products: Not FDIC Insured • No Bank Guarantee • May Lose Value",
            style = InstitutionalTypography.copy(
                fontSize = 11.sp,
                color = ApexTextSecondary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun ApexTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: (Boolean) = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { 
            Text(
                text = placeholder, 
                style = InstitutionalTypography.copy(color = ApexTextSecondary)
            ) 
        },
        modifier = modifier,
        textStyle = InstitutionalTypography.copy(fontSize = 16.sp),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password",
                        tint = ApexTextSecondary
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = ApexTeal,
            focusedTextColor = ApexTextPrimary,
            unfocusedTextColor = ApexTextPrimary
        ),
        singleLine = true
    )
}

@Composable
private fun ApexSquareCheckbox(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .border(1.dp, if (checked) ApexTeal else ApexBorder, RoundedCornerShape(0.dp))
            .background(if (checked) ApexTeal else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

/**
 * Mock helper for Biometric Authentication.
 * In a real app, this would be part of an AuthManager or ViewModel.
 */
private fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Apex Wealth Management")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use Password")
        .build()

    // Wrap in try-catch as some devices may not have biometrics enrolled
    try {
        biometricPrompt.authenticate(promptInfo)
    } catch (_: Exception) {
        // Silently fail or log in a production app
    }
}

@Preview
@Composable
fun PreviewApexLogin() {
    MaterialTheme {
        ApexLoginScreen()
    }
}
