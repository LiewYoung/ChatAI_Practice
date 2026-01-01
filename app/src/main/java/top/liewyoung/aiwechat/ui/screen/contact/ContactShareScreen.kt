package top.liewyoung.aiwechat.ui.screen.contact

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.liewyoung.aiwechat.model.Contact
import top.liewyoung.aiwechat.util.QRCodeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactShareScreen(contact: Contact, onBack: () -> Unit) {
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(contact) { qrCodeBitmap = QRCodeUtil.generateQRCode(contact, 512) }

    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = { Text("分享联系人", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                            }
                        }
                )
            }
    ) { innerPadding ->
        Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                    text = contact.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                    text = "让对方扫描下方二维码添加此联系人",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            qrCodeBitmap?.let { bitmap ->
                Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "联系人二维码",
                        modifier = Modifier.size(256.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                    text = "性格设定：${contact.prompt}",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 3
            )
        }
    }
}
