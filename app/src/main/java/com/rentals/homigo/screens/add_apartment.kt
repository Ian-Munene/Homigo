package com.rentals.homigo.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.model.ApartmentViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddApartmentScreen(
    navController: NavController,
    apartmentViewModel: ApartmentViewModel = viewModel()
) {
    var unitNumber by remember { mutableStateOf("") }
    var rentAmount by remember { mutableStateOf("") }
    var unitType by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSapphire),
                title = { Text("Add Apartment", color = StormBlue, fontSize = 20.sp)},
                navigationIcon = {
                    Icon(Icons.Default.Home, contentDescription = "Apartment Icon",tint = StormBlue)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = DeepSapphire)
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = unitNumber,
                onValueChange = { unitNumber = it },
                label = { Text("Unit Number",color = StormBlue) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = rentAmount,
                onValueChange = { rentAmount = it },
                label = { Text("Rent Amount (KES)",color = StormBlue) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = unitType,
                onValueChange = { unitType = it },
                label = { Text("Unit Type (e.g. 1BR, Bedsitter)",color = StormBlue) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (unitNumber.isBlank() || rentAmount.isBlank() || unitType.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val rent = rentAmount.toIntOrNull()
                    if (rent == null || rent <= 0) {
                        Toast.makeText(context, "Invalid rent amount", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    apartmentViewModel.addApartment(unitNumber, unitType, rent)
                    Toast.makeText(context, "Apartment Added Successfully!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = StormBlue)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save",tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Unit", color = Color.White)
            }
        }
    }
}
