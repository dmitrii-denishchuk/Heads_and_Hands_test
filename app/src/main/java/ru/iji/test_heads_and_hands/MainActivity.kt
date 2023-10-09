package ru.iji.test_heads_and_hands

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.iji.test_heads_and_hands.presentation.screen.MainScreen
import ru.iji.test_heads_and_hands.presentation.theme.Test_Heads_and_HandsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test_Heads_and_HandsTheme {
                MainScreen()
            }
        }
    }
}