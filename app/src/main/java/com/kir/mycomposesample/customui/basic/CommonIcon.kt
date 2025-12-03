package com.kir.mycomposesample.customui.basic

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CommonIcon(
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = "",
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    var dTint = tint
    if(tint == Color.Unspecified) {
//        dTint = if(isInverse) MaterialTheme.colorScheme.inverseOnSurface
//                else MaterialTheme.colorScheme.onSurface
        dTint = MaterialTheme.colorScheme.onSurface
    }

    if(imageVector != null) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = dTint
        )
    }
    if(painter != null) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = dTint
        )
    }
}