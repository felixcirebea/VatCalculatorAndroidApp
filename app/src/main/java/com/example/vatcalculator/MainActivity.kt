package com.example.vatcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vatcalculator.ui.theme.VatCalculatorTheme
import com.example.vatcalculator.util.calculateTotalBill
import com.example.vatcalculator.util.calculateVat
import com.example.vatcalculator.widget.RoundButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VatCalculatorApp()
        }
    }
}

@Composable
fun VatCalculatorApp() {

    val totalPriceState = remember {
        mutableStateOf(0.0)
    }

    val inputValueState = remember {
        mutableStateOf("")
    }

    val validState = remember(inputValueState.value) {
        inputValueState.value.trim().isNotEmpty()
    }

    val vatPercentageState = remember {
        mutableStateOf(0)
    }

    VatCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.height(150.dp)) {
                Header(totalPriceState)
                Content(
                    inputValueState = inputValueState,
                    validState = validState,
                    vatPercentageState = vatPercentageState,
                    totalPriceState = totalPriceState
                )
            }
        }
    }
}

@Composable
fun Header(totalPrice: MutableState<Double>) {

    val formattedTotal = "%.2f".format(totalPrice.value)

    Surface(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF94CAF1)
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {

            Text(
                text = "TOTAL",
                modifier = Modifier.padding(2.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif
                )
            )

            Text(
                text = "$${formattedTotal}",
                modifier = Modifier.padding(2.dp),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Content(
    inputValueState: MutableState<String>,
    validState: Boolean,
    vatPercentageState: MutableState<Int>,
    totalPriceState: MutableState<Double>
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 10.dp,
        border = BorderStroke(2.dp, Color(0xFFAFA7A7))
    ) {

        Column(horizontalAlignment = Alignment.Start) {

            InputField(inputValueState, validState, keyboardController)

            AmountRow(inputValue = inputValueState.value)

            VatAmountRow(
                amount = inputValueState.value,
                vatPercentage = vatPercentageState.value,
                validState = validState,
                totalPriceState = totalPriceState
            )

            Text(
                text = "${vatPercentageState.value}%",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp)
                    .align(CenterHorizontally),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif
                )
            )

            ButtonsRow(vatPercentageState = vatPercentageState)

            PercentageRow()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun InputField(
    inputValueState: MutableState<String>,
    validState: Boolean,
    keyboardController: SoftwareKeyboardController?
) {
    OutlinedTextField(
        value = inputValueState.value,
        onValueChange = { newValue ->
            inputValueState.value = newValue
        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = true,
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        ),
        label = {
            Text(text = "Enter amount")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AttachMoney,
                contentDescription = "DollarIcon"
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions {
            if (!validState) {
                return@KeyboardActions
            }
            keyboardController?.hide()
        },
        singleLine = true,
        shape = RectangleShape
    )
}

@Composable
private fun AmountRow(inputValue: String) {
    Row(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 5.dp
        ),
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = "Amount",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.width(187.dp))

        Text(
            text = "$$inputValue",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}

@Composable
private fun VatAmountRow(
    amount: String,
    vatPercentage: Int,
    validState: Boolean,
    totalPriceState: MutableState<Double>
) {
    Row(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 5.dp
        ),
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = "VAT Amount",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.width(150.dp))

        //Classic Java version
//        var calculatedVat = 0.0
//        if (validState) {
//            calculatedVat = calculateVat(
//                amount = amount.toDouble(),
//                vatPercentage = vatPercentage
//            )
//        }

        //Kotlin version
        val calculatedVat = if (validState) {
            calculateVat(amount = amount.toDouble(), vatPercentage = vatPercentage)
        } else {
            0.0
        }

        Text(
            text = "$$calculatedVat",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        totalPriceState.value = if (validState) {
            calculateTotalBill(vatAmount = calculatedVat, amount = amount.toDouble())
        } else {
            0.0
        }
    }

}


@Composable
private fun ButtonsRow(vatPercentageState: MutableState<Int>) {
    Row(
        modifier = Modifier.padding(top = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.width(27.dp))

        RoundButton(
            onClickEvent = { vatPercentageState.value = 0 },
            text = "A",
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            ),
            size = 45,
        )

        Spacer(modifier = Modifier.width(50.dp))

        RoundButton(
            onClickEvent = { vatPercentageState.value = 5 },
            text = "B",
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            ),
            size = 45,
        )

        Spacer(modifier = Modifier.width(50.dp))

        RoundButton(
            onClickEvent = { vatPercentageState.value = 10 },
            text = "C",
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            ),
            size = 45,
        )

        Spacer(modifier = Modifier.width(50.dp))

        RoundButton(
            onClickEvent = { vatPercentageState.value = 20 },
            text = "D",
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            ),
            size = 45,
        )
    }
}

@Composable
private fun PercentageRow() {
    Row(
        modifier = Modifier.padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.width(42.dp))

        Text(
            text = "0%",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.width(76.dp))

        Text(
            text = "5%",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.width(72.dp))

        Text(
            text = "10%",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.width(68.dp))

        Text(
            text = "20%",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}


