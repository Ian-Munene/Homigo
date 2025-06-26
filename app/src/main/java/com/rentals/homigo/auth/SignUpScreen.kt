package com.rentals.homigo.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.model.UserAuthViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SignupScreen(navController: NavController, userAuthViewModel: UserAuthViewModel = viewModel()) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var selectedUnit by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown

    val isLoading by userAuthViewModel.isLoading.collectAsState()
    val signupSuccess by userAuthViewModel.signupSuccess.collectAsState()
    val availableUnits by userAuthViewModel.availableUnits.collectAsState()
    val context = LocalContext.current
//
//    // Custom colors
//    val DeepSapphire = C(0xFF082567)
//    val StormBlue = Color(0xFF4A646C)

    LaunchedEffect(signupSuccess) {
        if (signupSuccess) {
            Toast.makeText(context, "Signup successful!", Toast.LENGTH_LONG).show()
            navController.navigate("tenant_dashboard/${userAuthViewModel.currentUserId.value}")
            userAuthViewModel.resetSignupSuccess()
        }
    }

    LaunchedEffect(Unit) {
        userAuthViewModel.fetchAvailableUnits()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StormBlue.copy(alpha = 0.05f)  // subtle background tint
    ) {
        Column(
            modifier = Modifier
                .background(color = DeepSapphire)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSapphire.copy(alpha = 0.05f)),

                ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Create an Account",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = StormBlue,
                            fontSize = 22.sp
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name",color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full name",tint = StormBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box {
                        OutlinedTextField(
                            value = selectedUnit,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Unit Number",color = StormBlue) },
                            trailingIcon = {
                                Icon(icon, "Dropdown Icon", Modifier.clickable { expanded = !expanded })
                            },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = "Select house",tint = StormBlue) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (availableUnits.isNotEmpty()) {
                                availableUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            selectedUnit = unit
                                            expanded = false
                                        }
                                    )
                                }
                            } else {
                                DropdownMenuItem(
                                    text = { Text("No Apartment Units Yet") },
                                    onClick = { expanded = false }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number",color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = "Phone Number",tint = StormBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = moveInDate,
                        onValueChange = { moveInDate = it },
                        label = { Text("Move-In Date (DD/MM/YYYY)",color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Move in Date",tint = StormBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email",color = StormBlue)},
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email",tint = StormBlue) },
                        modifier = Modifier.fillMaxWidth(),keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)

                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password",color = StormBlue) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password",tint = StormBlue) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (fullName.isBlank() || selectedUnit.isBlank() || phoneNumber.isBlank()
                                || moveInDate.isBlank() || email.isBlank() || password.isBlank()
                            ) {
                                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            if (availableUnits.contains(selectedUnit)) {
                                userAuthViewModel.registerUser(fullName, selectedUnit, phoneNumber, moveInDate, email, password)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Selected unit is already occupied. Please choose another.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StormBlue,
                            contentColor = Color.White
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Sign Up")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Already have an account? Click Login",
                        style = MaterialTheme.typography.bodyMedium.copy(color = StormBlue),
                        modifier = Modifier
                            .clickable { navController.navigate("login") }
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}


