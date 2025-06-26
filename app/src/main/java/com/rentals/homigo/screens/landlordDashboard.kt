package com.rentals.homigo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.data.ApartmentUnit
import com.rentals.homigo.model.LandlordDashboardViewModel
import com.rentals.homigo.model.MaintenanceRequestViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LandlordDashboardScreen(
    navController: NavController,
    landlordViewModel: LandlordDashboardViewModel = viewModel(),
    maintenanceViewModel: MaintenanceRequestViewModel = viewModel()
) {
    val isLoading by landlordViewModel.isLoading.collectAsState()
    val requests by maintenanceViewModel.allRequests.collectAsState()
    val availableUnits by landlordViewModel.availableUnits.collectAsState()
    val occupiedUnits by landlordViewModel.occupiedUnits.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<ApartmentUnit?>(null) }
    var editedRent by remember { mutableStateOf("") }
    var editedType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        landlordViewModel.loadDashboardData()
        landlordViewModel.loadAvailableUnits()
        landlordViewModel.loadOccupiedUnits()
        maintenanceViewModel.fetchAllRequests()
    }

    val headerColor = Color(0xFF082567)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_unit") },
                containerColor = headerColor,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Apartment")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DeepSapphire)
        ) {
            Text(
                text = "🏢 LANDLORD DASHBOARD",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = StormBlue,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = headerColor)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Apartment Units
                    item {
                        SectionHeader("🏠 Apartment Units")
                    }
                    if (availableUnits.isEmpty()) {
                        item { EmptyText("No available units.") }
                    } else {
                        items(availableUnits) { unit ->
                            InfoCard {
                                Text("Unit Number: ${unit.unitNumber ?: "N/A"}")
                                Text("Type: ${unit.unitType ?: "N/A"}")
                                Text("Rent: KES ${unit.rentAmount ?: "N/A"}")

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Button(onClick = {
                                        selectedUnit = unit
                                        editedRent = (unit.rentAmount ?: "").toString()
                                        editedType = unit.unitType ?: ""
                                        showEditDialog = true
                                    }) {
                                        Text("Edit")
                                    }

                                    Button(onClick = {
                                        landlordViewModel.deleteApartmentUnit(unit.unitId)
                                    }) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }

                    }

                    // function to load occupied units
                    item {
                        SectionHeader("👥 Occupied Units")
                    }
                    if (occupiedUnits.isEmpty()) {
                        item { EmptyText("No occupied units.") }
                    } else {
                        items(occupiedUnits) { unit ->
                            InfoCard {
                                Text("Unit Number: ${unit.unitNumber ?: "N/A"}")
                                Text("Tenant Name: ${unit.tenantName ?: "N/A"}")

                                Button(onClick = {
                                    landlordViewModel.deleteOccupiedUnit(unit.unitId)
                                }, modifier = Modifier.padding(top = 8.dp)) {
                                    Text("Delete")
                                }
                            }
                        }


                    }

                    // Maintenance Requests
                    item {
                        SectionHeader("🛠️ Maintenance Requests")
                    }
                    if (requests.isEmpty()) {
                        item { EmptyText("No maintenance requests yet.") }
                    } else {
                        items(requests) { req ->
                            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                .format(Date(req.timestamp))

                            InfoCard {
                                Text("Tenant: ${req.tenantName}")
                                Text("Description: ${req.description}")
                                Text("Date: $formattedDate")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog && selectedUnit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                Button(onClick = {
                    landlordViewModel.editApartmentUnit(
                        unitId = selectedUnit!!.unitId,
                        updatedRent = editedRent,
                        updatedType = editedType)
                    showEditDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Edit Apartment Unit") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedRent,
                        onValueChange = { editedRent = it },
                        label = { Text("Rent") }
                    )
                    OutlinedTextField(
                        value = editedType,
                        onValueChange = { editedType = it },
                        label = { Text("Type") }
                    )
                }
            }
        )
    }

}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            color = White,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}

@Composable
fun EmptyText(message: String) {
    Text(
        text = message,
        color = StormBlue,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DeepSapphire,
            contentColor = StormBlue),
        elevation = CardDefaults.cardElevation(9.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            content()
        }
    }
}
