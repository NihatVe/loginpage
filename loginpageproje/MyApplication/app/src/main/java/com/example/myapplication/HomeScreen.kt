package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.burnoutcrew.reorderable.*
import java.io.IOException
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Color




@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    var itemList by remember {
        mutableStateOf(loadHardwareList(context))
    }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        itemList = itemList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state)
                .padding(bottom = 72.dp, start = 16.dp, end = 16.dp)
        ) {
            itemsIndexed(itemList, key = { _, item -> item.title }) { index, item ->
                ReorderableItem(state, key = item.title) { isDragging ->
                    val elevation = if (isDragging) 8.dp else 2.dp

                    Column(modifier = Modifier.fillMaxWidth()) {
                        ComponentCard(item = item, elevation = elevation)
                        Spacer(modifier = Modifier.height(16.dp)) // 🟢 Her item arası margin
                    }
                }
            }
        }


        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Çıkış Yap")
        }
    }
}

@Composable
fun ComponentCard(item: Item, elevation: Dp = 4.dp) {
    val backgroundColor = getCategoryColor(item.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = getImageResId(item.image)),
                contentDescription = item.title,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            Column {
                Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


fun loadHardwareList(context: Context): List<Item> {
    return try {
        val inputStream = context.assets.open("hardware_data.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        Gson().fromJson(json, Array<Item>::class.java).toList()
    } catch (e: IOException) {
        emptyList()
    }
}

fun getImageResId(imageName: String?): Int {
    return when (imageName?.lowercase()) {
        "gpu" -> R.drawable.gpu
        "cpu" -> R.drawable.cpu
        "ram" -> R.drawable.ram
        "ssd" -> R.drawable.ssd
        "psu" -> R.drawable.psu
        "motherboard" -> R.drawable.motherboard
        "case" -> R.drawable.pc_case
        "cooler" -> R.drawable.cooler
        else -> R.drawable.ic_launcher_foreground
    }
}



fun getCategoryColor(category: String?): Color {
    return when (category?.lowercase()) {
        "cpu" -> Color(0xFFE3F2FD)       // Açık Mavi
        "gpu" -> Color(0xFFF1F8E9)       // Açık Yeşil
        "ram" -> Color(0xFFFFF9C4)       // Açık Sarı
        "ssd" -> Color(0xFFFFEBEE)       // Açık Pembe
        "cooler" -> Color(0xFFEDE7F6)    // Açık Mor
        else -> Color(0xFFFFFFFF)        // Varsayılan Beyaz
    }
}

