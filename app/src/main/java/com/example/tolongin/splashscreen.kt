package com.example.tolongin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

class Splashscreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HtmlBody()
                }
            }
        }
    }
}

@Composable
fun HtmlBody(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackgroundDecoration()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            LogoBox()

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "tolong",
                    color = Color(0xff0084ff),
                    textAlign = TextAlign.Center,
                    lineHeight = 1.17.em,
                    style = TextStyle(
                        fontSize = 48.sp,
                        letterSpacing = (-1.2).sp
                    ),
                    modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
                )

                Text(
                    text = ".in",
                    color = Color(0xff006b5c),
                    textAlign = TextAlign.Center,
                    lineHeight = 1.17.em,
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1.2).sp
                    ),
                    modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

            Text(
                text = "Bersama, membantu lebih\nmudah.",
                color = Color(0xff414754),
                textAlign = TextAlign.Center,
                lineHeight = 1.56.em,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .padding(
                        start = 32.69.dp,
                        top = 16.dp,
                        end = 32.69.dp
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun LogoBox() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(160.dp)
            .shadow(
                elevation = 40.dp,
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tolonginlogo),
            contentDescription = "tolong.in logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BackgroundDecoration() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-39).dp, y = (-88.39).dp)
                .requiredWidth(234.dp)
                .requiredHeight(354.dp)
                .clip(RoundedCornerShape(9999.dp))
                .blur(radius = 64.dp)
                .background(Color(0xffd6e3ff).copy(alpha = 0.3f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 39.dp, y = 88.39.dp)
                .requiredWidth(273.dp)
                .requiredHeight(442.dp)
                .clip(RoundedCornerShape(9999.dp))
                .blur(radius = 64.dp)
                .background(Color(0xff68fadd).copy(alpha = 0.2f))
        )

        Image(
            painter = painterResource(id = R.drawable.abstractwavedecorationbottom),
            contentDescription = "Abstract Wave Decoration Bottom",
            colorFilter = ColorFilter.tint(Color(0xffd6e3ff).copy(alpha = 0.2f)),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .requiredWidth(390.dp)
                .requiredHeight(87.dp)
        )
    }
}

@Preview(widthDp = 390, heightDp = 884, showBackground = true)
@Composable
private fun HtmlBodyPreview() {
    HtmlBody()
}