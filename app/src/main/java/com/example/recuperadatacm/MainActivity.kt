package com.example.recuperadatacm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recuperadatacm.ui.theme.RecuperaDataCMTheme
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val AppBackground = Color(0xFFF6F7F1)
private val PanelBackground = Color(0xFFFFFFFF)
private val PrimaryText = Color(0xFF17201B)
private val SecondaryText = Color(0xFF5E6A63)
private val Accent = Color(0xFF0E766E)
private val FieldBorder = Color(0xFFD7DDD6)
private val SuccessBackground = Color(0xFFEAF7EF)
private val SuccessText = Color(0xFF155E35)
private val ErrorBackground = Color(0xFFFFECE8)
private val ErrorText = Color(0xFF9F2F1C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecuperaDataCMTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = AppBackground
                ) { innerPadding ->
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
    val resultadoEhErro = resultado.startsWith("Data")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = PanelBackground,
            shadowElevation = 8.dp,
            tonalElevation = 1.dp,
            border = BorderStroke(1.dp, Color(0xFFE7EBE4))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Recuperar dia da semana",
                        color = PrimaryText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Digite uma data e descubra em qual dia da semana ela cai.",
                        color = SecondaryText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                OutlinedTextField(
                    value = dataDigitada,
                    onValueChange = { dataDigitada = it },
                    label = { Text("Data") },
                    placeholder = { Text("dd/mm/aaaa") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = Accent,
                        cursorColor = Accent
                    ),
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
                    enabled = dataDigitada.isNotBlank(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFC9D4D1),
                        disabledContentColor = Color(0xFF6B7773)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 0.dp
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Recuperar",
                        modifier = Modifier.padding(vertical = 6.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (resultado.isNotBlank()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = if (resultadoEhErro) ErrorBackground else SuccessBackground,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (resultadoEhErro) Color(0xFFFFC7BC) else Color(0xFFCBEBD5)
                        )
                    ) {
                        Text(
                            text = resultado,
                            color = if (resultadoEhErro) ErrorText else SuccessText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
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
        "Data inválida. Use o formato dd/mm/aaaa."
    }
}

@Preview(showBackground = true)
@Composable
fun RecuperaDiaSemanaPreview() {
    RecuperaDataCMTheme {
        RecuperaDiaSemanaScreen()
    }
}