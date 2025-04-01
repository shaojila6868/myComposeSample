package com.kir.mycomposesample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [1] LaunchedEffect: [UseLaunchedEffect]
 *      withContext(Dispatchers.IO)를 지원 한다.즉 IO thread 로 값을 가져 왔을 떄 Recomposable 할 수 있다.
 *     - getAsyncValue() 보면 앞에 suspend가 있다.즉 suspend를 지원한다.
 *     - LaunchedEffect()의 인자는 key로 Unite이면 변경하지 않는 값으로 한번만 진행되고 key값이 변경가능한거면
 *          key(State)가 변경될 때마다 LaunchedEffect가 다시 실행되어 Async로 또 다시 실행 할 수 있다
 *
 *  https://developer88.tistory.com/entry/LaunchedEffect-%EC%B4%9D%EC%A0%95%EB%A6%AC
 *
 * [2] rememberUpdateState: [UseLaunchedEffect2]
 *      - remember는 초기 컴포지션에서만 값을 저장하고 리컴포지션 때 들어온 값은 저장하지 않는다. 리컴포지션 때 들어온 값도 저장하고 싶을 때 rememberUpdateState를 사용한다.
 *      - 즉 리컴포지션 될 떄 State 값이 변경됨을 인식하려면 rememberUpdateState사용하여야 한다.remember는 초기한번만 저장하기 때문에 참조 State가 변경되더라도 인식하지 못해
 *          리컴포지션 되더라도 값이 변경되지 않는다.
 *      - 아래 UseLaunchedEffect2()와 UseLaunchedEffect2Sub()의 사용 상황: UseLaunchedEffect2()에서 버튼 클릭 할 때 count(State)가 변경되어 리컴포지션 발생.
 *          UseLaunchedEffect2Sub()에서도 리컴포지션이 될 때 value 값이 변경됨을 아려면 value 변경되었다는걸 인지해야 한다.
 *          이 떄 rememberUpdatedState()은 State의 변경값이 항상 반영되기 때문에 인지가능.
 *
 *  https://jinukeu.hashnode.dev/android-rememberupdatedstate
 *
 * [3] rememberCoroutineScope: [UseRememberCoroutineScope]
 *      Composable 밖에서 실행될 떄.
 *      - Composable 밖에서 State에 영향을 주기 위해서는 rememberCoroutineScope 사용
 *      - 즉 클릭이벤트가 발생 했을 때 서버 데이터를 가져왈을 때 state에 영향을 주기위해서
 *
 * [4] DisposableEffect: [UseDisposableEffect]
 *  Activity의 Lifecycle에서 onDestroy가 될 때 observer나 리소스를 정리했던 것처럼 Compose에서는 Composable이 화면에서 사라지면서 observer나 리소스를 정리해야 하는 경우 DisposableEffect를 활용할 수 있습니다.
 *      - Activity의 Lifecycle 의 사용에 관한 더좋은 방법 LifecycleEventEffect
 *
 *  https://wannabe-master.tistory.com/5
 *
 * [5] SideEffect: [UseSideEffect]
 *  - Composition이 완료되면 실행되는 부수효과
 *  - Recomposition이 발생될 때마다 실행
 *  - Composable 내부의 상태를 외부 시스템과 동기화하거나 Composition이 완료된 후에 실행되어야 하는 작업을 정의
 *
 *  https://velog.io/@ham2174/Android-Compose-SideEffect%EC%9D%98-%EC%A2%85%EB%A5%98%EC%99%80-%EC%97%AD%ED%95%A0
 *
 * [6] DerivedStateOf: [UseDerivedStateOf]
 *  - 단기간에 한번에 많은 State가 변경될 떄 바로바로 적용되면 성능에 좋지 않다.
 *    그때 DerivedStateOf 사용한다.예를 들면 10번에 적용될 걸 마지막 1번에 적용된다.
 *    예로 List Scroll 할 떄 한번 스크롤 하면 엄청 많은 State가 발생할 수 있지만 DerivedStateOf를 사용하면 적게 State가 발생
 *
 *  https://thinking-face.tistory.com/387
 *
 */
class Sample_3_SideEffect : ComponentActivity() {

    val TAG = this.javaClass.name

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    UseLaunchedEffect()
                }
            }
        }
    }

    @Composable
    fun UseLaunchedEffect() {
        var name by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            Log.i(TAG, "UseLaunchedEffect() Thread : ${Thread.currentThread().id}")
            name = getAsyncValue()
        }

        Text(
            text = "Launched Effect Name : $name",
            modifier = Modifier
        )

    }

    private suspend fun getAsyncValue(): String = withContext(Dispatchers.IO) {
        Log.i(TAG, "getAsyncValue() Thread : ${Thread.currentThread().id}")
        delay(2000)
        return@withContext "cuidan"
    }

    @Composable
    fun UseLaunchedEffect2() {
        var tootle by remember { mutableStateOf(true) }
        var count by remember { mutableIntStateOf(0) }

        Column {
            Text("Count: $count")
            Button(onClick = {
                tootle  = !tootle
                count ++
            }) { Text("키 전환 count 증가시키기") }
            UseLaunchedEffect2Sub(count)
        }

        LaunchedEffect(tootle) {
            Log.i(TAG, "UseLaunchedEffect2() 현재 count: $count")
        }
    }

    @Composable
    fun UseLaunchedEffect2Sub(value: Int) {

        val updateValue by rememberUpdatedState(value) // 부모 composable의 Recomposable이 될 때 여기 본 Composable에서도 Recomposable될 때 변경을 감지 할 수 있게 하려면 rememberUpdatedState사용
        /** remember를 사용하면 초기화떄의 값만 저장하기때문에 Recomposable 될 때 감지할 수 없음. 즉 value의 초기 설정 UseLaunchedEffect2()의 var count by remember { mutableIntStateOf(0) }만 저장됨
         *  count가 변경 되더라고 updateValue는 감지 할 수 없음
        **/
//        val updateValue by remember { mutableIntStateOf(value) }

        Text("Sub Count: $updateValue")
    }

    @Composable
    fun UseRememberCoroutineScope() {
        val coroutineScope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        Column {
            Button(onClick = {
                coroutineScope.launch {
                    text = getAsyncValue()
                }
            }) {
                Text("이름 가져오기")
            }

            Text(text)
        }

    }

    @Composable
    fun UseDisposableEffect() {
        var value by remember { mutableIntStateOf(0) }

        Column {
            Button(onClick = {
                value += 1
            }) {
                Text("DisposableEffect")
            }

            Text("index : $value")
        }

        DisposableEffect(value) {
            onDispose {
                Log.i(TAG, "UseDisposableEffect.onDispose value: $value")
            }
        }
    }

    @Composable
    fun UseSideEffect() {
        var text by remember { mutableStateOf("") }

        SideEffect {
            Log.i(TAG, "UseSideEffect : $text")
        }
    }

    @Composable
    fun UseDerivedStateOf() {
        var userName by remember { mutableStateOf("") }
        val submitEnable = remember { derivedStateOf { userName } }

        TextField(value = userName
            , onValueChange = {userName = it}
        )

        Text("derivedStateOf :${submitEnable.value}")
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview4() {
        MyComposeSampleTheme {
            Column {
                UseLaunchedEffect()
                UseLaunchedEffect2()
                UseRememberCoroutineScope()
                UseDisposableEffect()
                UseSideEffect()
                UseDerivedStateOf()
            }
        }
    }

}


