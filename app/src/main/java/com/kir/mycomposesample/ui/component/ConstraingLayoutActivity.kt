package com.kir.mycomposesample.ui.component

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.kir.mycomposesample.ui.theme.MyComposeSampleTheme

/**
 * case 1: 기본 사용 설명 [ConstraintLayoutContent]
 *  createRefs() 참조 사용: button, text라는 이름으로 참조들을 만든다.
 *      이건 뭐냐면 아래 Button,Text의 Component 들에서 제야 조건을 주겠다.
 *
 * 50라인의 Button의 제약조건을 설명하겠다.
 *      53라인에서 Modifier.constrainAs사용하여 이 Button은 button 참조의 제약조건을 설정하곘다.
 *      그 제약내용은 55라인에 있다: 참조의 top(위는)은 parrent.top으로 한다.즉 parrent는 부모 ConstraintLayout를 말한다.
 *      즉 top.linkTo(parent.top, margin = 16.dp)는 button 참조의 위부분은 ConstraintLayout를 윗부분(parrent.top)으로 맞추고
 *      마진을 16.dp주겠다는 뜻이다
 *
 * 66라인의 Text의 제약조건을보겠다.
 *      69라인에서 역시 Text Component의 제약조건을 text로 하곘다.
 *      그리고 71라인은 text의 위부분은 button의 아래에 맞추겠다.그 마진은 16으로 주겠다.
 *
 * case 2: 분리된 상테에서 참조하여 보여지기 [DecoupledConstraintLayout]
 *  ConstraintSet로 ConstraintLayout전달하는 Set을 만든다.(114라인), 이
 *      ConstraintSet안에 117라인에 createRefFor등을 사용하여 참조를 만들고,
 *      121라인에 constrain을 사용하여 제약조건을 설정한다.
 *      즉 122기준으로보면 button잠조의 제약조건은 button참조의 top의 제약조건은 parrent.top:젝 ConstraintLayout(parrent)의 위제 맞춘다는 것.
 * 그리고 105라인에 보면 ConstraintLayout사용시 contraintSet를 constraints로 넣었기 때문에
 *  110라인에서 Modifier.layoutId("button")를 사용하여 constraints에서 정의한 참조를 가져올 수 있다.
 * 정리해보면 Button의 재약조건은(Modifier.layoutId("button")), Button의 top(127라인)은 ConstraintLayout의 top으로 맞춘것이다
 *
 * case 3: 가이드라인 [ConstraintLayoutGuidelines]
 *  간단한 편이어서 설명 생략
 *
 * case 4: 배리어 라인 [ConstraintBarrier]
 * 	•	Barrier = 보이지 않는 선(virtual guideline)
 * 	•	여러 컴포저블 중에서 특정 방향에서 가장 끝나는 위치를 기준으로 잡을 수 있어요.
 * 	•	예를 들어 top Barrier는 여러 컴포저블 중 가장 위쪽(top) 위치를 가진 컴포저블 기준을 만들어 줍니다.
 *
 *
 */
class ConstraingLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        ConstraintLayoutContent() // 안의 주석을 풀면 전체화면으로 노출하기떄문에 아래있는 DecoupledConstraintLayout Component가 보이지 않기때문에 주석처리하였다.
                        DecoupledConstraintLayout()
                        ConstraintLayoutGuidelines()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout /*(modifier = Modifier.fillMaxHeight())*/ {
        val (button, text) = createRefs()

        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        Text(
            "Text",
            Modifier.constrainAs(text) {
                top.linkTo(button.bottom, margin = 16.dp)
//                bottom.linkTo(parent.bottom, margin = 20.dp)
            }
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (minWidth < 600.dp) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

@Preview
@Composable
private fun ConstraintLayoutGuidelines() {
    // [START android_compose_constraintlayout_guidelines]
    ConstraintLayout {
        // Create guideline from the start of the parent at 10% the width of the Composable
        val startGuideline = createGuidelineFromStart(0.1f)
        // Create guideline from the end of the parent at 10% the width of the Composable
        val endGuideline = createGuidelineFromEnd(0.1f)
        //  Create guideline from 16 dp from the top of the parent
        val topGuideline = createGuidelineFromTop(16.dp)
        //  Create guideline from 16 dp from the bottom of the parent
        val bottomGuideline = createGuidelineFromBottom(16.dp)

        val (button, text) = createRefs()

        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button) {
                start.linkTo(startGuideline)  // 버튼의 왼쪽을 startGuideline에 붙임
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("ConstraintLayoutGuidelines Button")
        }

        Text(
            "ConstraintLayoutGuidelines Text",
            modifier = Modifier.constrainAs(text) {
                start.linkTo(endGuideline) // 텍스트도 같은 위치 기준
                top.linkTo(button.bottom, margin = 16.dp)
            }
        )
    }
    // [END android_compose_constraintlayout_guidelines]
}

@Preview
@Composable
private fun ConstraintBarrier() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button1")
        }

        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 40.dp)
            }
        ) {
            Text("Button2")
        }

        // button1, button2 중 가장 위쪽(top) 위치를 가진 컴포저블 기준으로 Barrier 생성
        val topBarrier = createTopBarrier(button1, button2)

        Text(
            "Text below top barrier",
            Modifier.constrainAs(text) {
                top.linkTo(topBarrier, margin = 16.dp) // topBarrier 아래에 배치
            }
        )


    }
}
