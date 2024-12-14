package com.example.tictactoe // Replace with your package name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.isNotEmpty
import kotlin.collections.toMutableList

data class TicTacToeState(
    val board: List<String> = List(9) { "" },
    val currentPlayer: String = "X",
    val winner: String? = null
) {
    fun restart() = TicTacToeState() // Add this function
}

fun makeMove(state: TicTacToeState, index: Int): TicTacToeState {
    if (state.board[index].isNotEmpty() || state.winner != null) {
        return state // Invalid move
    }

    val newBoard = state.board.toMutableList()
    newBoard[index] = state.currentPlayer

    val newWinner = calculateWinner(newBoard)

    return state.copy(
        board = newBoard,
        currentPlayer = if (state.currentPlayer == "X") "O" else "X",
        winner = newWinner
    )
}

fun calculateWinner(board: List<String>): String? {
    val winningCombinations = kotlin.collections.listOf(
        kotlin.collections.listOf(0, 1, 2), kotlin.collections.listOf(3, 4, 5),
        kotlin.collections.listOf(6, 7, 8), // Rows
        kotlin.collections.listOf(0, 3, 6), kotlin.collections.listOf(1, 4, 7),
        kotlin.collections.listOf(2, 5, 8), // Columns
        kotlin.collections.listOf(0, 4, 8), kotlin.collections.listOf(2, 4, 6) // Diagonals
    )

    for (combination in winningCombinations) {
        val (a, b, c) = combination
        if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
            return board[a]
        }
    }

    return null
}


@Composable
fun TicTacToeBoard(state: TicTacToeState, onMove: (Int) -> Unit) {
    var currentState by remember { mutableStateOf(state) } //
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0..2) {
            androidx.compose.foundation.layout.Row {
                for (j in 0..2) {
                    val index = i * 3 + j
                    TicTacToeCell(
                        value = state.board[index],
                        onMove = { onMove(index) }
                    )
                }
            }
        }

        if (state.winner != null) {
            Text("Winner: ${state.winner}", fontSize = 24.sp)
        } else {
            Text("Current Player: ${state.currentPlayer}", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add some space

        Button(onClick = { currentState = currentState.restart() }) { // Add this button
            Text("Restart Game")
        }
    }
}


@Composable
fun TicTacToeCell(value: String, onMove: () -> Unit) {
    Button(onClick = onMove, modifier = androidx.compose.ui.Modifier
        .size(100.dp)
        .padding(8.dp)) {
        Text(text = value, fontSize = 30.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        var state by remember { mutableStateOf(TicTacToeState()) }

        TicTacToeBoard(state = state) { index ->
            state = makeMove(state, index)
        }
    }
}