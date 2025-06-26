package com.rentals.homigo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rentals.homigo.model.TenantViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue


@Composable
fun TenantDashboardScreen(
    userName: String,
    tenantViewModel: TenantViewModel = viewModel(),
    navController: NavController
) {
    val tenantData by tenantViewModel.tenantData.collectAsState()
    val context = LocalContext.current


    val BackgroundGradient = Brush.verticalGradient(
        colors = listOf(DeepSapphire, StormBlue)
    )

    LaunchedEffect(userName) {
        tenantViewModel.fetchTenantData(userName)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = BackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .background(color = DeepSapphire)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "🏠 Welcome Back!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            tenantData?.let { tenant ->
                Text(
                    text = tenant.fullName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepSapphire.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("👤 Full Name: ${tenant.fullName}", color = StormBlue)
                        Text("Email: ${tenant.email}", color= StormBlue)
                        Text("🏢 Unit Number: ${tenant.unitNumber}", color = StormBlue)
                        Text("📅 Move-In Date: ${tenant.moveInDate}", color = StormBlue)
                    }
                }
            } ?: run {
                Text(
                    text = "Loading tenant details...",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "ACTIONS",
                style = MaterialTheme.typography.titleMedium.copy(color = StormBlue)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("make_maintenance_request/$userName") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StormBlue,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Maintenance Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Request Maintenance")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("payment_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StormBlue,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Payment Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Make Payment")
            }
        }
    }
}
