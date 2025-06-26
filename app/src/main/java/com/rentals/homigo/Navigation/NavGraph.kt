package com.rentals.homigo.Navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rentals.homigo.auth.ForgotPassword
import com.rentals.homigo.auth.LoginScreen
import com.rentals.homigo.auth.SignupScreen
import com.rentals.homigo.screens.AddApartmentScreen
import com.rentals.homigo.screens.HomeScreen
import com.rentals.homigo.screens.LandlordDashboardScreen
import com.rentals.homigo.screens.MaintenanceRequestScreen
import com.rentals.homigo.screens.SplashScreen
import com.rentals.homigo.screens.TenantDashboardScreen
import com.rentals.homigo.screens.TenantPaymentScreen


@Composable
fun HomigoNavHost(modifier: Modifier = Modifier,
               navController: NavHostController = rememberNavController(),
               startDestination: String= "splash_screen") {
    NavHost(
        navController = navController, modifier = modifier,
        startDestination = startDestination
    ) {
        composable("signup") {
            SignupScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable(
            route = "tenant_dashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TenantDashboardScreen(navController = navController, userName = userId)
        }
        composable("landlord_dashboard") {
            LandlordDashboardScreen(navController)
        }
        composable("add_unit") {
            AddApartmentScreen(navController)
        }
        composable(
            route = "make_maintenance_request/{tenantName}",
            arguments = listOf(navArgument("tenantName") { type = NavType.StringType })
        ) { backStackEntry ->
            val tenantName = backStackEntry.arguments?.getString("tenantName") ?: ""
            MaintenanceRequestScreen(
                tenantName = tenantName,
                navController = navController
            )
        }
        composable("payment_screen"){
            TenantPaymentScreen()
        }
        composable("splash_screen"){
            SplashScreen(onTimeout = {navController.navigate("home_screen")})
        }
        composable("home_screen"){
            HomeScreen(navController)
        }
        composable ("forgot_password"){
            ForgotPassword(
                onResetSuccess = { navController.navigate("login") },
                navController = navController
            )
        }

    }
}
