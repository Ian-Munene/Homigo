package com.rentals.homigo.model

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.rentals.homigo.data.ApartmentUnit

class ApartmentViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance().reference

    fun addApartment(unitNumber: String, unitType: String, rentAmount: Int) {
        val apartmentId = db.child("apartment_units").push().key ?: return
        val apartment = ApartmentUnit(unitId = apartmentId, unitNumber, unitType, rentAmount)
        db.child("apartment_units").child(apartmentId).setValue(apartment)
    }

}
