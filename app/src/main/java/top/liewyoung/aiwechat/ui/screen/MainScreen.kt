package top.liewyoung.aiwechat.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.liewyoung.aiwechat.ui.screen.bottomScreen.AccountScreen
import top.liewyoung.aiwechat.ui.screen.bottomScreen.ChatListScreen
import top.liewyoung.aiwechat.ui.screen.contact.ContactListScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
    onNavigateToShareContact: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "聊天") },
                    label = { Text("聊天") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Contacts, contentDescription = "联系人") },
                    label = { Text("联系人") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "我") },
                    label = { Text("我") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 ->
                ChatListScreen(
                    onChatClick = onNavigateToChat,
                    onContactsClick = { selectedTab = 1 },
                    onAccountClick = { selectedTab = 2 },
                    onAddContact = onNavigateToAddContact
                )
            1 ->
                ContactListScreen(
                    onAddContact = onNavigateToAddContact,
                    onContactClick = onNavigateToChat,
                    onScanQR = onNavigateToQRScanner,
                    onShareContact = onNavigateToShareContact
                )
            2 ->
                AccountScreen(
                    onMessageButtonClicked = { selectedTab = 0 },
                    onSettingsClicked = onNavigateToSettings
                )
        }
    }
}
