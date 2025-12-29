package top.liewyoung.aiwechat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.liewyoung.aiwechat.model.MessageOverview
import top.liewyoung.aiwechat.ui.screen.chatlist.MainBody
import top.liewyoung.aiwechat.ui.screen.chatlist.MessageCard
import top.liewyoung.aiwechat.ui.screen.chatlist.MessageList
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var messageCount by remember { mutableIntStateOf(0) }
            val messagesList = remember { mutableStateListOf<MessageOverview>() }

            AIWeChatTheme {
                Scaffold(
                    topBar = {
                        AIWeChatTopAppBar(messageCount, {
                            messageCount++
                            messagesList.add(
                                MessageOverview(
                                    "刘阳阳",
                                    "现在几点了"
                                )
                            )
                        })
                    },
                    bottomBar = { BottomBar() },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MainBody(innerPadding, messagesList)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart Called")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIWeChatTopAppBar(currentMessages: Int, onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (currentMessages > 0) "Chat(${currentMessages})" else "Chat",
                fontWeight = FontWeight.Bold
            )
        },
        modifier = Modifier.padding(2.dp),
        actions = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    )
}




@Composable
fun BottomBar() {
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

        }, onClick = {})


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

        }, onClick = {})


    }
}

@Preview(showBackground = true)
@Composable
fun TopbarPreview(){
    AIWeChatTopAppBar(0) {}
}
