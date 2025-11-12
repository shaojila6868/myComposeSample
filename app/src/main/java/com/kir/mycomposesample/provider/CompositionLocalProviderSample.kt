package com.kir.mycomposesample.provider

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

/**
 * [1] CompositionLocalProvider
 * 한마디로 전역 Provider 이다.즉 한곳에서 정의해 놓고 모든 곳에서 사용할 수 있음
 *
 *  case 1: [CompositionLocalExample]
 *      LocalContentColor로 들어가보면 전역으로 정의 해 놓은 거도. 29라인 보면 LocalContentColor의 provider를 MaterialTheme.colorScheme.primary로 변경하겠다는거다.
 *              그러면 31라인의 Text에서 컬러를 LocalContentColor 의 전역으로 사용한다면 MaterialTheme.colorScheme.primary로 변겨되었기때문에 해당 컬러로 보여지고
 *              34라인에서는 MaterialTheme.colorScheme.error로 바꾸었기때문에 35라인으로 들어가서 45라인의 텍스트가 MaterialTheme.colorScheme.error에서 지정된 컬러로 보여준다
 *  case 2: [AppContent]
 *      50라인에는 LocalUserName의 CompositionLocalProvider를 정의 해 놓고 <String> 형식이다.
 *      54라인에서는 LocalUserName의 Provider를 DarkKir로 설정했다
 *      그러면 62라인에서 LocalUserName.current 가져 올 때 DarkKir로 가져 올 수 있다.
 */
class CompositionLocalProviderSample {

    @Composable
    fun CompositionLocalExample() {
        MaterialTheme {

            Surface {
                Column {
                    Text("Uses Surface's provided content color")
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                        Text("Primary color provided by LocalContentColor")
                        Text("This Text also uses primary as textColor")
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                            DescendantExample()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DescendantExample() {
        Text("This Text uses the error color now")
    }

    val LocalUserName = compositionLocalOf<String> { error("No default provided") }
    @Composable
    fun AppContent() {
        CompositionLocalProvider(LocalUserName provides "DarkKir") {
            UserProfile()
        }
    }
    @Composable
    fun UserProfile() {
        val name = LocalUserName.current
        Text("Hello, $name!")
    }
}