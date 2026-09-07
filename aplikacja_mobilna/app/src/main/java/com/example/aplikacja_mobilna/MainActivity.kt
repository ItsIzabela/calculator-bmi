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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    // stan pól tekstowych
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }

    // stan walidacji i błędów
    var weightError by remember { mutableStateOf<String?>(null) }
    var heightError by remember { mutableStateOf<String?>(null) }

    // stan wyniku obliczeń BMI oraz interpretacji
    var bmiResultText by remember { mutableStateOf<String?>(null) }
    var bmiCategoryText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kalkulator BMI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // wprowadzania wagi
        OutlinedTextField(
            value = weightInput,
            onValueChange = { newValue ->
                val sanitized = newValue.replace(',', '.')
                if (sanitized.isEmpty() || sanitized.matches(Regex("^\\d*\\.?\\d*$"))) {
                    weightInput = sanitized
                    weightError = null // Wyczyszczenie błędu po zmianie tekstu
                }
            },
            label = { Text("Masa ciała (kg)") },
            isError = weightError != null,
            supportingText = {
                weightError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // pole wprowadzenia wzrostu
        OutlinedTextField(
            value = heightInput,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    heightInput = newValue
                    heightError = null // Wyczyszczenie błędu po zmianie tekstu
                }
            },
            label = { Text("Wzrost (cm)") },
            isError = heightError != null,
            supportingText = {
                heightError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // przycisk obliczajacy
        Button(
            onClick = {
                // walidacja danych wejściowych
                val weightNum = weightInput.toDoubleOrNull()
                val heightNum = heightInput.toDoubleOrNull()

                var isValid = true

                if (weightNum == null || weightNum <= 0 || weightNum > 300) {
                    weightError = "Wprowadź poprawną wagę (1-300 kg)"
                    isValid = false
                }

                if (heightNum == null || heightNum <= 0 || heightNum > 250) {
                    heightError = "Wprowadź poprawny wzrost (1-250 cm)"
                    isValid = false
                }

                // jesli walidacja jest prawidlowa
                if (isValid && weightNum != null && heightNum != null) {
                    val heightInMeters = heightNum / 100.0
                    val bmi = weightNum / (heightInMeters * heightInMeters)

                    // interpretacja wyniku
                    val category = when {
                        bmi < 18.5 -> "Niedowaga"
                        bmi < 25.0 -> "Waga prawidłowa"
                        bmi < 30.0 -> "Nadwaga"
                        else -> "Otyłość"
                    }

                    bmiResultText = "Twój wynik BMI: %.2f".format(Locale.US, bmi)
                    bmiCategoryText = "Interpretacja: $category"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Oblicz BMI")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // sekcja wyswietlania i zapisu
        bmiResultText?.let { result ->
            Text(
                text = result,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            bmiCategoryText?.let { category ->
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // przycisk zapisujacy wynik
            Button(
                onClick = {
                    saveBmiResultToFile(context, result, bmiCategoryText ?: "")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz wynik do pliku")
            }
        }
    }
}

/**
 * funkcja zapisujaca dane do pliku
 */
private fun saveBmiResultToFile(context: Context, bmiText: String, categoryText: String) {
    try {
        val fileName = "historia_bmi.txt"
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val record = "$timestamp | $bmiText | $categoryText\n"

        // zapis do pliku
        val file = File(context.filesDir, fileName)
        file.appendText(record)

        Toast.makeText(context, "Zapisano do pliku: ${file.name}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Błąd podczas zapisu do pliku", Toast.LENGTH_SHORT).show()
    }
}