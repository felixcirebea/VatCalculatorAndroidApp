package com.example.vatcalculator.util

fun calculateVat(amount: Double, vatPercentage: Int): Double {
    return (vatPercentage * amount) / 100
}

fun calculateTotalBill(vatAmount: Double, amount: Double): Double {
    return vatAmount + amount
}