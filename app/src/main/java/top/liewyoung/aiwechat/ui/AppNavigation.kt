package top.liewyoung.aiwechat.ui

import android.view.Window
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.ui.screen.MainScreen
import top.liewyoung.aiwechat.ui.screen.SettingsScreen
import top.liewyoung.aiwechat.ui.screen.aboutme.AboutMeScreen
import top.liewyoung.aiwechat.ui.screen.chat.ChatScreen
import top.liewyoung.aiwechat.ui.screen.contact.AddContactScreen
import top.liewyoung.aiwechat.ui.screen.contact.ContactShareScreen
import top.liewyoung.aiwechat.ui.screen.contact.QRScannerScreen
import top.liewyoung.aiwechat.viewmodel.ContactViewModel

object AppDestinations {
    const val MAIN_ROUTE = "main"
    const val SETTINGS_ROUTE = "settings"
    const val CHAT_ROUTE = "chat"
    const val ADD_CONTACT_ROUTE = "add_contact"
    const val QR_SCANNER_ROUTE = "qr_scanner"
    const val CONTACT_SHARE_ROUTE = "contact_share"

    const val ABOUT_ME = "about_me"
}

@Composable
fun AppNavigation(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val contactViewModel: ContactViewModel =
        viewModel(
            factory =
                (context.applicationContext as AIWeChatApplication).container
                    .provideContactViewModelFactory()
        )

    NavHost(
        navController = navController,
        startDestination = AppDestinations.MAIN_ROUTE,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(400)
            )
        }
    ) {
        composable(AppDestinations.MAIN_ROUTE) {
            MainScreen(
                onNavigateToSettings = {
                    navController.navigate(AppDestinations.SETTINGS_ROUTE)
                },
                onNavigateToChat = { contactId ->
                    navController.navigate("${AppDestinations.CHAT_ROUTE}/$contactId")
                },
                onNavigateToAddContact = {
                    navController.navigate(AppDestinations.ADD_CONTACT_ROUTE)
                },
                onNavigateToQRScanner = {
                    navController.navigate(AppDestinations.QR_SCANNER_ROUTE)
                },
                onNavigateToShareContact = { contactId ->
                    navController.navigate("${AppDestinations.CONTACT_SHARE_ROUTE}/$contactId")
                },
                windowSize
            )
        }

        composable(AppDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                onBackClicked = { navController.popBackStack() },
                onNavigateToAboutMe = {
                    navController.navigate(AppDestinations.ABOUT_ME)
                })
        }

        composable(
            route = "${AppDestinations.CHAT_ROUTE}/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            ChatScreen(contactId = contactId, onBack = { navController.popBackStack() })
        }

        composable(AppDestinations.ADD_CONTACT_ROUTE) {
            AddContactScreen(onBack = { navController.popBackStack() })
        }

        composable(AppDestinations.QR_SCANNER_ROUTE) {
            QRScannerScreen(
                onBack = { navController.popBackStack() },
                onContactScanned = { contact ->
                    navController.popBackStack()
                    navController.navigate("${AppDestinations.CHAT_ROUTE}/${contact.id}")
                }
            )
        }

        composable(
            route = "${AppDestinations.CONTACT_SHARE_ROUTE}/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            val contacts by contactViewModel.contacts.collectAsState()
            val contact = contacts.find { it.id == contactId }
            contact?.let {
                ContactShareScreen(contact = it, onBack = { navController.popBackStack() })
            }

        }

        composable(
            route = AppDestinations.ABOUT_ME
        ) {
            AboutMeScreen() {
                navController.popBackStack()
            }
        }
    }
}
