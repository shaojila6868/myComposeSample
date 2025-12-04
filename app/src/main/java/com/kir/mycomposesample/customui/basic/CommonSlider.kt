package com.kir.mycomposesample.customui.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonSlider(
    value : MutableState<Float>,
    valueSetCallback : (() -> Unit)? = null,
    range : ClosedFloatingPointRange<Float> = 0f..100f,
    step : Int = 4,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    activeTrackColor : Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor : Color = MaterialTheme.colorScheme.onSecondaryContainer,
    thumbColor : Color = MaterialTheme.colorScheme.onSurface,
    activeTickColor : Color = MaterialTheme.colorScheme.tertiary,
    inactiveTickColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    val thumbSize = 25.dp
    val sliderValue = remember { mutableStateOf(value.value) }

    LaunchedEffect(value.value) {
        sliderValue.value = value.value
    }

    Slider(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = -thumbSize / 2),
        value = sliderValue.value,
        onValueChange = {
            sliderValue.value = it
        },
        steps = step,
        onValueChangeFinished = {
            value.value = sliderValue.value
            valueSetCallback?.invoke()
        },
        enabled = enabled,
        valueRange = range,
        track = {
            Box(modifier = Modifier) {
                val trackSize = remember {mutableStateOf(IntSize.Zero) }
                val trackHeight = 10.dp
                SliderDefaults.Track(
                    sliderState = it,
//                    thumbTrackGapSize = 0.dp,
                    modifier = Modifier
                        .onGloballyPositioned {
                            trackSize.value = it.size
                        }
                        .fillMaxWidth()
                        .height(100.dp), // 이건 버그다.아무리 설정해도 이미 내부에서 지정하였기떄문이다.
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeTrackColor,
                        inactiveTrackColor = inactiveTrackColor,
                        activeTickColor = activeTickColor,
                        inactiveTickColor = inactiveTickColor,
                        disabledActiveTickColor = activeTickColor,
                        disabledInactiveTickColor = inactiveTickColor
                    ),
                )

                // 마지막 tick 색깔이 track의 active색과 동일하게 떨어지도록 설계됨.
                // 덮어씌우도록 tick 하나를 따로 그림.
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    drawCircle(
                        color = inactiveTickColor,
                        radius = 2.dp.toPx(),
                        center = Offset(trackSize.value.width.toFloat() - trackHeight.toPx() / 2, trackSize.value.height.toFloat() / 2)
                    )
                }
            }
        },
        thumb = {
            val modifier = if (thumbColor == Color.Gray) {
                Modifier
                    .size(thumbSize)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(50), clip = false)
            } else {
                Modifier
                    .size(thumbSize)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(50), clip = false)
                    .background(color = Color.White, shape = RoundedCornerShape(50))
            }
            CommonIcon(
                imageVector = Icons.Filled.Info,
                tint = thumbColor,
                contentDescription = "",
                modifier = modifier
            )
        },
    )
}
