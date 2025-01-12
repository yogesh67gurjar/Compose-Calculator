package com.yogesh.composecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(calculatorAction: CalculatorAction) {
        when (calculatorAction) {
            CalculatorAction.Calculate -> performCalculation()
            CalculatorAction.Clear -> state = CalculatorState()
            CalculatorAction.Decimal -> enterDecimal()
            CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Number -> enterNumber(calculatorAction.number)
            is CalculatorAction.Operation -> enterOperation(calculatorAction.calculatorOperation)
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(number1 = state.number1 + number)
            return
        }

        if (state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(number2 = state.number2 + number)
    }

    private fun enterOperation(calculatorOperation: CalculatorOperation) {
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = calculatorOperation)
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> {
                state = state.copy(number2 = state.number2.dropLast(1))
            }

            state.operation != null -> {
                state = state.copy(operation = null)
            }

            state.number1.isNotBlank() -> {
                state = state.copy(number1 = state.number1.dropLast(1))
            }
        }
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.number1.contains(".") && state.number1.isNotBlank()) {
            state = state.copy(number1 = state.number1 + ".")
            return
        }

        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number1 != null && number2 != null) {
            val result = when (state.operation) {
                CalculatorOperation.Add -> {
                    number1 + number2
                }

                CalculatorOperation.Divide -> {
                    number1 / number2
                }

                CalculatorOperation.Multiply -> {
                    number1 * number2
                }

                CalculatorOperation.Subtract -> {
                    number1 - number2
                }

                null -> return
            }

            state = state.copy(
                number1 = result.toString().take(15),
                number2 = "",
                operation = null
            )
        }
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }


}