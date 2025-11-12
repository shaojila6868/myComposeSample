package com.kir.mycomposesample.ui.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ModifierComponent {
}

@Preview
@Composable
fun PreViewComposable() {
    TwoTexts(Modifier, "text1", "text2")

}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        VerticalDivider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }

}

@Preview
@Composable
fun IntrinsicSizeMin() {
    Row (modifier = Modifier.height(IntrinsicSize.Min).background(Color.Green)){
        Box(Modifier.size(40.dp).background(Color.Red))
        VerticalDivider(color = Color.Yellow,
            modifier = Modifier.fillMaxHeight().width(1.dp))
        Box(Modifier.size(60.dp).background(Color.Blue))
    }
}

@Preview
@Composable
fun Sample_3_GraphicsLayer_Translation(){
    Row (modifier = Modifier.size(400.dp)) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    translationX = 150f
                    translationY = 100f
                }
                .background(Color.Red)
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    translationX = 200f
                    translationY = 100f
                }
                .background(Color.Blue)
        )
    }


}