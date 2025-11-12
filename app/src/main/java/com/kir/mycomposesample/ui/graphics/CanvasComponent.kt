package com.kir.mycomposesample.ui.graphics


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kir.mycomposesample.R
import androidx.core.graphics.createBitmap


@Composable
fun ConnectedLines() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val path = Path().apply {
            moveTo(100f, 100f)   // 시작점
            lineTo(300f, 150f)   // 첫 번째 점
            lineTo(200f, 400f)   // 두 번째 점
            lineTo(500f, 500f)   // 세 번째 점
        }

        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 5f) // 선 두께
        )
    }
}

@Composable
fun DrawPolyline(points: List<Offset>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }
            drawPath(
                path = path,
                color = Color.Red,
                style = Stroke(width = 4f)
            )
        }
    }
}

@Preview
@Composable
fun preViewFun() {
    val points = listOf(
        Offset(50f, 50f),
        Offset(200f, 100f),
        Offset(300f, 250f),
        Offset(100f, 400f)
    )

    DrawPolyline(points)

}

@SuppressLint("SuspiciousIndentation")
@Preview
@Composable
fun drawBehindFun() {
    val painter = painterResource(id  = R.drawable.ic_launcher_foreground)
    val vector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
    val image = rememberVectorAsBitmap(R.drawable.ic_launcher_foreground)

    Text(
        text = "Hello Compose",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .drawBehind {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4.dp.toPx()
                )

                    drawImage(image, topLeft = Offset(5.dp.toPx(), 5.dp.toPx()))
            }
    )
}


@Composable
fun rememberVectorAsBitmap(@DrawableRes id: Int): ImageBitmap {
    val context = LocalContext.current
    val drawable = AppCompatResources.getDrawable(context, id)!!
    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

@Preview
@Composable
fun withTransformFun() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        withTransform({
            translate(left = 100f, top = 100f)
            rotate(degrees = 45f)
            scale(scaleX = 2f, scaleY = 2f)
        }) {
          drawRect(
              color = Color.Red,
              topLeft = Offset(0f, 0f),
              size = size / 3F
          )
        }
    }
}

@Preview
@Composable
fun drawArcFun() {
    Column {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                color = Color.Red,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,  // 중심 연결 안함 → 단순 호
                size = size
            )
        }

        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                color = Color.Blue,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = true,    // 중심과 연결 → 부채꼴
                size = size
            )
        }

    }

}
