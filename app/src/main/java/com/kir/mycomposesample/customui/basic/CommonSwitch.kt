package com.kir.mycomposesample.customui.basic

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


@Composable
fun CommonSwitch(
    checked: MutableState<Boolean>,
    onPreCheckCallback: (() -> Unit)? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width: Dp = 46.dp,
    height: Dp = 26.dp,
    iconId: Int? = null,
    ignoreDim: Boolean = false,
    gapBetweenThumbAndTrackEdge: Dp = 2.dp,
    checkedThumbColor: Color = Color.Unspecified,
    checkedBorderColor: Color = Color.Unspecified,
    checkedTrackColor: Color = Color.Unspecified,
    checkedIconColor: Color? = null,
    uncheckedThumbColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    uncheckedIconColor: Color? = null,
) {
    val dCheckedThumbColor = if (checkedThumbColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurface
    } else {
        checkedThumbColor
    }

    val dCheckedBorderColor = if (checkedBorderColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        checkedBorderColor
    }

    val dCheckedTrackColor = if (checkedTrackColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        checkedTrackColor
    }

    val dUncheckedThumbColor = if (uncheckedThumbColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurface
    } else {
        uncheckedThumbColor
    }

    val dUncheckedBorderColor = if (uncheckedBorderColor == Color.Unspecified) {
//            MaterialTheme.colorScheme.onPrimary
            MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        uncheckedBorderColor
    }

    val dUncheckedTrackColor = if (uncheckedTrackColor == Color.Unspecified) {
//            MaterialTheme.colorScheme.onPrimary
            MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        uncheckedTrackColor
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    val density = LocalDensity.current
    // To move thumb, we need to calculate the position (along x axis)

    val thumbOnXPosition = with(density) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
    val thumbOffXPosition = with(density) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }

    val animatePosition = animateFloatAsState(
        targetValue =
            if (checked.value) thumbOnXPosition
            else thumbOffXPosition,
        animationSpec = tween(durationMillis = 50, easing = LinearEasing),
        label = "",
    )
    var painter: VectorPainter? = null
    if (iconId != null) {
        painter = rememberVectorPainter(image = ImageVector.vectorResource(id = iconId))
    }

    val iconColorFilter = if (checked.value && checkedIconColor != null) ColorFilter.tint(checkedIconColor)
    else if (!checked.value && uncheckedIconColor != null) ColorFilter.tint(uncheckedIconColor)
    else null

    Canvas(
        modifier = modifier
            .alpha(if (enabled || ignoreDim) 1.0f else 0.5f)
            .size(width = width, height = height)
            .noRippleClickable {
                onPreCheckCallback?.invoke()
                if (enabled) {
                    checked.value = !checked.value
                    onCheckedChange?.invoke(checked.value)
                }
            }
    ) {
        // background
        val trackRadius = thumbRadius.toPx() + gapBetweenThumbAndTrackEdge.toPx() * 2
        drawRoundRect(
            color = if (checked.value) dCheckedBorderColor else dUncheckedBorderColor,
            cornerRadius = CornerRadius(x = trackRadius, y = trackRadius),
        )
        // Track
        drawRoundRect(
            color = if (checked.value) dCheckedTrackColor else dUncheckedTrackColor,
            cornerRadius = CornerRadius(x = trackRadius, y = trackRadius),
            style = Stroke(width = 2.dp.toPx())
        )

        // Thumb
        drawCircle(
            color = if (checked.value) dCheckedThumbColor else dUncheckedThumbColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
        if (painter != null) {
            translate(
                left = animatePosition.value - (painter.intrinsicSize.width / 2),
                top = size.height / 2 - (painter.intrinsicSize.height / 2)
            )
            {
                with(painter) {
                    draw(
                        size = painter.intrinsicSize,
                        colorFilter = iconColorFilter
                    )
                }
            }
        }
    }
}
