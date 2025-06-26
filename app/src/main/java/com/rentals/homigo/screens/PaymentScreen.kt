package com.rentals.homigo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rentals.homigo.model.MpesaViewModel
import com.rentals.homigo.ui.theme.DeepSapphire
import com.rentals.homigo.ui.theme.StormBlue

@Composable
fun TenantPaymentScreen(mpesaViewModel: MpesaViewModel = viewModel()) {
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var paymentReason by remember { mutableStateOf("Rent") }
    var isProcessing by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf<Boolean?>(null) }
    val reasons = listOf("Rent", "Garbage Collection","Water")
    var expanded by remember { mutableStateOf(false) }

//    val backgroundGradient = Brush.verticalGradient(
//        colors = listOf(Color(0xFF082567), Color(0xFF4A646C))
//    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color =  DeepSapphire)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSapphire.copy(alpha = 0.05f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "💸 Make a Payment",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = StormBlue
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number (e.g. 254XXXXXXXXX)",color = StormBlue) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount",color = StormBlue) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedTextField(
                        value = paymentReason,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Payment Type",color = StormBlue) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = true }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        reasons.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    paymentReason = it
                                    expanded = false
                                },
                                text = { Text(it) }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        isProcessing = true
                        mpesaViewModel.initiatePayment(phoneNumber, amount) { success ->
                            isProcessing = false
                            paymentSuccess = success
                        }
                    },
                    enabled = phoneNumber.isNotBlank() && amount.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StormBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Pay Now")
                }

                if (isProcessing) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF082567))
                    }
                }

                paymentSuccess?.let { success ->
                    val message = if (success) "✅ Payment Request Sent!" else "❌ Payment Failed. Try Again."
                    val color = if (success) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                    Text(
                        text = message,
                        color = color,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}