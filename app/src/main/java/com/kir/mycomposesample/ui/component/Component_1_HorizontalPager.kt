package com.kir.mycomposesample.ui.component

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme

/**
 * 1. HorizontalPager
 *      [-]좌우 스크롤 할 수 있음
 *      [-]Modifier.weight(1f): Column 은 Linearlayout 세로로 되어있는상태에서 weigh를 1로 하면 밑 부분이 default로 0으로 되기때문에 밑에 부분이 먼저 보인다.
 *      [-]rememberPagerState 은 페이징의 할 때 사용한다. HorizontalPager의 페이징이 변경되면 pagerState 의 state도 변경된다
 */
class Component_1_HorizontalPager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Component_HorizontalPager(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Component_HorizontalPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HorizontalPager(state = pagerState,
            modifier = Modifier.weight(1f)) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (page == 0) Color.Red else if (page == 1) Color.Blue else Color.Green),
                contentAlignment = Alignment.Center
            ) {
                Text("Page $page", color = Color.White, fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                        .background(
                            if (pagerState.currentPage == index) Color.Red else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    MyComposeSampleTheme {
        Component_HorizontalPager()
    }
}