package com.example.tolongin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihAkunScreen(navController: NavController) { // SEKARANG SUDAH ADA PARAMETER INI
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pilih akun",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        color = Color(0xff212d51)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xff0084ff)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xfff6f5ff)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))

                // Logo Utama
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xff005ab2), Color(0xff64a1ff)),
                                start = Offset(0f, 0f),
                                end = Offset(150f, 150f)
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tolong.in",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = Color(0xff212d51)
                )

                Text(
                    text = "Lanjutkan pendaftaran atau masuk dengan Akun\nGoogle Anda",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color(0xff4e5a81),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Contoh Akun 1
                AccountRow(
                    name = "Brandon",
                    email = "brandon@pristine.com",
                    badgeColor = Color(0xff005ab2),
                    onClick = { navController.navigate("beranda") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contoh Akun 2
                AccountRow(
                    name = "Gus Miftah",
                    email = "gusmiftah@gmail.com",
                    badgeColor = Color(0xffff9800),
                    onClick = { navController.navigate("beranda") }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Gunakan akun lain
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable { /* Aksi tambah akun */ }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xffdbe1ff))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Tambah Akun",
                            tint = Color(0xff4e5a81),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Gunakan akun lain",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        color = Color(0xff212d51)
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))

                // Footer
                Text(
                    text = buildAnnotatedString {
                        append("Untuk melanjutkan, Google akan membagikan nama, alamat email, preferensi bahasa, dan foto profil Anda dengan Tolong.in. Sebelum menggunakan aplikasi ini, Anda dapat meninjau ")
                        withStyle(style = SpanStyle(color = Color(0xff005ab2))) { append("kebijakan privasi") }
                        append(" dan ")
                        withStyle(style = SpanStyle(color = Color(0xff005ab2))) { append("persyaratan layanan") }
                        append(" Google.")
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color(0xff4e5a81),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}

@Composable
fun AccountRow(name: String, email: String, badgeColor: Color, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.size(52.dp)) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.LightGray
                )

                // Status Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    color = Color(0xff212d51)
                )
                Text(
                    text = email,
                    fontSize = 14.sp,
                    color = Color(0xff4e5a81)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLayarPilihAkun() {
    PilihAkunScreen(navController = rememberNavController())
}