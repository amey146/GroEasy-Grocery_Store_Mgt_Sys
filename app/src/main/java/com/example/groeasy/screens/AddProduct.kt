package com.example.groeasy.screens

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.groeasy.Screens
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.model.Product
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun AddProduct() {
    val context = LocalContext.current
    val dbhelper = DatabaseHelper.getDb(context)
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val isDarkTheme = isSystemInDarkTheme() // Determine if the theme is dark
            val iconColor = if (isDarkTheme) Color.White else Color.Black
            
            Image(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        Form(dbhelper, context)
    }
}


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Form(dbhelper: DatabaseHelper, context: Context) {
    val param1 = simpleOutlinedTextFieldSample("Name", KeyboardType.Text)
    val param2 = simpleOutlinedTextFieldSample("Description", KeyboardType.Text)
    val param4 = dropDownMenu()
    val param3: String = if (param4 == "Groceries") {
        "---"
    } else {
        simpleOutlinedTextFieldSample("Brand", KeyboardType.Text)
    }
    val param5 = simpleOutlinedTextFieldSample("Price", KeyboardType.Number)
    val param7 = simpleOutlinedTextFieldSample("Quantity", KeyboardType.NumberPassword)
    val param6 = simpleOutlinedTextFieldSample("Weight per unit(kg/ltr)", KeyboardType.Number)

    val param8 = showDatePicker()
    Button(
        onClick = {
            val validity = check(param1, param2, param3, param4, param5, param6, param7)
            if (validity == -1) {
                val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                builder1.setMessage("Please fill up all the details.")
                builder1.setCancelable(true)

                builder1.setPositiveButton(
                    "OK"
                ) { dialog, _ -> dialog.cancel() }

                val alert11: AlertDialog = builder1.create()
                alert11.show()
            } else {
                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    dbhelper.productDao().upsertProduct(
                        Product(
                            0,
                            param1,
                            param2,
                            param3,
                            param4,
                            param5.toFloat(),
                            param6.toFloat(),
                            param7.toInt(),
                            param8
                        )
                    )
                }
                HS.getNav().navigate(Screens.AddProduct.screen) {
                    popUpTo(0)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text("Submit")
    }
    Spacer(modifier = Modifier.height(20.dp))
}

fun check(
    param1: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
    param6: String,
    param7: String
): Int {
    if (param1.isEmpty() || param2.isEmpty() || param3.isEmpty() || param4.isEmpty() || param5.isEmpty() || param6.isEmpty() || param7.isEmpty()) {
        return -1
    }
    return 0
}

@Composable
fun simpleOutlinedTextFieldSample(label: String, keyboardType: KeyboardType): String {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
    return text
}


@Composable
fun dropDownMenu(): String {
    var expanded by remember { mutableStateOf(false) }
    val suggestions =
        listOf("Groceries", "Bakery", "Snacks", "Dairy", "Households", "Frozen-Foods", "Others")
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp, 10.dp)) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { newText ->
                selectedText = newText
                if (!expanded) {
                    expanded = true // Expand dropdown when typing
                }
                // Auto-complete if input matches any item in suggestions list
                val matchedItem = suggestions.find { it.contains(newText, ignoreCase = true) }
                if (matchedItem != null) {
                    selectedText = matchedItem
                }
                // Check if selectedText is not in suggestions list
                else if (!suggestions.contains(selectedText)) {
                    selectedText = "Others" // Set default value to "Others"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedText = label
                        expanded = false
                    }
                )
            }
        }
    }
    return selectedText
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDatePicker(): Long {
    val datePickerState = rememberDatePickerState()
    DatePicker(
        datePickerState,
        modifier = Modifier
            .fillMaxWidth()
            .scale(0.9f),
        title = {
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "Expiry Date: ", fontSize = 20.sp)
        },
    )
    return datePickerState.selectedDateMillis ?: System.currentTimeMillis()
}

@Preview
@Composable
fun CallPreview() {
    AddProduct()
}
