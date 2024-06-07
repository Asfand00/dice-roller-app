package com.example.diceroller

// import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerApp() {
    var result by remember { mutableIntStateOf(1) }
    var selectedDiceType by remember { mutableIntStateOf(6) }
    val diceTypes = listOf(4, 6, 8, 10, 12, 20)
    val history = remember { mutableStateListOf<Int>() }
    var numOfDice by remember { mutableIntStateOf(1) }

    // val context = LocalContext.current
    // val mediaPlayer = remember { MediaPlayer.create(context, R.raw.dice_roll_sound) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dice Roller") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DiceTypeSelector(
                diceTypes = diceTypes,
                selectedDiceType = selectedDiceType,
                onDiceTypeSelected = { selectedDiceType = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            NumberOfDiceSelector(
                numOfDice = numOfDice,
                onNumOfDiceSelected = { numOfDice = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = getDiceImage(result)),
                contentDescription = "Dice Result",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                // mediaPlayer.start()
                result = (1..numOfDice).sumOf { Random.nextInt(1, selectedDiceType + 1) }
                history.add(result)
            }) {
                Text(text = "Roll")
            }
            Spacer(modifier = Modifier.height(24.dp))
            HistoryList(history)
        }
    }
}

fun getDiceImage(result: Int): Int {
    return when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1 // default case, should handle up to 6 for D6
    }
}

@Composable
fun DiceTypeSelector(diceTypes: List<Int>, selectedDiceType: Int, onDiceTypeSelected: (Int) -> Unit) {
    Text(text = "Dice Type:", style = MaterialTheme.typography.titleLarge)
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(diceTypes.size) { index ->
            val diceType = diceTypes[index]
            Button(
                onClick = { onDiceTypeSelected(diceType) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (diceType == selectedDiceType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(text = "D$diceType", modifier = Modifier.padding(4.dp))
            }
        }
    }
}


@Composable
fun NumberOfDiceSelector(numOfDice: Int, onNumOfDiceSelected: (Int) -> Unit) {
    Text(text = "Number of Dice:", style = MaterialTheme.typography.titleLarge)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        (1..6).forEach { number ->
            Button(
                onClick = { onNumOfDiceSelected(number) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (number == numOfDice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "$number")
            }
        }
    }
}

@Composable
fun HistoryList(history: List<Int>) {
    Text(text = "History(scroll down to see latest roll):", style = MaterialTheme.typography.titleMedium)
    LazyColumn {
        items(history) { roll ->
            Text(text = "Roll: $roll", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DiceRollerApp()
}
