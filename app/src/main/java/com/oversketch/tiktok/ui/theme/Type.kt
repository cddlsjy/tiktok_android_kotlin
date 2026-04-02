package com.oversketch.tiktok.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography styles mapped from Flutter's StandardTextStyle
 */
object TikTokTypography {
    val big = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = SysSize.big.sp,
        fontFamily = FontFamily.Default
    )

    val bigWithOpacity = TextStyle(
        color = ColorPlate.white66,
        fontWeight = FontWeight.SemiBold,
        fontSize = SysSize.big.sp,
        fontFamily = FontFamily.Default
    )

    val normalW = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = SysSize.normal.sp,
        fontFamily = FontFamily.Default
    )

    val normal = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = SysSize.normal.sp,
        fontFamily = FontFamily.Default
    )

    val normalWithOpacity = TextStyle(
        color = ColorPlate.white66,
        fontWeight = FontWeight.Normal,
        fontSize = SysSize.normal.sp,
        fontFamily = FontFamily.Default
    )

    val small = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = SysSize.small.sp,
        fontFamily = FontFamily.Default
    )

    val smallWithOpacity = TextStyle(
        color = ColorPlate.white66,
        fontWeight = FontWeight.Normal,
        fontSize = SysSize.small.sp,
        fontFamily = FontFamily.Default
    )
}

// Material3 Typography wrapper
val TikTokMaterialTypography = Typography(
    bodyLarge = TikTokTypography.normal,
    bodyMedium = TikTokTypography.small,
    titleLarge = TikTokTypography.big,
    labelLarge = TikTokTypography.normalW
)
