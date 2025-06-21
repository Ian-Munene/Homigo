package com.rentals.homigo.Navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rentals.homigo.auth.LoginScreen
import com.rentals.homigo.auth.SignupScreen
import com.rentals.homigo.screens.AddApartmentScreen
import com.rentals.homigo.screens.HomeScreen
import com.rentals.homigo.screens.LandlordDashboardScreen


@Composable
fun AppNavHost(modifier: Modifier = Modifier,
               navController: NavHostController = rememberNavController(),
               startDestination: String= ROUTE_SPLASH_SCREEN) {
    NavHost(
        navController = navController, modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_TENANT_SIGNUP) {
            SignupScreen(navController)
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController)
        }
//        composable(
//            route = "tenant_dashboard/{userId}",
//            arguments = listOf(navArgument("userId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val userId = backStackEntry.arguments?.getString("userId") ?: ""
//            TenantDashboardScreen(navController = navController, userName = userId)
//        }
        composable(ROUTE_LANDLORD_DASHBOARD) {
            LandlordDashboardScreen(navController)
        }
        composable(ROUTE_ADD_APARTMENT) {
            AddApartmentScreen(navController)
        }
//        composable(
//            route = "make_maintenance_request/{tenantName}",
//            arguments = listOf(navArgument("tenantName") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val tenantName = backStackEntry.arguments?.getString("tenantName") ?: ""
//            MaintenanceRequestScreen(
//                tenantName = tenantName,
//                navController = navController
//            )
//        }
//        composable(ROUTE_PAYMENT_SCREEN){
//            TenantPaymentScreen()
//        }
//        composable(ROUTE_SPLASH_SCREEN){
//            SplashScreen(navController)
//        }
        composable(ROUTE_HOME_SCREEN){
            HomeScreen(navController)
        }

    }
}
