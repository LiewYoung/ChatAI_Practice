package top.liewyoung.aiwechat.ui.screen.bottomScreen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.R

import top.liewyoung.aiwechat.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onSettingsClicked: () -> Unit,
    viewModel: AccountViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as AIWeChatApplication)
            .container
            .provideAccountViewModelFactory()
    )
) {

    val uiState by viewModel.uiState.collectAsState()

    val account = uiState.account

    val context = LocalContext.current
    // TODO(完成编辑事件)
    var showDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                Log.d("IMAGE", "GOT")
                viewModel.updateAvatar(uri,context)
            }
        }
    )



    if (showDialog) {
        EditInfoDialog(
            account?.userName ?: "",
            account?.userId ?: "",
            account?.userAvatar?:R.drawable.avastar,
            onClickAvatar = {
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            { showDialog = false }) { name, id ->
            viewModel.updateInfo(name, id)
            showDialog = false
        }
    }
    AccountScreenContent(
        account?.userName ?: "NULL",
        account?.userId ?: "NULL",
        account?.userAvatar,
        onButtonClicked = {
            showDialog = true
        },
        onSettingsClicked
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreenContent(
    name: String,
    id: String,
    imageUri: Any?,
    onButtonClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("我", fontWeight = FontWeight.Bold) })
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = if (imageUri is String && imageUri.isNotEmpty()) imageUri else R.drawable.avastar,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ID: $id", fontSize = 14.sp)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onButtonClicked,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }


                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clickable {
                            onSettingsClicked()
                        }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.settings),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun EditInfoDialog(
    initName: String,
    initID: String,
    avatar: Any?,
    onClickAvatar:()-> Unit,
    onDismissRequest: () -> Unit,
    onDismiss: (name: String, id: String) -> Unit
) {
    var name by remember { mutableStateOf(initName) }
    var id by remember { mutableStateOf(initID) }
    var image by remember { mutableStateOf(avatar) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(

        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AsyncImage(
                    model = image,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = onClickAvatar
                        )
                )

                Spacer(modifier = Modifier.size(10.dp))


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it }
                )
                Spacer(modifier = Modifier.size(10.dp))
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it }
                )

                Spacer(modifier = Modifier.size(10.dp))

                Button(
                    onClick = { onDismiss(name, id) }
                ) {
                    Text("保存")
                }
            }
        }
    }
}


@Preview
@Composable
fun AccountScreenPreview() {
    AccountScreenContent("Alice", "Young123", null, {}) { }
}

@Preview(showBackground = true)
@Composable
fun EditInfoDialogPreview() {
    EditInfoDialog("", "", R.drawable.avastar,{},{}) { name, id ->
    }
}
