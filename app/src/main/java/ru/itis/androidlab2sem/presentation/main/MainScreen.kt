package ru.itis.androidlab2sem.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen() {
    val vm: MainViewModel = hiltViewModel()
    val state by vm.state.collectAsState()
    var tag by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = tag,
            onValueChange = { tag = it },
            label = { Text("Enter tag (e.g. 'cute', 'sleepy')")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row {
            Button(
                onClick = { vm.loadRandomCat() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Random Cat")
            }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = { vm.loadCatByTag(tag) },
                modifier = Modifier.weight(1f),
                enabled = tag.isNotEmpty()
            ) {
                Text("By Tag")
            }
        }

        Spacer(Modifier.height(16.dp))

        when (state) {
            is MainState.Loading -> {
                CatItemShimmer()
            }
            is MainState.Success -> {
                (state as MainState.Success).bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Cat"
                    )
                }
            }
            is MainState.Error -> {
                val error = (state as MainState.Error).message
                Text("Error: $error", color = Color.Red)
            }
        }
    }
}