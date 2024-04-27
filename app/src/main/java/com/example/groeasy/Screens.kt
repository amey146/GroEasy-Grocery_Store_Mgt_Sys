package com.example.groeasy

sealed class Screens(val screen: String) {
    data object HomeScreen: Screens("home")
    data object NotifScreen: Screens("notifications")
    data object AddProduct: Screens("addproduct")
    data object ViewProducts: Screens("viewproduct")
    data object ProductDetailScreen: Screens("productdetail")
    data object ProductUpdateScreen: Screens("productupdate")
}