package com.example.myapplication

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
        }



        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Kayıt başarılıysa giriş ekranına yönlendir
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true } // geri tuşuyla dönmeyi engeller
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
    }
}
