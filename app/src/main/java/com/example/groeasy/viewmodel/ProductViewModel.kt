package com.example.groeasy.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.groeasy.model.Product

class ProductViewModel : ViewModel() {
    private var myproduct by mutableStateOf<Product?>(null)

    fun setProduct(product: Product) {
        myproduct = product
    }

    fun getProduct(): Product? {
        return myproduct
    }
}