package com.kir.mycomposesample.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme

@Preview
@Composable
fun sample_1_withTransform(){
    MyComposeSampleTheme {
        Column (modifier = Modifier.padding(top = 200.dp)){
            /**
             * 1: 빨간색을 0,0에서 그린다
             * 2: withTransform를 사용하여 그린다음 좌표가 다시 복귀된다.즉 0,0으로 복귀된다는 얘기다
             *      뜻은 withTransform로 하늘색 사각형을 45도로 돌려놓고 다시 0,0 복귀
             * 3. 그래서 마지막 green은 0,0부터 시작하기떄문에 다시 빨간색을 덮어씌운다
             */
            Canvas(modifier = Modifier.size(150.dp)) {
                drawRect(Color.Red, size = Size(100f, 100f)) // ① 빨간 사각형

                withTransform({
                    rotate(45f)
                }) {
                    drawRect(Color.Blue, size = Size(100f, 100f)) // ② 파란 사각형
                }

                drawRect(Color.Green, size = Size(100f, 100f)) // ③ 초록 사각형
            }
        }
    }
}

