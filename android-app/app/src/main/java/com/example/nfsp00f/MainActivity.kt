package com.example.nfsp00f

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Main Activity for nf-sp00f EMV Security Platform Minimal implementation to achieve BUILD
 * SUCCESSFUL
 */
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("nf-sp00f EMV Platform")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "🏴‍☠️ $name")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MaterialTheme { Greeting("nf-sp00f EMV Platform") }
}
