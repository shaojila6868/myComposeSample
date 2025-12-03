package com.kir.mycomposesample.customui.basic

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun CommonText(
    text: String,
    modifier: Modifier = Modifier,
    ignoreColor : Boolean = false,
    color: Color = Color.Unspecified,
    fontSizeSp: TextUnit? = null,
    fontSizeDp: Dp? = null,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    lineHeightDp : Dp? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    maxLength : Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,

    ) {
    val fontSize = fontSizeDp?.dpToSp() ?: (fontSizeSp ?: TextUnit.Unspecified)

    val dLineHeight = if(fontSize == TextUnit.Unspecified) {
        TextUnit.Unspecified
    } else lineHeightDp?.dpToSp()
        ?: if(lineHeight == TextUnit.Unspecified) {
            fontSize * 1.2
        } else {
            lineHeight
        }

    val dColor = if(color != Color.Unspecified) color

    else {
            MaterialTheme.colorScheme.onSurface
    }

    var dText = text
    if(dText.length > maxLength && overflow != TextOverflow.Ellipsis) {
        dText = dText.substring(0, maxLength )
        dText += "..."
    }

    BasicText(
        text = dText,
        style = TextStyle(
            fontSize = fontSize,
            color = if(ignoreColor) Color.Unspecified else dColor,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            lineHeight = dLineHeight,
            textAlign = textAlign ?: TextAlign.Unspecified,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing,
        ),
        modifier = modifier,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
    )
}