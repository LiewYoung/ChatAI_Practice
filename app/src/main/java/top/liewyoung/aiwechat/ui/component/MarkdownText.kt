package top.liewyoung.aiwechat.ui.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * Simple Markdown text renderer for chat messages Supports: **bold**, *italic*, `code`,
 * ~~strikethrough~~, [links](url)
 */
@Composable
fun MarkdownText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colorScheme.onSurface,
        onLinkClick: ((String) -> Unit)? = null
) {
    val annotatedString = parseMarkdown(text, color)

    if (onLinkClick != null) {
        ClickableText(
                text = annotatedString,
                modifier = modifier,
                onClick = { offset ->
                    annotatedString
                            .getStringAnnotations("URL", offset, offset)
                            .firstOrNull()
                            ?.let { annotation -> onLinkClick(annotation.item) }
                }
        )
    } else {
        androidx.compose.material3.Text(text = annotatedString, modifier = modifier)
    }
}

private fun parseMarkdown(text: String, defaultColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val input = text

        // Regex patterns for markdown
        val patterns =
                listOf(
                        // Code blocks (```code```)
                        Regex("```([^`]+)```") to
                                { match: MatchResult ->
                                    withStyle(
                                            SpanStyle(
                                                    fontFamily = FontFamily.Monospace,
                                                    background = Color.Gray.copy(alpha = 0.2f)
                                            )
                                    ) { append(match.groupValues[1]) }
                                },
                        // Inline code (`code`)
                        Regex("`([^`]+)`") to
                                { match: MatchResult ->
                                    withStyle(
                                            SpanStyle(
                                                    fontFamily = FontFamily.Monospace,
                                                    background = Color.Gray.copy(alpha = 0.2f)
                                            )
                                    ) { append(match.groupValues[1]) }
                                },
                        // Bold (**text**)
                        Regex("\\*\\*([^*]+)\\*\\*") to
                                { match: MatchResult ->
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(match.groupValues[1])
                                    }
                                },
                        // Italic (*text*)
                        Regex("\\*([^*]+)\\*") to
                                { match: MatchResult ->
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                        append(match.groupValues[1])
                                    }
                                },
                        // Strikethrough (~~text~~)
                        Regex("~~([^~]+)~~") to
                                { match: MatchResult ->
                                    withStyle(
                                            SpanStyle(textDecoration = TextDecoration.LineThrough)
                                    ) { append(match.groupValues[1]) }
                                },
                        // Links [text](url)
                        Regex("\\[([^\\]]+)\\]\\(([^)]+)\\)") to
                                { match: MatchResult ->
                                    val linkText = match.groupValues[1]
                                    val url = match.groupValues[2]
                                    pushStringAnnotation("URL", url)
                                    withStyle(
                                            SpanStyle(
                                                    color = Color(0xFF2196F3),
                                                    textDecoration = TextDecoration.Underline
                                            )
                                    ) { append(linkText) }
                                    pop()
                                }
                )

        // Simple approach: process text character by character with pattern matching
        var remainingText = input

        while (remainingText.isNotEmpty()) {
            var matched = false

            for ((pattern, handler) in patterns) {
                val match = pattern.find(remainingText)
                if (match != null && match.range.first == 0) {
                    handler(match)
                    remainingText = remainingText.substring(match.range.last + 1)
                    matched = true
                    break
                }
            }

            if (!matched) {
                // Check if any pattern matches later in the string
                var nextMatchStart = remainingText.length
                for ((pattern, _) in patterns) {
                    val match = pattern.find(remainingText)
                    if (match != null && match.range.first < nextMatchStart) {
                        nextMatchStart = match.range.first
                    }
                }

                // Append plain text up to next match
                if (nextMatchStart > 0) {
                    withStyle(SpanStyle(color = defaultColor)) {
                        append(remainingText.substring(0, nextMatchStart))
                    }
                    remainingText = remainingText.substring(nextMatchStart)
                } else {
                    withStyle(SpanStyle(color = defaultColor)) { append(remainingText) }
                    break
                }
            }
        }
    }
}
