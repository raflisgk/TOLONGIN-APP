package com.example.tolongin

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val PrimaryBlue      = Color(0xFF005AB2)
val DarkBlue         = Color(0xFF004488)
val NavyDark         = Color(0xFF111827)
val NavyMid          = Color(0xFF1F2937)
val BlueMid          = Color(0xFF1E40AF)
val BlueLight        = Color(0xFF3B82F6)
val TextNavy         = Color(0xFF1E2D51)
val TextMuted        = Color(0xFF4E5A81)
val TextGray         = Color(0xFF6B7280)
val BgPage           = Color(0xFFF8FAFC)
val BgChip           = Color(0xFFEFF6FF)
val BgCard           = Color(0xFFFFFFFF)
val BorderLight      = Color(0xFFF1F5F9)

data class ServiceItem(val icon: ImageVector, val label: String)
data class HelperItem(val name: String, val spec: String, val rating: String, val emoji: String)
data class PromoItemData(val tag: String, val title: String, val desc: String)

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
    val alamatGpsAsli = sharedPreferences.getString("ALAMAT_GPS_ASLI", "SUDIRMAN, JAKARTA SELATAN") ?: "SUDIRMAN, JAKARTA SELATAN"

    var userName by remember { mutableStateOf("raflijosjis") }

    LaunchedEffect(Unit) {
        val emailLogin = "raflisgk@gmail.com"
        RetrofitClient.instance.getProfil(emailLogin).enqueue(object : Callback<ProfilResponse> {
            override fun onResponse(call: Call<ProfilResponse>, response: Response<ProfilResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val fetchedName = response.body()?.data?.nama
                    if (!fetchedName.isNullOrEmpty()) {
                        userName = fetchedName.split(" ")[0]
                    }
                }
            }
            override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {}
        })
    }

    val services = listOf(
        ServiceItem(Icons.Outlined.CleaningServices, "Pembersihan"),
        ServiceItem(Icons.Outlined.ShoppingCart,     "Titip Beli"),
        ServiceItem(Icons.Outlined.LocalShipping,    "Antar Barang")
    )

    val helpers = listOf(
        HelperItem("Siti Aminah",  "Spesialis Pembersihan", "4.9", "🧕"),
        HelperItem("Budi Santoso", "Kurir Barang Ringan",   "4.8", "👨"),
        HelperItem("Dewi Lestari", "Titip Beli Makanan",    "5.0", "👩")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 82.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(alamat = alamatGpsAsli)

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Halo, $userName!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextNavy,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Ada yang bisa kami bantu hari ini?",
                    fontSize = 14.sp,
                    color = TextMuted
                )
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Layanan Kami",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavy,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    services.forEach { svc ->
                        ModernHorizontalServiceButton(
                            svc = svc,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                when (svc.label) {
                                    "Pembersihan" -> navController.navigate("detail_pembersihan")
                                    "Titip Beli"   -> navController.navigate("pemesanan_titip_beli")
                                    "Antar Barang" -> navController.navigate("detail_pengiriman")
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))
            }

            PromoCarousel()

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Helper Terpopuler",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavy,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "LIHAT SEMUA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(Modifier.height(14.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                helpers.forEach { helper ->
                    HelperCard(helper, onPesanClick = { navController.navigate("pemesanan_titip_beli") })
                }
            }
            Spacer(Modifier.height(32.dp))
        }

        FloatingActionButton(
            onClick = { },
            containerColor = TextNavy,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 106.dp)
                .size(54.dp)
        ) {
            Icon(Icons.Outlined.HelpOutline, contentDescription = "Bantuan", modifier = Modifier.size(24.dp))
        }

        BottomNavigationBarHome(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TopBar(alamat: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, BorderLight)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tolong.in",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryBlue,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, null, tint = TextGray, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = alamat.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ModernHorizontalServiceButton(
    svc: ServiceItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BgChip),
            contentAlignment = Alignment.Center
        ) {
            Icon(svc.icon, svc.label, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = svc.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavy,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PromoCarousel() {
    val promoList = listOf(
        PromoItemData("LIMITED OFFER", "Diskon 50%\nUntuk Kamu!", "Khusus pengguna baru untuk layanan Pembersihan Rumah."),
        PromoItemData("CASHBACK", "Cashback 30%\nBebas Kuota!", "Berlaku untuk semua layanan dengan pembayaran QRIS."),
        PromoItemData("GRATIS", "Gratis Biaya\nTransport", "Pesan layanan hari ini dan helper kami datang gratis.")
    )
    val listState = rememberLazyListState()
    val currentIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            if (!listState.isScrollInProgress && promoList.isNotEmpty()) {
                val nextIndex = (listState.firstVisibleItemIndex + 1) % promoList.size
                listState.animateScrollToItem(nextIndex)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(promoList.size) { index ->
                val promo = promoList[index]
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(146.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brush.linearGradient(colors = listOf(NavyDark, NavyMid, BlueMid)))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
                        Column {
                            Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                                Text(text = promo.tag, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = promo.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 24.sp)
                        }
                        Text(text = promo.desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        // ── FIXED: Sekarang sudah menggunakan Arrangement.Center murni ──
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(promoList.size) { index ->
                val isSelected = currentIndex == index
                Box(modifier = Modifier.padding(horizontal = 3.dp).width(if (isSelected) 16.dp else 6.dp).height(6.dp).clip(RoundedCornerShape(50)).background(if (isSelected) PrimaryBlue else Color(0xFFCBD5E1)))
            }
        }
    }
}

@Composable
fun HelperCard(helper: HelperItem, onPesanClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = BgCard,
        modifier = Modifier
            .width(145.dp)
            .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(14.dp)
        ) {
            Box {
                Box(modifier = Modifier.size(76.dp).clip(RoundedCornerShape(20.dp)).background(BgChip), contentAlignment = Alignment.Center) {
                    Text(text = helper.emoji, fontSize = 36.sp)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(11.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(text = helper.rating, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextNavy)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(text = helper.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextNavy, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = helper.spec, fontSize = 10.sp, color = TextGray, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onPesanClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(32.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Pesan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun BottomNavigationBarHome(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Filled.Home to "HOME",
        Icons.Outlined.ListAlt to "ORDERS",
        Icons.Outlined.ChatBubbleOutline to "MESSAGES",
        Icons.Outlined.Person to "PROFILE"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 24.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { (icon, label) ->
                val active = label == "HOME"
                val bgColor = if (active) Color(0xFFF0F5FF) else Color.Transparent
                val contentColor = if (active) PrimaryBlue else Color(0xFF94A3B8)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .clickable {
                            if (active) return@clickable
                            when (label) {
                                "HOME"     -> navController.navigate("beranda") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "ORDERS"   -> navController.navigate("daftar_pesanan") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "MESSAGES" -> navController.navigate("pesan") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "PROFILE"  -> navController.navigate("profil") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                            }
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(26.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}