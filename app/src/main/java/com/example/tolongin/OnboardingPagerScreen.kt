package com.example.tolongin

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class OnboardingItem(
    val imageRes: Int,
    val title: String,
    val description: String,
    val titleColor: Color,
    val descriptionColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPagerScreen(navController: NavController) {
    val pages = listOf(
        OnboardingItem(
            imageRes = R.drawable.tolonginlogo,
            title = "Butuh bantuan? Tolong.in\naja.",
            description = "Temukan bantuan terdekat dengan\ncepat, mudah, dan terpercaya.",
            titleColor = Color(0xff1a1c1e),
            descriptionColor = Color(0xff414754)
        ),
        OnboardingItem(
            imageRes = R.drawable.servicecategoriesillustration,
            title = "Pilih bantuan yang kamu\nbutuhkan",
            description = "Mulai dari bantuan harian, jasa ringan,\nhingga kebutuhan mendesak di sekitarmu.",
            titleColor = Color(0xff005bb3),
            descriptionColor = Color(0xff414754)
        ),
        OnboardingItem(
            imageRes = R.drawable.obs3,
            title = "Terhubung dengan orang\nyang siap membantu",
            description = "Chat, atur lokasi, dan selesaikan bantuan\ndengan aman dalam satu aplikasi.",
            titleColor = Color(0xff1a1c1e),
            descriptionColor = Color(0xff006b5c)
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    fun goToLogin() {
        navController.navigate("login") {
            popUpTo("onboarding1") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        OnboardingHeader(
            onSkipClick = { goToLogin() }
        )

        Spacer(modifier = Modifier.height(48.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(item = pages[page])
        }

        PageIndicator(
            currentPage = pagerState.currentPage,
            pageCount = pages.size
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { goToLogin() },
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
            onClick = { goToLogin() },
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

@Composable
private fun OnboardingHeader(
    onSkipClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.requiredSize(40.dp))

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "tolong.in",
            color = Color(0xff005bb3),
            lineHeight = 1.33.em,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSkipClick,
            shape = RoundedCornerShape(9999.dp)
        ) {
            Text(
                text = "Skip",
                color = Color(0xff005bb3),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.14.sp
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    item: OnboardingItem
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .requiredWidth(320.dp)
                .requiredHeight(300.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = item.title,
            color = item.titleColor,
            textAlign = TextAlign.Center,
            lineHeight = 1.29.em,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = item.description,
            color = item.descriptionColor,
            textAlign = TextAlign.Center,
            lineHeight = 1.56.em,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PageIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .then(
                        if (index == currentPage) {
                            Modifier
                                .requiredWidth(32.dp)
                                .requiredHeight(8.dp)
                        } else {
                            Modifier.requiredSize(8.dp)
                        }
                    )
                    .clip(RoundedCornerShape(9999.dp))
                    .background(
                        if (index == currentPage) {
                            Color(0xff005bb3)
                        } else {
                            Color(0xffe2e2e5)
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}