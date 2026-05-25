package com.example.tolongin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            OnboardingTopBar(
                onBackClick = onBackClick,
                onSkipClick = onSkipClick
            )

            Spacer(modifier = Modifier.height(44.dp))

            Image(
                painter = painterResource(id = R.drawable.tolonginlogo),
                contentDescription = "Illustration",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .requiredWidth(320.dp)
                    .requiredHeight(320.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Butuh bantuan? Tolong.in\naja.",
                color = Color(0xff1a1c1e),
                textAlign = TextAlign.Center,
                lineHeight = 1.29.em,
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Temukan bantuan terdekat dengan\ncepat, mudah, dan terpercaya.",
                color = Color(0xff414754),
                textAlign = TextAlign.Center,
                lineHeight = 1.56.em,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            PageIndicator()

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff005bb3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(9999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Mulai Sekarang",
                    fontSize = 14.sp,
                    letterSpacing = 0.14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Sudah punya akun? Masuk",
                    color = Color(0xff005bb3),
                    fontSize = 14.sp,
                    letterSpacing = 0.14.sp
                )
            }
        }
    }
}

@Composable
private fun OnboardingTopBar(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .requiredSize(40.dp)
                .clip(RoundedCornerShape(9999.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_kembali),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Color(0xff414754))
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSkipClick,
            shape = RoundedCornerShape(9999.dp)
        ) {
            Text(
                text = "Skip",
                color = Color(0xff005bb3),
                textAlign = TextAlign.Center,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.14.sp
                )
            )
        }
    }
}

@Composable
private fun PageIndicator() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .requiredWidth(32.dp)
                .requiredHeight(8.dp)
                .clip(RoundedCornerShape(9999.dp))
                .background(Color(0xff005bb3))
        )

        Box(
            modifier = Modifier
                .requiredSize(8.dp)
                .clip(RoundedCornerShape(9999.dp))
                .background(Color(0xffe2e2e5))
        )

        Box(
            modifier = Modifier
                .requiredSize(8.dp)
                .clip(RoundedCornerShape(9999.dp))
                .background(Color(0xffe2e2e5))
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomGradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(288.dp)
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xffa9c7ff).copy(alpha = 0.2f),
                        1f to Color(0xffa9c7ff)
                    ),
                    start = Offset(143.99f, 287.98f),
                    end = Offset(143.99f, 0f)
                )
            )
    )
}

@Preview(widthDp = 390, heightDp = 864, showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}