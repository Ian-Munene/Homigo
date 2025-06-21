package com.rentals.homigo.model

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rentals.homigo.data.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class TenantViewModel : ViewModel() {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _tenantData = MutableStateFlow<Tenant?>(null)
    val tenantData: StateFlow<Tenant?> = _tenantData

    fun fetchTenantData(userId: String) {
        db.child("users").child(userId).get().addOnSuccessListener { snapshot ->
            val tenant = snapshot.getValue(Tenant::class.java)
            _tenantData.value = tenant
        }.addOnFailureListener { exception ->
            // handle error (e.g., log it or show a message)
        }
    }
}