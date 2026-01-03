package top.liewyoung.aiwechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import top.liewyoung.aiwechat.ui.AppNavigation
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIWeChatTheme {
                val windowSize = calculateWindowSizeClass(this)
                AppNavigation(windowSize.widthSizeClass)
            }
        }
    }
}
