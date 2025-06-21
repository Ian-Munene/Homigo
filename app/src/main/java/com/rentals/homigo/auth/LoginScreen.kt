package com.rentals.homigo.auth


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.model.UserAuthViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue


@Composable
fun LoginScreen(navController: NavController, userAuthViewModel: UserAuthViewModel = viewModel()) {

//    val DeepSapphire = Color(0xFF082567)
//    val StormBlue = Color(0xFF4A646C)
//    val White = Color.White

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by userAuthViewModel.isLoading.collectAsState()
    val errorMessage by userAuthViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color =
            DeepSapphire
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(color = StormBlue)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = StormBlue.copy(alpha = 0.05f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = StormBlue) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = StormBlue) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                                    return@Button
                                }
                                userAuthViewModel.login(email, password) { success, uid ->
                                    if (success) {
                                        navController.navigate("tenant_dashboard/$uid")
                                    } else {
                                        Toast.makeText(context, "Tenant login failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StormBlue,
                                contentColor = White
                            )
                        ) {
                            Text("Tenant Login")
                        }

                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                if (email == "muneneian1964@gmail.com") {
                                    userAuthViewModel.login(email, password) { success, _ ->
                                        if (success) {
                                            navController.navigate(ROUTE_LANDLORD_DASHBOARD)
                                        } else {
                                            Toast.makeText(context, "Landlord login failed", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "This account is not a landlord", Toast.LENGTH_LONG).show()
                                }
                            },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StormBlue,
                                contentColor = White
                            )
                        ) {
                            Text("Landlord Login")
                        }
                    }

                    errorMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "⚠️ $msg",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
