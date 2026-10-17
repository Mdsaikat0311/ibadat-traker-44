package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DuaNote
import com.example.data.model.LeaderboardUser
import com.example.data.model.ReminderSetting
import com.example.ui.theme.*

@Composable
fun DetailsDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDeep,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                HorizontalDivider(color = BorderLight)

                // Body
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp)
                ) {
                    Text(
                        text = content,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 22.sp
                    )
                }

                HorizontalDivider(color = BorderLight)

                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("বন্ধ করুন", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderDialog(
    activityKey: String,
    activityName: String,
    initialSetting: ReminderSetting?,
    onSave: (String, String, String, Int, Int) -> Unit,
    onDelete: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var startTime by remember { mutableStateOf(initialSetting?.start ?: "06:00") }
    var countText by remember { mutableStateOf(initialSetting?.count?.toString() ?: "3") }
    var intervalText by remember { mutableStateOf(initialSetting?.interval?.toString() ?: "10") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "রিমাইন্ডার সেট করুন",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$activityName এর জন্য সেটিংস",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Start Time
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("১. শুরু করার সময় (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Count
                OutlinedTextField(
                    value = countText,
                    onValueChange = { countText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("২. মোট কতবার রিমাইন্ডার দেবে (১-১০)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Interval
                OutlinedTextField(
                    value = intervalText,
                    onValueChange = { intervalText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("৩. কত মিনিট পর পর দেবে (মিনিটে)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (initialSetting != null) {
                        OutlinedButton(
                            onClick = { onDelete(activityKey, activityName) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralRed),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("বাতিল করুন", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDismiss) {
                            Text("বন্ধ", color = TextMuted)
                        }
                        Button(
                            onClick = {
                                val cnt = countText.toIntOrNull() ?: 3
                                val itv = intervalText.toIntOrNull() ?: 10
                                onSave(activityKey, activityName, startTime, cnt.coerceIn(1, 10), itv.coerceAtLeast(1))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("সেভ করুন", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteDetailDialog(
    note: DuaNote,
    onDelete: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (note.title.isNotBlank()) note.title else note.date,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDeep,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(color = BorderLight)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = note.text,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 22.sp
                    )
                }

                HorizontalDivider(color = BorderLight)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.date,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    if (!note.isAuto) {
                        TextButton(
                            onClick = { onDelete(note.id) },
                            colors = ButtonDefaults.textButtonColors(contentColor = CoralRed)
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("মুছে ফেলুন", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("লাইব্রেরি নোট", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardDialog(
    title: String,
    users: List<LeaderboardUser>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0FDFA))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.EmojiEvents, contentDescription = null, tint = AmberAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealDeep
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(color = BorderLight)

                // List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        val isMe = user.isCurrentUser
                        val rowBg = if (isMe) Color(0xFFCCFBF1) else Color.White
                        val border = if (isMe) TealPrimary else BorderLight

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(rowBg)
                                .border(1.dp, border, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (user.rank) {
                                                1 -> Color(0xFFFACC15)
                                                2 -> Color(0xFF94A3B8)
                                                3 -> Color(0xFFFB923C)
                                                else -> Color(0xFFF1F5F9)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (user.rank == 1) {
                                        Text("👑", fontSize = 14.sp)
                                    } else {
                                        Text(
                                            text = "${user.rank}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (user.rank <= 3) Color.White else TextDark
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = user.name,
                                    fontSize = 14.sp,
                                    fontWeight = if (isMe) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = TextDark
                                )
                                if (isMe) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = TealPrimary,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "আমি",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Surface(
                                color = Color(0xFFF0FDFA),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${user.points} pts",
                                    color = TealDeep,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    isSuccess: Boolean,
    isConfirm: Boolean = false,
    onConfirm: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (!isSuccess && isConfirm) CoralRed else TealDeep
            )
        },
        text = {
            Text(
                text = message,
                fontSize = 14.sp,
                color = TextDark,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isConfirm && onConfirm != null) {
                        onConfirm()
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isConfirm) CoralRed else TealPrimary
                )
            ) {
                Text(if (isConfirm) "নিশ্চিত করুন" else "ঠিক আছে", color = Color.White)
            }
        },
        dismissButton = if (isConfirm) {
            {
                TextButton(onClick = onDismiss) {
                    Text("বাতিল করুন", color = TextMuted)
                }
            }
        } else null
    )
}
