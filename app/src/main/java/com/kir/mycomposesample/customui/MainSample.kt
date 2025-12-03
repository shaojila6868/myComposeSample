package com.kir.mycomposesample.customui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kir.mycomposesample.customui.basic.CommonSlider
import com.kir.mycomposesample.customui.basic.CommonSwitch




@Preview
@Composable
fun sample_1_switch() {
    val check = remember { mutableStateOf(true) }

    Column (modifier = Modifier.padding(60.dp)){
        CommonSwitch(check)
    }
}

@Preview
@Composable
fun sample_2_slide() {
    val valueRe = remember { mutableStateOf(1f) }

    Column (modifier = Modifier.padding(60.dp)){
        CommonSlider(
            value = valueRe,
            range = 0f..5f
        )
    }
}