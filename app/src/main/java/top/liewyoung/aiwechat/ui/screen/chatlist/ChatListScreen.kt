package top.liewyoung.aiwechat.ui.screen.chatlist

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.liewyoung.aiwechat.ui.component.MessageExpandButton
import top.liewyoung.aiwechat.R
import top.liewyoung.aiwechat.model.MessageOverview


@Composable
fun MainBody(paddingValues: PaddingValues, messagesList: List<MessageOverview>) {

    Column(
        modifier = Modifier.padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageList(messagesList, modifier = Modifier.padding(10.dp))
    }
}


@Composable
fun MessageList(messagesList: List<MessageOverview>, modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    LazyColumn(modifier = modifier) {
        item {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {

                if (text.isEmpty()) {
                    Text(
                        text = "搜索",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true
                )
            }

        }

        items(messagesList) { message ->
            MessageCard(message.name, message.overview)
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
        }
    }
}


@Composable
fun MessageCard(name: String, content: String) {

    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.avastar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.size(6.dp))
            Column(
                verticalArrangement = Arrangement.Center,
            )
            {
                Text(name, fontWeight = FontWeight.Normal, fontSize = 18.sp)
                Text(
                    text = if (isExpanded) {
                        ""
                    } else {
                        content.substring(0, 4) + "…"
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            MessageExpandButton(isExpanded, {
                isExpanded = !isExpanded
            }, Modifier.padding(0.dp))
        }
        if (isExpanded) {
            MessageDetail(
                content,
                Modifier.padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 10.dp)
            )
        }
    }


}

@Composable
fun MessageDetail(info: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(info, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun MessageCardPreview() {
    MessageCard("刘阳阳", "现在几点了")
}

@Preview(showBackground = true)
@Composable
fun MainBodyPreview() {
    MainBody(PaddingValues(0.dp), listOf(MessageOverview("刘阳阳", "现在几点了")))
}