package top.liewyoung.aiwechat.ui.screen.bottomScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.model.Contact
import top.liewyoung.aiwechat.ui.component.appbar.AIWeChatMainBottomBar
import top.liewyoung.aiwechat.ui.component.appbar.AIWeChatMainTopAppBar
import top.liewyoung.aiwechat.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    onContactsClick: () -> Unit,
    onAccountClick: () -> Unit,
    onAddContact: () -> Unit,
    viewModel: ChatListViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as AIWeChatApplication).container.provideChatListViewModelFactory()
    )
) {
    val contacts by viewModel.filteredContacts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = { 
            AIWeChatMainTopAppBar(
                currentMessages = contacts.filter { it.unreadCount > 0 }.size,
                onSearchClick = { /* TODO: Implement search */ },
                onAddClick = onAddContact
            ) 
        },
        bottomBar = { AIWeChatMainBottomBar(onContactsClick, onAccountClick) }
    ) { inner ->
        MainBody(
            inner,
            contacts,
            searchQuery,
            onSearchChange = viewModel::updateSearch,
            onChatClick = onChatClick,
            onDeleteChat = viewModel::deleteChat // Pass the delete function
        )
    }
}

@Composable
fun MainBody(
    paddingValues: PaddingValues,
    contacts: List<Contact>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onDeleteChat: (String) -> Unit // Receive the delete function
) {
    Column(
        modifier = Modifier.padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageList(
            contacts = contacts,
            searchQuery = searchQuery,
            onSearchChange = onSearchChange,
            onChatClick = onChatClick,
            onDeleteChat = onDeleteChat, // Pass it down
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun MessageList(
    contacts: List<Contact>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onDeleteChat: (String) -> Unit, // Receive the delete function
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            // Search box
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "搜索联系人",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    )
                }

                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true
                )
            }
        }

        if (contacts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) { Text("还没有聊天记录，添加联系人开始聊天吧！") }
            }
        } else {
            items(contacts, key = { it.id }) { contact ->
                MessageCard(
                    contact = contact,
                    onClick = { onChatClick(contact.id) },
                    onDelete = { onDeleteChat(contact.id) } // Pass the delete action
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.LightGray
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(contact: Contact, onClick: () -> Unit, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showMenu = true }
                )
                .padding(0.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = contact.avatar, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.size(6.dp))

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(contact.name, fontWeight = FontWeight.Normal, fontSize = 18.sp)
                    if (contact.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Red, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = contact.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Text(
                    text = contact.lastMessage ?: "暂无消息",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("删除聊天") },
                onClick = {
                    onDelete()
                    showMenu = false
                }
            )
        }
    }
}
