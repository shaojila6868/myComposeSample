package com.kir.mycomposesample.ui.remember

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull


class RememberLazyListStateComponent {
}

/**
 * snapshotFlow에 대해 생각해보자.
 *  우선 LaunchedEffect를 사용하여 snapshotFlow(suspend(Async)를 사용위해) 사용한다.
 *  snapshotFlow는 Compose의 Stae를 Flow로 전환해준다.
 *      이게 뭐냐면 Flow는 관찰하여(observer) 값이 변경되었을 때 observer로 들어온다.
 *      collect혹은 collectlatest에 들어온다.
 *    결론적으로 설명해보면 72라인 listState가 있고,사용자가 스크롤 될 때 remember + state 이 작동하면서
 *    listState가 변경되고( 그러면 Flow에서 listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index를 관찰했기 때문에
 *    해당 값이 변경되면서 다시 collectLatest에 호출된다.
 *    그려면 이제 items의 값을 변경하면 다시 reComposition이 일어난다.
 *      여기서 중효한게 LaunchedEffect를 사용하는 목적은 snapshotFlow은 suspend(async)고 이럴때  LaunchedEffect 사용해야만
 *      item값을 부여했을 때 reComposition이 된다.
 *
 *
 */
@Preview
@Composable
fun InfiniteLazyList() {
    val listState = rememberLazyListState()
    var items by remember { mutableStateOf((0..30).toList()) } // 초기 데이터
    val coroutineScope = rememberCoroutineScope()

    // 스크롤 상태 감지
    LaunchedEffect(Unit) {

        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index}
            .filterNotNull()
            .collectLatest { lastIndex ->
                val total = items.size
                Log.d("InfiniteLazyList", "마지막 아이템 lastIndex = $lastIndex, total : $total")
                if (lastIndex >= total - 5) {
                    val nextItems = (total until total + 20).toList()
                    items = items + nextItems
                }
            }



//        snapshotFlow { listState.firstVisibleItemIndex }
//            .collect { index ->
//                Log.d("InfiniteLazyList", "현재 첫 아이템 index = $index")
//                val total = items.size
//                // 마지막 아이템 근처로 스크롤하면 새 데이터 추가
//                /**
//                 * index는 현재 보이는 리스트중에서 젤 위에이보이는 아이템의 인덱스.
//                 * 이건 정확하지 않을 수 있다.왜냐면 -5보면 한 화면에 보이는 아이템이 5보다 많으면 더보기가 진행 될 수 없기 때문이다.
//                  */
//
//                if (index >= total - 5) {
//                    val nextItems = (total until total + 20).toList()
//                    items = items + nextItems
//                }
//            }

    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Text(
                text = "Item #$item",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}