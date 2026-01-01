package top.liewyoung.aiwechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import top.liewyoung.aiwechat.ui.AppNavigation
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIWeChatTheme {
                AppNavigation()
            }
        }
    }
}
