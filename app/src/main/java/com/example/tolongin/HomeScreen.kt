package com.example.tolongin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ─── Warna ───────────────────────────────────────────────────────────────────
val PrimaryBlue      = Color(0xFF2563EB)
val DarkBlue         = Color(0xFF1D4ED8)
val NavyDark         = Color(0xFF0D1B3E)
val NavyMid          = Color(0xFF0F2A5E)
val BlueMid          = Color(0xFF1A4A8A)
val BlueLight        = Color(0xFF1E6FB5)
val TextNavy         = Color(0xFF1E2D5A)
val TextMuted        = Color(0xFF4E5A81)
val TextGray         = Color(0xFF64748B)
val BgPage           = Color(0xFFF0F4FF)
val BgChip           = Color(0xFFEEF0FF)
val BgCard           = Color(0xFFFFFFFF)
val BorderLight      = Color(0xFFE8EAF6)
val PromoTextSub     = Color(0xFFA8C8E8)

// ─── Data Class ───────────────────────────────────────────────────────────────
data class ServiceItem(val icon: ImageVector, val label: String)
data class HelperItem(val name: String, val spec: String, val rating: String, val emoji: String)
data class PromoItemData(val tag: String, val title: String, val desc: String)

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    // --- STATE UNTUK NAMA PENGGUNA ---
    var userName by remember { mutableStateOf("raflijosjis") } // Default sesuai gambar Mas

    // --- LOGIKA MENARIK NAMA DARI DATABASE ---
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
                .padding(bottom = 80.dp) // Disesuaikan dengan tinggi Navbar baru
                .verticalScroll(rememberScrollState())
        ) {
            // ── TopBar ────────────────────────────────────────────────────────
            TopBar()

            // ── Body ──────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Halo, $userName!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavy
                )
                Text(
                    text = "Ada yang bisa kami bantu hari ini?",
                    fontSize = 13.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Layanan Kami",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavy
                )
                Spacer(Modifier.height(12.dp))

                // --- TOMBOL LAYANAN (DIPERBAIKI: Sambungan Titip Beli) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    services.forEach { svc ->
                        ServiceButton(svc, onClick = {
                            when (svc.label) {
                                "Pembersihan" -> navController.navigate("detail_pembersihan")
                                "Titip Beli"   -> navController.navigate("pemesanan_titip_beli")
                                "Antar Barang" -> navController.navigate("detail_pengiriman")
                            }
                        })
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // --- PROMO CAROUSEL ---   
            PromoCarousel()

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Helper Terpopuler",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavy
                    )
                    Text(
                        text = "LIHAT SEMUA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(Modifier.height(14.dp))
            }

            // Helper horizontal scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                helpers.forEach { helper ->
                    HelperCard(helper, onPesanClick = { navController.navigate("pemesanan_titip_beli") })
                }
            }

            Spacer(Modifier.height(32.dp))
        }

        // ── FAB ───────────────────────────────────────────────────────────────
        FloatingActionButton(
            onClick = { },
            containerColor = PrimaryBlue,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 100.dp)
                .size(52.dp)
        ) {
            Icon(Icons.Outlined.Help, contentDescription = "Bantuan", modifier = Modifier.size(28.dp))
        }

        // ── Bottom Navigation ─────────────────────────────────────────────────
        BottomNavigationBarHome(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ─── TopBar ──────────────────────────────────────────────────────────────────
@Composable
fun TopBar() {
    Surface(color = Color.White, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Tolong.in", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, null, tint = PrimaryBlue, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "SUDIRMAN, JAKARTA SELATAN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

// ─── Service Button ───────────────────────────────────────────────────────────
@Composable
fun ServiceButton(svc: ServiceItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(16.dp)).background(BgChip),
            contentAlignment = Alignment.Center
        ) {
            Icon(svc.icon, svc.label, tint = DarkBlue, modifier = Modifier.size(26.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(svc.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
    }
}

// ─── Promo Carousel ───────────────────────────────────────────────────────────
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(promoList.size) { index ->
                val promo = promoList[index]
                Box(
                    modifier = Modifier.fillParentMaxWidth().height(150.dp).clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(colors = listOf(NavyDark, NavyMid, BlueMid, BlueLight))).padding(22.dp)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
                        Column {
                            Box(modifier = Modifier.clip(RoundedCornerShape(50)).background(BlueLight).padding(horizontal = 10.dp, vertical = 3.dp)) {
                                Text(text = promo.tag, fontSize = 9.sp, color = Color(0xFFE0F0FF), letterSpacing = 0.3.sp)
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(text = promo.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 24.sp)
                        }
                        Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(10.dp), modifier = Modifier.height(32.dp)) {
                            Text(text = "Klaim Promo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextNavy)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(promoList.size) { index ->
                val isSelected = currentIndex == index
                Box(modifier = Modifier.padding(horizontal = 3.dp).width(if (isSelected) 20.dp else 6.dp).height(6.dp).clip(RoundedCornerShape(50)).background(if (isSelected) PrimaryBlue else Color(0xFFB0B8D8)))
            }
        }
    }
}

// ─── Helper Card ─────────────────────────────────────────────────────────────
@Composable
fun HelperCard(helper: HelperItem, onPesanClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(20.dp), color = BgCard, shadowElevation = 3.dp, modifier = Modifier.width(140.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(14.dp)) {
            Box {
                Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(14.dp)).background(BgChip), contentAlignment = Alignment.Center) {
                    Text(text = helper.emoji, fontSize = 34.sp)
                }
                Surface(shape = RoundedCornerShape(7.dp), color = Color.White, shadowElevation = 3.dp, modifier = Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp)) {
                    Row(modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(10.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(text = helper.rating, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextNavy)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(text = helper.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextNavy, textAlign = TextAlign.Center)
            Text(text = helper.spec, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = TextMuted, textAlign = TextAlign.Center)
            Spacer(Modifier.height(10.dp))
            Button(onClick = onPesanClick, colors = ButtonDefaults.buttonColors(containerColor = BgChip), shape = RoundedCornerShape(10.dp), modifier = Modifier.height(30.dp)) {
                Text(text = "Pesan Jasa", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DarkBlue)
            }
        }
    }
}

// ─── NAVBAR BAWAH (PILL SHAPE & TERHUBUNG SEMUA) ──────────────────────────────────
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