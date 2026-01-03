package top.liewyoung.aiwechat.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.viewmodel.compose.viewModel
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.ui.screen.aboutme.AboutMeScreen
import top.liewyoung.aiwechat.ui.screen.bottomScreen.AccountScreen
import top.liewyoung.aiwechat.ui.screen.bottomScreen.ChatListScreen
import top.liewyoung.aiwechat.ui.screen.chat.ChatScreen
import top.liewyoung.aiwechat.ui.screen.contact.AddContactScreen
import top.liewyoung.aiwechat.ui.screen.contact.ContactListScreen
import top.liewyoung.aiwechat.ui.screen.utils.ViewType
import top.liewyoung.aiwechat.viewmodel.ContactViewModel


enum class PageType {
    Home, CONTRACT, ME, SETTINGS, ABOUT
}

@Composable
fun MainScreenContent(
    selectedTab: Int,
    viewType: ViewType,
    modifier: Modifier,
    onNavigateToSettings: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
    onNavigateToShareContact: (String) -> Unit,
) {
    when (viewType) {
        ViewType.LIST_ONLY -> MainScreenListOnly(
            selectedTab,
            modifier,
            onNavigateToSettings,
            onNavigateToChat,
            onNavigateToAddContact,
            onNavigateToQRScanner,
            onNavigateToShareContact
        )

        else -> MainScreenListAndView(
            selectedTab,
            modifier,
            onNavigateToSettings,
            onNavigateToChat,
            onNavigateToAddContact,
            onNavigateToQRScanner,
            onNavigateToShareContact
        )
    }
}


@Composable
fun MainScreenListOnly(
    selectedTab: Int,
    modifier: Modifier,
    onNavigateToSettings: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
    onNavigateToShareContact: (String) -> Unit,
) {
    Box(
        modifier = modifier
    ) {

        when (selectedTab) {
            0 ->
                ChatListScreen(
                    onChatClick = onNavigateToChat,
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
                    onSettingsClicked = onNavigateToSettings
                )
        }

    }


}


@Composable
fun MainScreenListAndView(
    selectedTab: Int,
    modifier: Modifier,
    onNavigateToSettings: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
    onNavigateToShareContact: (String) -> Unit,
) {
    // 1. 移除冗余的 showSettings, showAboutMe，只保留 pageType 和 数据ID
    var contractId by remember { mutableStateOf("") }
    var pageType by remember { mutableStateOf(PageType.Home) }

    Surface(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (selectedTab) {
                    0 -> {
                        ChatListScreen(
                            onChatClick = {
                                contractId = it
                                pageType = PageType.Home
                            },
                            onAddContact = {
                                contractId = ""
                                pageType = PageType.Home
                            }
                        )
                    }

                    1 -> {
                        ContactListScreen(
                            onAddContact = {
                                contractId = ""
                                pageType = PageType.CONTRACT
                            },
                            onContactClick = {
                                contractId = it
                                pageType = PageType.CONTRACT
                            },
                            onScanQR = onNavigateToQRScanner,
                            onShareContact = onNavigateToShareContact
                        )
                    }

                    2 -> {
                        AccountScreen(
                            onSettingsClicked = {
                                pageType = PageType.SETTINGS
                            }
                        )
                    }
                }
            }


            Box(modifier = Modifier.weight(1f)) {


                AnimatedContent(
                    targetState = Pair(pageType,contractId),
                    label = "RightPaneAnimation",
                    transitionSpec = {
                        (slideInHorizontally { width -> width } + fadeIn(animationSpec = tween(400)))
                            .togetherWith(
                                slideOutHorizontally { width -> -width / 5 } + fadeOut(animationSpec = tween(400))
                            )
                    }

                ) {
                    when (pageType) {

                        PageType.Home -> {
                            if (contractId.isNotEmpty()) {
                                ChatScreen(contractId, { contractId = "" })
                            } else {
                                AddContactScreen({ /* 关闭逻辑 */ })
                            }
                        }

                        PageType.CONTRACT -> {
                            if (contractId.isNotEmpty()) {
                                ChatScreen(contractId, { contractId = "" })
                            } else {
                                AddContactScreen({ /* 关闭逻辑 */ })
                            }
                        }


                        PageType.ME -> {
                            AboutMeScreen { }
                        }

                        PageType.SETTINGS -> {
                            SettingsScreen(
                                onBackClicked = {
                                    pageType = PageType.ME
                                },
                                onNavigateToAboutMe = {
                                    pageType = PageType.ABOUT
                                }
                            )
                        }


                        PageType.ABOUT -> {
                            AboutMeScreen {
                                pageType = PageType.SETTINGS
                            }
                        }
                    }
                }


            }
        }
    }
}


