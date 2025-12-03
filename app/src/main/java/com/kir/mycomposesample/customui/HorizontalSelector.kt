package com.kir.mycomposesample.customui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.kir.mycomposesample.customui.basic.CommonText
import com.kir.mycomposesample.customui.basic.noRippleClickable

/**
 * 좌우로 custom 선택하는 Radio버튼이라고 보면 된다.
 *
 * 분석해 보면 Box를 클릭 할 때 onSelect()가 호출 되고
 * component는 이 UI의 그리는 부분을 내보낸다.그래서 호출하는 부분에서 이 아이템을 어떻게 그릴지 정하라는거다.
 *              대신 파라미터에 선택되었는지 아닌지를 compare()를 통해 판단해준다
 */
@Composable
fun <T> HorizontalSelector(
    list : List<T>,
    modifier: Modifier = Modifier,
    itemModifier : Modifier = Modifier,
    compare : (T,T) -> Boolean = { a,b ->
         a.toString().contains(b.toString())
    },
    enabled : Boolean = true,
    horizontalArrangement : Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    verticalAlignment : Alignment.Vertical = Alignment.CenterVertically,
    selectValue : MutableState<T>,
    callback : ((T) -> Unit)? = null,
    enabledCallback : ((T) -> Unit)? = null,
    component : @Composable (T, Boolean) -> Unit,
) {
    fun onSelect(value : T) {
        selectValue.value = value
    }
    Row (modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment){
        list.forEach{
            Box(modifier = itemModifier.noRippleClickable {
                if(enabled) {
                    onSelect(it)
                    callback?.invoke(it)
                } else {
                    enabledCallback?.invoke(it)
                }
            }){
                component(it, compare(it, selectValue.value))
            }
        }
    }
}

@Composable
fun HorizontalMenuCard(
    cardModifier: Modifier = Modifier,
    contentModifier : Modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
    isSelect : Boolean = false,
    enabled : Boolean = true,
    shapeSize : Dp = 4.dp,
    selectedTextColor : Color = Color.Unspecified,
    selectedBackColor : Color = Color.Unspecified,
    unSelectedTextColor : Color = Color.Unspecified,
    unSelectedBackColor : Color = Color.Unspecified,
    component : @Composable (color : Color) -> Unit,
) {
    val dSelectedTextColor = if(selectedTextColor == Color.Unspecified) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        selectedTextColor
    }

    val dSelectedBackColor = if(selectedBackColor == Color.Unspecified) {
        MaterialTheme.colorScheme.inverseSurface
    } else {
        selectedBackColor
    }

    val dUnSelectedTextColor = if(unSelectedTextColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        unSelectedTextColor
    }

    val dUnSelectedBackColor = if(unSelectedBackColor == Color.Unspecified) {
        MaterialTheme.colorScheme.surfaceContainerLow
    } else {
        unSelectedBackColor
    }

    val containerColor = if(isSelect) dSelectedBackColor else dUnSelectedBackColor
    val contentColor = if(isSelect) dSelectedTextColor else dUnSelectedTextColor
    Card (shape = RoundedCornerShape(shapeSize),
        colors = CardDefaults.cardColors(
            containerColor = containerColor.copy(alpha = if(enabled) 1.0f else 0.5f),
            contentColor = contentColor.copy(alpha = if(enabled) 1.0f else 0.5f),
        ),
        modifier = cardModifier,
    ){
        Box(modifier = contentModifier,
            contentAlignment = Alignment.Center) {
            component(contentColor.copy(alpha = if(enabled) 1.0f else 0.5f))
        }
    }
}

/**
 * HorizontalSelector의 사용방식.
 */
@Preview
@Composable
fun sample_1_HorizontalSelect() {
    val itemList = listOf("선택1","선택2","선택3")
    val selectValue = remember { mutableStateOf("선택1") }

    Row(modifier = Modifier.fillMaxWidth().padding(top = 150.dp)) {
        HorizontalSelector(
            list = itemList,
            selectValue = selectValue,
            component = {it, selected ->
                HorizontalMenuCard(
                    isSelect = selected,
                    shapeSize = 8.dp,
                    cardModifier = Modifier.fillMaxWidth().weight(1f),
                    contentModifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    component = { color ->
                        CommonText(
                            text = it,
                            fontSizeDp = 14.dp
                        )
                    }
                )
            }
        )
    }

}

@Preview
@Composable
fun ss() {
    var checked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(30.dp)) {
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            thumbContent = {
                if (checked) Icon(Icons.Default.Check, contentDescription = null)
            }
        )
    }

}