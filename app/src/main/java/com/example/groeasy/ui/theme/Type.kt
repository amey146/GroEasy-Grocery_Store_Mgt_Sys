package com.example.groeasy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.groeasy.R

val aliceRegularFont = FontFamily(
    Font(R.font.alice)
)
val greatvibesRegularfont = FontFamily(
    Font(R.font.great)
)
val nunitoRegularfont = FontFamily(
    Font(R.font.nunito)
)
val quicksandRegularfont = FontFamily(
    Font(R.font.quicksand)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = aliceRegularFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)