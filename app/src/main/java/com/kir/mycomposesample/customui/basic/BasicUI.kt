package com.kir.mycomposesample.customui.basic

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    basicClickable (indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
            onClick()
        }
    )
}

fun Modifier.basicClickable(
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    val multipleEventsCutter = remember { MultipleEventsCutterImpl.get() }
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = { multipleEventsCutter.processEvent(onClick) }
    )
}

//연속 클릭 방지
class MultipleEventsCutterImpl {
    companion object {
        private var instance : MultipleEventsCutterImpl? = null
        fun get() : MultipleEventsCutterImpl {
            return instance ?: synchronized(this) {
                instance ?: MultipleEventsCutterImpl().also{
                    instance = it
                }
            }
        }
    }
    private val clickTerm : Long = 200L
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= clickTerm) {
            event.invoke()
            lastEventTimeMs = now
        }
    }
    fun processEvent(offset : Offset, event : (Offset) -> Unit) {
        if (now - lastEventTimeMs >= clickTerm) {
            event.invoke(offset)
            lastEventTimeMs = now
        }
    }
}

@Composable
fun Dp.dpToSp(): TextUnit = with(LocalDensity.current) { this@dpToSp.toSp() }
@Composable
fun Dp.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.toPx() }
@Composable
fun Float.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }