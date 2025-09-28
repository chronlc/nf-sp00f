package com.example.nfsp00f

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SplashActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme(
              colorScheme =
                      darkColorScheme(
                              background = Color.Black,
                              surface = Color.Black,
                              primary = Color(0xFF4CAF50)
                      )
      ) { SplashScreen() }
    }

    // Navigate to MainActivity after 3 seconds with smooth transition
    Handler(Looper.getMainLooper())
            .postDelayed(
                    {
                      startActivity(Intent(this, MainActivity::class.java))
                      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                      finish()
                    },
                    3000
            )
  }
}

@Composable
fun SplashScreen() {
  var isVisible by remember { mutableStateOf(false) }

  // Trigger animations on composition
  LaunchedEffect(Unit) { isVisible = true }

  // Animation specs following design guidelines
  val fadeInAnimation =
          animateFloatAsState(
                  targetValue = if (isVisible) 1f else 0f,
                  animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
          )

  Box(
          modifier = Modifier.fillMaxSize().background(Color.Black),
          contentAlignment = Alignment.Center
  ) {
    // Background logo with subtle alpha
    Image(
            painter = painterResource(id = R.drawable.nfspoof_logo),
            contentDescription = "nf-sp00f Background Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().padding(48.dp).alpha(0.15f * fadeInAnimation.value)
    )

    // Main content with typography scale
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp).alpha(fadeInAnimation.value)
    ) {
      // Main title - Headlines: Roboto Bold 30sp
      Text(
              text = "nf-sp00f",
              fontSize = 30.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Default, // Roboto is default on Android
              color = Color(0xFF4CAF50),
              textAlign = TextAlign.Center,
              letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Subtitle - Titles: Roboto Bold 22sp
      Text(
              text = "NFC PhreaK BoX",
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Default,
              color = Color(0xFF4CAF50),
              textAlign = TextAlign.Center,
              letterSpacing = 0.25.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Description - Titles: Roboto Regular 16sp
      Text(
              text = "RFiD TooLKiT",
              fontSize = 16.sp,
              fontWeight = FontWeight.Normal,
              fontFamily = FontFamily.Default,
              color = Color(0xFFFFFFFF), // White color
              textAlign = TextAlign.Center,
              letterSpacing = 0.15.sp,
              textDecoration = TextDecoration.Underline
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Loading indicator with smooth animation
      CircularProgressIndicator(
              modifier = Modifier.size(32.dp),
              color = Color(0xFF4CAF50),
              strokeWidth = 3.dp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Loading text - Body: Roboto Regular 14sp
      Text(
              text = "Initializing EMV Platform...",
              fontSize = 14.sp,
              fontWeight = FontWeight.Normal,
              fontFamily = FontFamily.Default,
              color = Color(0xFF4CAF50).copy(alpha = 0.6f),
              textAlign = TextAlign.Center
      )
    }
  }
}
