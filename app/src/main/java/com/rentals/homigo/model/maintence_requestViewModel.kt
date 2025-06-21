package com.rentals.homigo.model

import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.rentals.homigo.data.MaintenanceRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MaintenanceRequestViewModel : ViewModel() {

    private val db: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("maintenance_requests")

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting

    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess: StateFlow<Boolean> = _submitSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _allRequests = MutableStateFlow<List<MaintenanceRequest>>(emptyList())
    val allRequests: StateFlow<List<MaintenanceRequest>> = _allRequests

    fun submitMaintenanceRequest(tenantName: String, description: String) {
        _isSubmitting.value = true
        _errorMessage.value = null

        val requestId = db.push().key ?: return

        val request = MaintenanceRequest(
            requestId = requestId,
            tenantName = tenantName,
            description = description
        )

        db.child(requestId).setValue(request)
            .addOnCompleteListener { task ->
                _isSubmitting.value = false
                if (task.isSuccessful) {
                    _submitSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun fetchAllRequests() {
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MaintenanceRequest>()
                for (child in snapshot.children) {
                    val req = child.getValue(MaintenanceRequest::class.java)
                    if (req != null) {
                        list.add(req)
                    }
                }
                _allRequests.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
            }
        })
    }

    fun resetSubmitState() {
        _submitSuccess.value = false
    }
}
