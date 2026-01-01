package top.liewyoung.aiwechat.ui.component.appbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.liewyoung.aiwechat.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIWeChatMainTopAppBar(currentMessages: Int, onSearchClick: () -> Unit, onAddClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (currentMessages > 0) stringResource(R.string.chat)+"(${currentMessages})" else stringResource(R.string.chat),
                fontWeight = FontWeight.Bold
            )
        },
        modifier = Modifier.padding(2.dp),
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Outlined.Search, contentDescription = "Search")
            }
            IconButton(onClick = onAddClick) {
                Icon(Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AIWeChatMainTopAppBarPreview(){
    AIWeChatMainTopAppBar(0, onSearchClick = {}, onAddClick = {})
}
