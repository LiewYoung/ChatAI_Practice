package top.liewyoung.aiwechat.ui.screen.aboutme

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme
import top.liewyoung.aiwechat.BuildConfig


val githubIcon = top.liewyoung.aiwechat.R.drawable.github_mark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMeScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("关于") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // 应用名称
            Text(
                text = "AI WeChat",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 作者
            Text(
                text = "by LiewYoung",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            // GitHub链接
            GithubLink()

            Spacer(modifier = Modifier.height(24.dp))

            // 信息卡片
            InfoCard()
        }
    }
}

@Composable
private fun GithubLink() {
    val context = LocalContext.current
    val githubUrl = "https://github.com/LiewYoung"

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                context.startActivity(intent)
            }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = githubIcon),
            contentDescription = "GitHub Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = " / LiewYoung",
            modifier = Modifier.padding(start = 8.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // 版本信息
        InfoListItem(
            icon = Icons.Default.Info,
            title = "版本",
            subtitle = BuildConfig.VERSION_NAME, // 从BuildConfig获取版本名
            onClick = { /* TODO: 检查更新逻辑 */ }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        // 项目简介
        InfoListItem(
            icon = Icons.Default.Star,
            title = "项目简介",
            subtitle = "一个基于大语言模型的AI聊天应用。",
            showArrow = false // 不需要点击效果
        )
    }
}

@Composable
private fun InfoListItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showArrow: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle, fontSize = 14.sp) },
        trailingContent = {
            if (showArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "More",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AboutMeScreenPreview() {
    // 为了让预览正常工作，你可能需要一个预览用的Theme
    // import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme
    // AIWeChatTheme {
    AIWeChatTheme {
        AboutMeScreen()
    }
    // }
}


