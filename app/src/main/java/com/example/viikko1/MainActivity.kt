package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.viikko1.domain.filterByDone
import com.example.viikko1.domain.addTask
import com.example.viikko1.domain.toggleDone
import com.example.viikko1.domain.sortByDueDate
import com.example.viikko1.domain.Task
import com.example.viikko1.domain.mockTasks
import com.example.viikko1.ui.theme.Viikko1Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Viikko1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var tasks by remember { mutableStateOf<List<Task>>( mockTasks) }

    var visible by remember { mutableStateOf<List<Task>>(mockTasks)}

    LaunchedEffect(tasks) {
        visible = tasks
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                val nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                val newTask = Task(
                    id = nextId,
                    title = "New task #$nextId",
                    description = "Created from button",
                    priority = 2,
                    dueDate = "2026-01-20",
                    done = false
                )
                tasks = addTask(tasks, newTask)
            }) {
                Text("Add")
            }

            Button(onClick = { visible = sortByDueDate(visible) }) {
                Text("Sort")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = { visible = tasks }) { Text("All") }
            OutlinedButton(onClick = { visible = filterByDone(tasks, done = false) }) { Text("Active") }
            OutlinedButton(onClick = { visible = filterByDone(tasks, done = true) }) { Text("Done") }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            items(visible, key = {it.id} ) { task ->
                TaskRow(task = task,
                    onToggle = { tasks = toggleDone(tasks, task.id)}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Due: ${task.dueDate} | Priority: ${task.priority}")
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(onClick = onToggle) {
            Text(if (task.done) "Undo" else "Done")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Viikko1Theme {
        HomeScreen()
    }
}