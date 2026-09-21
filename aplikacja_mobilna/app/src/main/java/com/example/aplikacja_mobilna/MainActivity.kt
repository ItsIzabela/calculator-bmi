package com.example.aplikacja_mobilna

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aplikacja_mobilna.ui.theme.Aplikacja_mobilnaTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Row

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aplikacja_mobilnaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }

    var weightError by remember { mutableStateOf<String?>(null) }
    var heightError by remember { mutableStateOf<String?>(null) }

    var bmiResultText by remember { mutableStateOf<String?>(null) }
    var bmiCategoryText by remember { mutableStateOf<String?>(null) }

    var historyList by remember { mutableStateOf(listOf<String>()) }
    LaunchedEffect(Unit) {
        historyList = loadHistory(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kalkulator BMI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = weightInput,
            onValueChange = {
                val sanitized = it.replace(',', '.')
                if (sanitized.isEmpty() || sanitized.matches(Regex("^\\d*\\.?\\d*$"))) {
                    weightInput = sanitized
                    weightError = null
                }
            },
            label = { Text("Masa ciała (kg)") },
            isError = weightError != null,
            supportingText = { weightError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = heightInput,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    heightInput = it
                    heightError = null
                }
            },
            label = { Text("Wzrost (cm)") },
            isError = heightError != null,
            supportingText = { heightError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val weight = weightInput.toDoubleOrNull()
                val height = heightInput.toDoubleOrNull()

                if (weight == null || weight !in 1.0..300.0) {
                    weightError = "Błędna waga (1-300)"
                    return@Button
                }
                if (height == null || height !in 1.0..250.0) {
                    heightError = "Błędny wzrost (1-250)"
                    return@Button
                }

                val heightM = height / 100.0
                val bmi = weight / (heightM * heightM)
                val category = when {
                    bmi < 18.5 -> "Niedowaga"
                    bmi < 25.0 -> "Waga prawidłowa"
                    bmi < 30.0 -> "Nadwaga"
                    else -> "Otyłość"
                }

                bmiResultText = "BMI: %.2f".format(Locale.US, bmi)
                bmiCategoryText = category
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Oblicz BMI")
        }

        bmiResultText?.let { result ->
            Card(
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = result, style = MaterialTheme.typography.titleLarge)
                    Text(text = bmiCategoryText ?: "", color = MaterialTheme.colorScheme.primary)

                    Button(
                        onClick = {
                            saveBmiResultToFile(context, result, bmiCategoryText ?: "")
                            historyList = loadHistory(context)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Zapisz do historii")
                    }
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Historia pomiarów:", fontWeight = FontWeight.Bold)
            TextButton(onClick = {
                clearHistory(context)
                historyList = emptyList()
            }) {
                Text("Wyczyść")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(historyList.reversed()) { record ->
                Text(
                    text = record,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

private fun saveBmiResultToFile(context: Context, bmi: String, category: String) {
    try {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val record = "$timestamp | $bmi | $category\n"
        context.openFileOutput("historia_bmi.txt", Context.MODE_APPEND).use {
            it.write(record.toByteArray())
        }
        Toast.makeText(context, "Zapisano!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun loadHistory(context: Context): List<String> {
    val file = File(context.filesDir, "historia_bmi.txt")
    return if (file.exists()) file.readLines() else emptyList()
}

private fun clearHistory(context: Context) {
    val file = File(context.filesDir, "historia_bmi.txt")
    if (file.exists()) file.delete()
}
