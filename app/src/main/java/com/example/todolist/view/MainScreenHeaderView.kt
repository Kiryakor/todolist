package com.example.todolist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.ui.theme.TODOListTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MainScreenHeaderView() {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale("ru"))
    val current = formatter.format(time)

    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = current.replaceFirstChar(Char::titlecase),
            color = Color.Black,
            style = TextStyle(fontSize = 16.sp),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 6.dp)
        )
        Text(
            text = "Хорошего дня !",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenHeaderViewPreview() {
    TODOListTheme {
        MainScreenHeaderView()
    }
}