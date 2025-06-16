package com.luci.aeris.utils.components

import BodyText
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AirisAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDivider: Boolean = false,
    iconColor: Color = MaterialTheme.colorScheme.error
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            BodyText(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                BodyText(
                    text = message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                if (showDivider) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                OutlinedButton(onClick = onDismiss) {
                    BodyText(
                        text = dismissText,
                        fontSize = 12.sp,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(onClick = onConfirm) {
                    BodyText(
                        text = confirmText,
                        fontSize = 12.sp,
                        textColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}
