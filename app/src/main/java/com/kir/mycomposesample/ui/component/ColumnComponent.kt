package com.kir.mycomposesample.ui.component

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Column_SpaceBetween_Component() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        )
    {
        val modifier = Modifier.weight(1f)

        AllSpace(modifier, Color.Blue)
        AllSpace(modifier, Color.Yellow)
        AllSpace(modifier, Color.Red)

    }
}

@Composable
private fun AllSpace(modifier: Modifier, color: Color) {
    Spacer(
        modifier = modifier
            .background(color)
            .height(50.dp),

    )

}

@Preview
@Composable
private fun modifierCase() {

    Text(
        "the quick brown fox jumped over the lazy dogs",
        Modifier.widthIn(max = edgeWidth * 4)
            // Rendering to an offscreen buffer is required to get the faded edges' alpha to be
            // applied only to the text, and not whatever is drawn below this composable (e.g. the
            // window).
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                drawFadedEdge(leftEdge = true)
                drawFadedEdge(leftEdge = false)
            }
            .basicMarquee(
                // Animate forever.
                iterations = Int.MAX_VALUE,
                spacing = MarqueeSpacing(0.dp),
            )
            .padding(start = edgeWidth),
    )
}

val edgeWidth = 32.dp
fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush =
            Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startX = if (leftEdge) 0f else size.width,
                endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx,
            ),
        blendMode = BlendMode.DstIn,
    )
}

@Preview
@Composable
fun LaunchedEffectComparison() {
    var useNewState by remember { mutableStateOf(false) }

    // 버튼으로 listState 교체하기
    val listState = if (useNewState) {
        rememberLazyListState() // 새로운 객체
    } else {
        rememberLazyListState() // 첫 번째 객체
    }

    Column {
        Button(onClick = { useNewState = !useNewState }) {
            Text(if (useNewState) "기존 state로 교체" else "새 state로 교체")
        }

        Text("첫 번째: LaunchedEffect(Unit)")
        LaunchedEffect(Unit) {
            println("🔥 LaunchedEffect 실행됨 (listState 교체와 상관없음)")
            snapshotFlow { listState.firstVisibleItemIndex }
                .collect { index ->
                    println("LaunchedEffect: Unit Flow index = $index")
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("두 번째: LaunchedEffect(listState)")
        LaunchedEffect(listState) {
            println("🔥 LaunchedEffect(listState) 실행됨 (listState 바뀌면 다시 실행)")
            snapshotFlow { listState.firstVisibleItemIndex }
                .collect { index ->
                    println("LaunchedEffect(listState): listState Flow index = $index")
                }
        }

        LazyColumn(state = listState, modifier = Modifier.height(200.dp)) {
            items(50) { index ->
                Text("아이템 $index", modifier = Modifier.padding(8.dp))
            }
        }
    }
}