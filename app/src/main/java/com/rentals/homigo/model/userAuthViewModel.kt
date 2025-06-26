package com.rentals.homigo.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rentals.homigo.data.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserAuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

//    private val _currentUserId = MutableStateFlow<String?>(null)
//    val currentUserId: StateFlow<String?> = _currentUserId

    val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> get() = _currentUserId


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _tenantData = MutableStateFlow<Tenant?>(null)
    val tenantData: StateFlow<Tenant?> = _tenantData

    private val _availableUnits = MutableStateFlow<List<String>>(emptyList())
    val availableUnits: StateFlow<List<String>> = _availableUnits

    fun registerUser(
        fullName: String,
        unitNumber: String,
        phoneNumber: String,
        moveInDate: String,
        email: String,
        password: String
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    val userMap = mapOf(
                        "uid" to uid,
                        "fullName" to fullName,
                        "unitNumber" to unitNumber,
                        "phoneNumber" to phoneNumber,
                        "moveInDate" to moveInDate,
                        "email" to email
                    )

                    // Save user info under users/{uid}
                    db.child("users").child(uid).setValue(userMap)
                        .addOnSuccessListener {
                            Log.d("Signup", "User saved successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Signup", "Failed to save user: ${e.message}")
                        }

                    // Fetch full unit details from apartment_units
                    db.child("apartment_units").child(unitNumber)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val occupiedUnitData = mutableMapOf<String, Any>()

                                if (snapshot.exists()) {
                                    snapshot.children.forEach { child ->
                                        val key = child.key ?: return@forEach
                                        val value = child.value ?: return@forEach
                                        occupiedUnitData[key] = value
                                    }
                                }

                                // Add tenant info to the occupied unit
                                occupiedUnitData["tenantName"] = fullName
                                occupiedUnitData["tenantUid"] = uid
                                occupiedUnitData["unitNumber"] = snapshot.child("unitNumber").getValue(String::class.java) ?: unitNumber
                                occupiedUnitData["unitType"] = snapshot.child("unitType").getValue(String::class.java) ?: ""
                                occupiedUnitData["rentAmount"] = snapshot.child("rentAmount").getValue(Double::class.java)
                                    ?: snapshot.child("rentAmount").getValue(Long::class.java)?.toDouble() ?: 0.0

                                db.child("occupied_units").child(unitNumber).setValue(occupiedUnitData)
                                    .addOnSuccessListener {
                                        db.child("apartment_units").child(unitNumber).removeValue()
                                            .addOnSuccessListener {
                                                Log.d("Signup", "Unit moved to occupied_units")
                                                fetchAvailableUnits()  // Refresh units
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Signup", "Failed to remove from apartment_units: ${e.message}")
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Signup", "Failed to write to occupied_units: ${e.message}")
                                    }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Signup", "Failed to read apartment unit: ${error.message}")
                            }
                        })

                    _currentUserId.value = uid
                    _signupSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.localizedMessage
                    Log.e("Signup", "Signup failed: ${task.exception?.message}")
                }
            }
    }


    fun resetSignupSuccess() {
        _signupSuccess.value = false
    }


    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        _currentUserId.value = uid
                        fetchTenantData(uid)  // ✅ also fetch tenant data on login
                        onResult(true, uid)
                    } else {
                        _errorMessage.value = "Failed to retrieve user ID"
                        onResult(false, null)
                    }
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                    onResult(false, null)
                }
            }
    }

    fun fetchTenantData(userId: String) {
        db.child("tenants").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tenant = snapshot.getValue(Tenant::class.java)
                _tenantData.value = tenant
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
            }
        })
    }

    fun fetchAvailableUnits() {
        db.child("apartment_units").orderByChild("tenantName").equalTo(null)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val units = mutableListOf<String>()
                    for (unitSnapshot in snapshot.children) {
                        val unitNumber = unitSnapshot.child("unitNumber").getValue(String::class.java)
                        if (!unitNumber.isNullOrEmpty()) {
                            units.add(unitNumber)
                        }
                    }
                    _availableUnits.value = units
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                }
            })
    }




}