package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DuaNote
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel

@Composable
fun NotesScreen(
    viewModel: IbadatViewModel,
    duaNotes: List<DuaNote>
) {
    var titleInput by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("") }

    val manualNotes = remember(duaNotes) { duaNotes.filter { !it.isAuto } }
    val autoNotes = remember(duaNotes) { duaNotes.filter { it.isAuto } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // New Note Input Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "নতুন নোট লিখুন",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        placeholder = { Text("হেড/শিরোনাম (ঐচ্ছিক)", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = BorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("এখানে লিখুন...", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = BorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.addNote(titleInput, textInput)
                                titleInput = ""
                                textInput = ""
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

        // Section: My Notes
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.NoteAlt,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "আমার নোটস (${manualNotes.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }

        if (manualNotes.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                ) {
                    Text(
                        text = "এখনও কোনো নোট যোগ করা হয়নি। উপরের বক্সে লিখে সেভ করুন।",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(manualNotes) { note ->
                NoteCard(
                    note = note,
                    onClick = { viewModel.openNoteDetail(note) },
                    onDelete = { viewModel.deleteNote(note.id) }
                )
            }
        }

        // Section: Library Notes
        item {
            HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "স্বয়ংক্রিয় লাইব্রেরি নোটস (${autoNotes.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }

        items(autoNotes) { note ->
            NoteCard(
                note = note,
                onClick = { viewModel.openNoteDetail(note) },
                onDelete = null
            )
        }
    }
}

@Composable
fun NoteCard(
    note: DuaNote,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color(0xFFF2F7FC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCCFBF1)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (note.title.isNotBlank()) note.title else note.date,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (onDelete != null) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = CoralRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = note.text,
                fontSize = 13.sp,
                color = Color(0xFF475569),
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.date,
                fontSize = 11.sp,
                color = TextLight,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
