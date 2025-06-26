package com.rentals.homigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.rentals.homigo.Navigation.HomigoNavHost
import com.rentals.homigo.ui.theme.HomigoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // initialize firebase for the app
        FirebaseApp.initializeApp(this)
        setContent {
            HomigoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                HomigoNavHost()
            }
        }
    }


}
