package top.liewyoung.aiwechat.ui.screen.bottomScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.liewyoung.aiwechat.R
import top.liewyoung.aiwechat.ui.component.appbar.AIWeChatMainBottomBar
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(onMessageButtonClicked: () -> Unit, onSettingsClicked: () -> Unit) {
    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("我", fontWeight = FontWeight.Bold) })
            },
            bottomBar = { AIWeChatMainBottomBar(onMessageButtonClicked) {} }
    ) { inner ->
        Column(
                modifier = Modifier.padding(inner).fillMaxSize(),
        ) {
            Card(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Image(
                            painter = painterResource(R.drawable.avastar),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(64.dp).clip(CircleShape)
                    )

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("LiewYoung", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ID: wechat_id_12345", fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                    modifier =
                            Modifier.fillMaxWidth().padding(horizontal = 10.dp).clickable {
                                onSettingsClicked()
                            }
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "设置")
                    Text(text = stringResource(R.string.settings), modifier = Modifier.padding(start = 16.dp).weight(1f))
                    Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AIWeChatTheme { AccountScreen({}, {}) }
}
