package com.kir.mycomposesample.labratory.scroller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

//카드 선택 기
@Preview
@Composable
fun WheelPickerPreviewFixed() {
    val items = (1..7).map { "아이템 $it" }
    var selected by remember { mutableStateOf(items.first()) }

    Column(Modifier.fillMaxSize().padding(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(40.dp))

        WheelPicker(items = items, visibleCount = 3, itemHeight = 48.dp) {
            selected = it
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("선택됨: $selected", fontSize = 18.sp)
    }
}

/**
 * 스크롤으로 아이템 선택되는거, 스크롤 후 최근에 있는 아이템이 중간으로 선택됨
 */
@Composable
fun WheelPicker(
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleCount: Int = 3,
    itemHeight: Dp = 48.dp,
    onSelected: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf 0

            val viewportCenter = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

            val item = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                kotlin.math.abs(itemCenter - viewportCenter)
            }

            item?.index ?: 0
        }
    }

    // 값 변경 콜백
    LaunchedEffect(centerItemIndex) {
        if (items.isNotEmpty()) {
            onSelected(items[centerItemIndex])
        }
    }

    /*
     * ⭐ 핵심!
     * 스크롤이 멈추면 → 가장 가까운 아이템(centerItemIndex)으로 자동 이동
     */
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && items.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(centerItemIndex)
            }
        }
    }

    Box(modifier = modifier.height(itemHeight * visibleCount)) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .background(Color(0x220000FF), shape = RoundedCornerShape(8.dp))
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight * (visibleCount / 2))
        ) {
            items(items.size) { index ->
                val isCenter = index == centerItemIndex
                val fontSize = if (isCenter) 20.sp else 16.sp
                val alpha = if (isCenter) 1f else 0.5f

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        fontSize = fontSize,
                        color = Color.Black.copy(alpha = alpha),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
