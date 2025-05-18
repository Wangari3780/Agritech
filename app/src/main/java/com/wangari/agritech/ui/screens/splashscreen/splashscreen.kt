package com.wangari.agritech.ui.screens.splashscreen

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wangari.agritech.R
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.font.FontStyle

@Composable
fun SplashScreen(navController: NavController, context: Context = LocalContext.current) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val offsetX = remember { Animatable(-200f) }

    LaunchedEffect(true) {
        offsetX.animateTo(
            targetValue = screenWidth.value + 200f,
            animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
        )

        navController.navigate("DashboardScreen") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.tractor),
                contentDescription = "Tractor",
                modifier = Modifier
                    .offset(x = offsetX.value.dp)
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "AgriTech",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "A farmer's bestfriend",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color.White
            )
        }
    }
}
