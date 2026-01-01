package top.liewyoung.aiwechat.ui.component.appbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AIWeChatMainBottomBar(onMessageButtonClicked: () -> Unit, onAccountButtonClicked: () -> Unit) {
    NavigationBar {

        NavigationBarItem(selected = false, icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Message",
                    modifier = Modifier.size(36.dp)
                )
                Text("信息", fontWeight = FontWeight.Bold)
            }

        }, onClick = onMessageButtonClicked)


        NavigationBarItem(selected = false, icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Account",
                    modifier = Modifier.size(36.dp)
                )
                Text("账户", fontWeight = FontWeight.Bold)
            }

        }, onClick = onAccountButtonClicked)


    }
}

@Preview(showBackground = true)
@Composable
fun AIWeChatMainBottomBarPreview(){
    AIWeChatMainBottomBar({},{})
}