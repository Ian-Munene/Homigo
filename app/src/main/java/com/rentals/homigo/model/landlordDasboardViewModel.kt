package com.rentals.homigo.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.rentals.homigo.data.ApartmentUnit
import com.rentals.homigo.data.MaintenanceRequest
import com.rentals.homigo.data.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LandlordDashboardViewModel : ViewModel() {

    private val realtimeDb = FirebaseDatabase.getInstance().reference

    private val _tenants = MutableStateFlow<List<Tenant>>(emptyList())
    val tenants: StateFlow<List<Tenant>> = _tenants

    private val _requests = MutableStateFlow<List<MaintenanceRequest>>(emptyList())
    val requests: StateFlow<List<MaintenanceRequest>> = _requests

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val unitTypes = listOf("Bedsitter", "One Bedroom", "Two Bedroom", "Three Bedroom")
    var selectedType = mutableStateOf(unitTypes[0])

    private val _availableUnits = MutableStateFlow<List<ApartmentUnit>>(emptyList())
    val availableUnits: StateFlow<List<ApartmentUnit>> = _availableUnits

    private val _occupiedUnits = MutableStateFlow<List<ApartmentUnit>>(emptyList())
    val occupiedUnits: StateFlow<List<ApartmentUnit>> = _occupiedUnits


    fun loadDashboardData() {
        _isLoading.value = true
        fetchTenants()
        fetchRequests()
        loadAvailableUnits()
        loadOccupiedUnits()
    }

    fun fetchTenants() {
        realtimeDb.child("users")
//            .orderByChild("role").equalTo("tenant")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Tenant>()
                    for (userSnap in snapshot.children) {
                        val name = userSnap.child("fullName").getValue(String::class.java)
                        val apartment = userSnap.child("apartmentNumber").getValue(String::class.java)
                        val moveIn = userSnap.child("moveInDate").getValue(String::class.java)

                        if (name != null && apartment != null && moveIn != null) {
                            list.add(Tenant(name, apartment, moveIn))
                        }
                    }
                    _tenants.value = list
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RealtimeError", "Failed to load tenants: ${error.message}")
                    _isLoading.value = false
                }
            })
    }

    fun fetchRequests() {
        realtimeDb.child("maintenance_requests")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<MaintenanceRequest>()
                    for (reqSnap in snapshot.children) {
                        val tenant = reqSnap.child("tenantName").getValue(String::class.java)
                        val issue = reqSnap.child("issue").getValue(String::class.java)

                        if (tenant != null && issue != null) {
                            list.add(MaintenanceRequest(tenant, issue))
                        }
                    }
                    _requests.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RealtimeError", "Failed to load maintenance requests: ${error.message}")
                }
            })
    }

    fun loadAvailableUnits() {
        realtimeDb.child("apartment_units")
            .addValueEventListener(object : ValueEventListener {  // ✅ switch to addValueEventListener
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ApartmentUnit>()
                    for (aptSnap in snapshot.children) {
                        val unitId = aptSnap.key ?: ""
                        val unitNumber = aptSnap.child("unitNumber").getValue(String::class.java) ?: ""
                        val unitType = aptSnap.child("unitType").getValue(String::class.java) ?: ""
                        val rentValue = aptSnap.child("rentAmount").getValue(Int::class.java)
                            ?: aptSnap.child("rentAmount").getValue(Long::class.java)?.toInt() ?: 0

                        val apartmentUnit = ApartmentUnit(
                            unitId = unitId,
                            unitNumber = unitNumber,
                            unitType = unitType,
                            rentAmount = rentValue,
                            tenantName = null
                        )
                        list.add(apartmentUnit)
                    }
                    _availableUnits.value = list
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RealtimeError", "Failed to load available units: ${error.message}")
                    _isLoading.value = false
                }
            })
    }


    fun loadOccupiedUnits() {
        realtimeDb.child("occupied_units")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ApartmentUnit>()
                    for (aptSnap in snapshot.children) {
                        val unitId = aptSnap.key ?: "" // database key
                        val unitNumber = aptSnap.child("unitNumber").getValue(String::class.java) ?: ""
                        val unitType = aptSnap.child("unitType").getValue(String::class.java) ?: ""
                        val rentValue = aptSnap.child("rentAmount").getValue(Double::class.java)
                            ?: aptSnap.child("rentAmount").getValue(Long::class.java)?.toDouble() ?: 0.0
                        val tenantName = aptSnap.child("tenantName").getValue(String::class.java) ?: ""

                        val apartmentUnit = ApartmentUnit(
                            unitId = unitId,
                            unitNumber = unitNumber,
                            unitType = unitType,
                            rentAmount = rentValue,
                            tenantName = tenantName // occupied → tenant exists
                        )
                        list.add(apartmentUnit)
                    }
                    _occupiedUnits.value = list
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RealtimeError", "Failed to load occupied units: ${error.message}")
                    _isLoading.value = false
                }
            })
    }


}
