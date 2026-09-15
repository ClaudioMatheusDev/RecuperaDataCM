package com.example.recuperadatacm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recuperadatacm.ui.theme.RecuperaDataCMTheme
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecuperaDataCMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecuperaDiaSemanaScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RecuperaDiaSemanaScreen(modifier: Modifier = Modifier) {
    val diasDaSemana = stringArrayResource(R.array.dias_da_semana)
    var dataDigitada by rememberSaveable { mutableStateOf("") }
    var resultado by rememberSaveable { mutableStateOf("") }
    val formatoData = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).apply {
            isLenient = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Recuperar dia da semana",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = dataDigitada,
            onValueChange = { dataDigitada = it },
            label = { Text("Data") },
            placeholder = { Text("dd/mm/aaaa") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                resultado = recuperarDiaDaSemana(
                    data = dataDigitada,
                    diasDaSemana = diasDaSemana,
                    formatoData = formatoData
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar")
        }

        if (resultado.isNotBlank()) {
            Text(
                text = resultado,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun recuperarDiaDaSemana(
    data: String,
    diasDaSemana: Array<String>,
    formatoData: SimpleDateFormat
): String {
    return try {
        val dataConvertida = formatoData.parse(data.trim())
        val calendario = Calendar.getInstance().apply {
            time = dataConvertida!!
        }
        val indiceDiaSemana = calendario.get(Calendar.DAY_OF_WEEK) - 1
        "Dia da semana: ${diasDaSemana[indiceDiaSemana]}"
    } catch (_: ParseException) {
        "Data invalida. Use o formato dd/mm/aaaa."
    }
}

@Preview(showBackground = true)
@Composable
fun RecuperaDiaSemanaPreview() {
    RecuperaDataCMTheme {
        RecuperaDiaSemanaScreen()
    }
}
