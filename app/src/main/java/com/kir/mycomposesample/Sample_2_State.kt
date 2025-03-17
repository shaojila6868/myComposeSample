package com.kir.mycomposesample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme

/**
 * [1] Stable로 취급되느 객체들
 *  - 모든 피리미티브 타입: Boolean, Int, Long, Float, Char, etc
 *  - Strings
 *  - All Function type(람다)
 *
 * [2] UnStable로 취급되는 객체들
 * List, MutableList
 *
 *  설명: GreetingPreview3()에서 ContactList()호출 부분은 고정되었지만
 *      selected는 remember로 값변경 시 Recomposeber를 한다.
 *      이때 정상이라면 ContactList()은 상태변경되는 값이 없으므로 recomposeber를 하지 않아야 하지만 Recomposable를 하고 있다.
 *      그 이유는 UnStable취급하는 names의 형식인 list가 있기때문이다.때문에 selected로 Recomposable 진행 될 때
 *      ContactList()는 GreetingPreview3()에 속한 @Composable이기 때문에 같이 Recomposable이 진행 된다.
 *
 *      만약 UnStable로 취급되는 객채들을 Stable처럼 보이게 하려면 @Stable 또는 @Immutable 어노테이션을 사용한다.
 *      ContactListState과 ContactList2(). ContactList2Preview4() 사용
 *
 *
 *  출처: https://android-devpia.tistory.com/15
 */
class Sample_2_State : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    GreetingPreview3()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    MyComposeSampleTheme {
        var selected by remember { mutableStateOf(false) }
        Column {
            Checkbox(
                checked = selected,
                onCheckedChange = {selected = it})

            ContactList(false, listOf("Jicheng"))
        }
    }
}

@Composable
fun ContactList(isLoading: Boolean, names: List<String>) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = names.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactList2Preview4() {
    MyComposeSampleTheme {
        var selected by remember { mutableStateOf(false) }
        Column {
            Checkbox(
                checked = selected,
                onCheckedChange = {selected = it})

            ContactList2(ContactListState(false, listOf("cuidan")))
        }
    }
}

@Composable
fun ContactList2(contactListState: ContactListState) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (contactListState.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = contactListState.names.toString())
        }
    }
}

@Composable
fun ContactList3(isLoading: Boolean, names: List<String>) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = names.toString())
        }
    }
}


@Stable
data class ContactListState(
    val isLoading: Boolean,
    val names: List<String>
)