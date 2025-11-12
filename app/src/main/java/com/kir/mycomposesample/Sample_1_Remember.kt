package com.kir.mycomposesample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 *  remember {mutableStateOf()} 사용
 *
 *  변경가능한
 *
 */
class Sample_1_Remember : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    var inPutname by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    val countState = mutableStateOf(0)
    Column {
        Text(
            text = "Hello $inPutname!",
            modifier = modifier
        )

        OutlinedTextField(
            value = inPutname,
            onValueChange = {inPutname = it}
        )

        Button(onClick = {
            isVisible = !isVisible
        }) {
            Text("보이기")
        }

        if (isVisible) {
            Text("나 보였다")
        }

        Text(
            text = "countState ${countState.value}",
            modifier = modifier
        )

        Button(onClick = { countState.value += 1 }) {
            Text("카운트 추가")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MyComposeSampleTheme {
        Greeting2()
    }
}