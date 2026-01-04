package top.liewyoung.aiwechat.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.liewyoung.aiwechat.R
import top.liewyoung.aiwechat.ui.screen.bottomScreen.AccountScreen
import top.liewyoung.aiwechat.ui.screen.bottomScreen.ChatListScreen
import top.liewyoung.aiwechat.ui.screen.contact.ContactListScreen
import top.liewyoung.aiwechat.ui.screen.utils.NavigationType
import top.liewyoung.aiwechat.ui.screen.utils.ViewType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
    onNavigateToShareContact: (String) -> Unit,
    windowSize: WindowWidthSizeClass
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    //自适应导航栏
    val navigationBarType = when (windowSize) {
        WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION_BAR
        WindowWidthSizeClass.Medium -> NavigationType.SIDE_NAVIGATION_BAR
        WindowWidthSizeClass.Expanded -> NavigationType.DRAWER_NAVIGATION_BAR
        else -> NavigationType.BOTTOM_NAVIGATION_BAR
    }


    val contentType = when (windowSize) {
        WindowWidthSizeClass.Compact -> ViewType.LIST_ONLY
        WindowWidthSizeClass.Medium -> ViewType.LIST_ONLY
        WindowWidthSizeClass.Expanded -> ViewType.LIST_AND_VIEW
        else -> ViewType.LIST_ONLY
    }


    if (navigationBarType == NavigationType.DRAWER_NAVIGATION_BAR) {

        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                    Spacer(modifier = Modifier.size(24.dp))

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Chat, contentDescription = "聊天",tint = MaterialTheme.colorScheme.primary) },
                        label = { Text(stringResource(R.string.chat)) },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Contacts, contentDescription = "联系人",tint = MaterialTheme.colorScheme.primary) },
                        label = { Text(stringResource(R.string.contract)) },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "我",tint = MaterialTheme.colorScheme.primary) },
                        label = { Text(stringResource(R.string.me)) },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            MainScreenContent(
                selectedTab,
                contentType,
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                onNavigateToSettings,
                onNavigateToChat,
                onNavigateToAddContact,
                onNavigateToQRScanner,
                onNavigateToShareContact
            )
        }
    } else {

        Scaffold(
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                if (navigationBarType == NavigationType.SIDE_NAVIGATION_BAR) {
                    NavigationBarsRail(selectedTab) { selectedTab = it }
                }

                Column {
                    MainScreenContent(
                        selectedTab,
                        contentType,
                        Modifier.weight(1f),
                        onNavigateToSettings,
                        onNavigateToChat,
                        onNavigateToAddContact,
                        onNavigateToQRScanner,
                        onNavigateToShareContact
                    )

                    if (navigationBarType == NavigationType.BOTTOM_NAVIGATION_BAR) {
                        NavigationBarsBottom(selectedTab) { selectedTab = it }
                    }

                }

            }


        }
    }


}

@Composable
fun NavigationBarsBottom(selectedTab: Int, onClickItem: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "聊天", tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.chat)) },
            selected = selectedTab == 0,
            onClick = { onClickItem(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Contacts, contentDescription = "联系人",tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.contract)) },
            selected = selectedTab == 1,
            onClick = { onClickItem(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "我",tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.me)) },
            selected = selectedTab == 2,
            onClick = { onClickItem(2) }
        )
    }
}


@Composable
fun NavigationBarsRail(selectedTab: Int, onClickItem: (Int) -> Unit) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight()
    ) {
        NavigationRailItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "聊天",tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.chat)) },
            selected = selectedTab == 0,
            modifier = Modifier.weight(1f),
            onClick = { onClickItem(0) }
        )

        NavigationRailItem(
            icon = { Icon(Icons.Default.Contacts, contentDescription = "联系人",tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.contract)) },
            selected = selectedTab == 1,
            modifier = Modifier.weight(1f),
            onClick = { onClickItem(1) }
        )

        NavigationRailItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "我",tint = MaterialTheme.colorScheme.primary) },
            label = { Text(stringResource(R.string.me)) },
            selected = selectedTab == 2,
            modifier = Modifier.weight(1f),
            onClick = { onClickItem(2) }
        )

    }
}


@Preview
@Composable
fun NavigationBarsRailPreview() {
    NavigationBarsRail(0) { }
}
