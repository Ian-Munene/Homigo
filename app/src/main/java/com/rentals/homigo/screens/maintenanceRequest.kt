package com.rentals.homigo.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.model.MaintenanceRequestViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue

@Composable
fun MaintenanceRequestScreen(
    tenantName: String,
    navController: NavController,
    maintenanceViewModel: MaintenanceRequestViewModel = viewModel()
) {
    var description by remember { mutableStateOf("") }
    val isSubmitting by maintenanceViewModel.isSubmitting.collectAsState()
    val submitSuccess by maintenanceViewModel.submitSuccess.collectAsState()
    val errorMessage by maintenanceViewModel.errorMessage.collectAsState()

    if (submitSuccess) {
        LaunchedEffect(Unit) {
            maintenanceViewModel.resetSubmitState()
            navController.popBackStack()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DeepSapphire)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(color = DeepSapphire)

        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Maintenance",
                tint = StormBlue,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Report a Maintenance Issue",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center


            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSapphire.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Describe the issue clearly",color = StormBlue) },
                        placeholder = { Text("e.g., electricity issue ") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            maintenanceViewModel.submitMaintenanceRequest(tenantName, description)
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StormBlue,
                            contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)


                    ) {
                        Text(if (isSubmitting) "Submitting..." else "Submit Request")
                    }

                    errorMessage?.let {
                        Text(
                            "⚠️ $it",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
