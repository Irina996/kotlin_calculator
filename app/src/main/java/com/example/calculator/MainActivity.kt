package com.example.calculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigInteger
import kotlin.math.PI
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // tap on numbers
        zeroBtn.setOnClickListener{setTextFields("0")}
        oneBtn.setOnClickListener{setTextFields("1")}
        twoBtn.setOnClickListener{setTextFields("2")}
        threeBtn.setOnClickListener{setTextFields("3")}
        fourBtn.setOnClickListener{setTextFields("4")}
        fiveBtn.setOnClickListener{setTextFields("5")}
        sixBtn.setOnClickListener{setTextFields("6")}
        sevenBtn.setOnClickListener{setTextFields("7")}
        eightBtn.setOnClickListener{setTextFields("8")}
        nineBtn.setOnClickListener{setTextFields("9")}

        // tap on simple math operators
        plusBtn.setOnClickListener{ setSimpleOperator('+') }
        minusBtn.setOnClickListener{ setSimpleOperator('-') }
        mulBtn.setOnClickListener{ setSimpleOperator('*') }
        divBtn.setOnClickListener{ setSimpleOperator('/') }

        commaBtn.setOnClickListener{
            val str = calcOperations.text.toString()
            if (str.isNotEmpty()) {
                if (str[str.length - 1] != '.' && str.indexOf('.') != -1) {
                    val indexOfComma = str.indexOf('.')
                    val listOfInd = listOf(
                        str.lastIndexOf('+'),
                        str.lastIndexOf('-'),
                        str.lastIndexOf('*'),
                        str.lastIndexOf('/')
                    )
                    var indexOfStart = listOfInd.maxOrNull() ?: 0
                    if (indexOfStart == -1)
                        indexOfStart = 0
                    if (indexOfStart > indexOfComma) {
                        if (str.isNotEmpty() && str[str.length - 1] != '(' &&
                            isNotOperation(str[str.length - 1])
                        ) {
                            setTextFields(".")
                        }
                    }
                } else if (str[str.length - 1] != '.' && str[str.length - 1] != '(' &&
                    isNotOperation(str[str.length - 1])
                ) {
                    setTextFields(".")
                }
            }
        }

        percentBtn.setOnClickListener{
            var str = calcOperations.text.toString()

            try {
                if (str != "") {
                    val opStr = "+-*/"
                    for (char in opStr) {


                        if (char in str)
                            str = str.substring(str.lastIndexOf(char) + 1, str.length)
                    }
                    val lastNumber = str.toDouble() / 100
                    var startOfStr = calcOperations.text.toString()
                    startOfStr = startOfStr.substring(0, startOfStr.length - str.length)
                    calcOperations.text = startOfStr.plus("$lastNumber")
                }
            } catch(e: Exception) {
                Log.d("Error", "Message: ${e.message}")
            }
        }

        expBtn.setOnClickListener { setTextFields("e") }
        piBtn.setOnClickListener { setTextFields("pi") }
        hyperbolaBtn.setOnClickListener { setTextFields("1/") }
        factorialBtn.setOnClickListener {
            val str = calcOperations.text.toString()
            if ( str.isNotEmpty() && str[str.length - 1] != '(' &&
                isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.') {
                setTextFields("!")
            }
        }
        sqrtBtn.setOnClickListener { setTextFields("√") }
        squareBtn.setOnClickListener {
            val str = calcOperations.text.toString()
            if ( str.isNotEmpty() && str[str.length - 1] != ')' &&
                isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.') {
                setTextFields("^2")
            }
        }
        exponentiationBtn.setOnClickListener {
            val str = calcOperations.text.toString()
            if ( str.isNotEmpty() && str[str.length - 1] != '(' &&
                isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.') {
                setTextFields("^")
            }
        }
        modBtn.setOnClickListener {
            val str = calcOperations.text.toString()
            if ( str.isNotEmpty() && str[str.length - 1] != '(' &&
                isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.') {
                setSimpleOperator('%')
            }
        }
        logBtn.setOnClickListener { setTextFields("log10(") }
        lnBtn.setOnClickListener { setTextFields("log(") }

        bracket1Btn.setOnClickListener {
            val str = calcOperations.text.toString()
            try {
                if (str.isNotEmpty() && str[str.length - 1] != ')') {
                    setTextFields("(")
                } else {
                    if (str.isEmpty())
                        setTextFields("(")
                }
            } catch (e: Exception) {
                Log.d("Error", "Message: ${e.message}")
            }
        }
        bracket2Btn.setOnClickListener {
            val str = calcOperations.text.toString()
            if (str.isNotEmpty() && str[str.length - 1] != '(' &&
                isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.') {
                setTextFields(")")
            }
        }

        sinBtn.setOnClickListener { setTextFields("sin(") }
        cosBtn.setOnClickListener { setTextFields("cos(") }
        tanBtn.setOnClickListener { setTextFields("tan(") }
        angleUnitBtn.setOnClickListener {
            if (angleUnitBtn.text.toString() == "rad")
                angleUnitBtn.text = getString(R.string.deg)
            else
                angleUnitBtn.text = getString(R.string.rad)
        }

        acBtn.setOnClickListener{
            calcOperations.text = ""
            calcResult.text = ""
        }
        backBtn.setOnClickListener{
            val str = calcOperations.text.toString()
            if(str.isNotEmpty()) {
                when (str[str.length - 1]) {
                    'n' -> calcOperations.text = str.substring(0, str.length - 3)
                    's' -> calcOperations.text = str.substring(0, str.length - 3)
                    'g' -> calcOperations.text = str.substring(0, str.length - 3)
                    'i' -> calcOperations.text = str.substring(0, str.length - 2)
                    else -> calcOperations.text = str.substring(0, str.length - 1)
                }
            }
            calcResult.text = ""
        }
        equalBtn.setOnClickListener{
            try {
                var str = calcOperations.text.toString()

                // add bracket ')' in the end
                var bracketCount = getBracketCount(str)
                while (bracketCount > 0)
                {
                    str = str.plus(')')
                    bracketCount -= 1
                }
                calcOperations.text = str

                // calculate factorials
                str = parseFactorialExpr()
                if (str == "error")
                    throw Exception("Trying to calculate factorial from negative number")

                // calculate √x
                str = calcSqrt(str)

                if (angleUnitBtn.text == "deg")
                    str = degToRad(str)

                val ex = ExpressionBuilder(str).build()
                val res = ex.evaluate()

                val longRes = res.toLong()
                if ( res == longRes.toDouble())
                    calcResult.text = longRes.toString()
                else
                     calcResult.text  = res.toString()
            } catch(e: Exception){
                Log.d("Error", "Message: ${e.message}")
                calcResult.text = getString(R.string.error)
            }
        }
    }

    private fun setTextFields(str: String) {
        if (calcResult.text != "") {
            if (calcResult.text == "Error") {
                calcOperations.text = ""
                calcResult.text = ""
            }
            else {
                calcOperations.text = calcResult.text.toString().plus("+")
                calcResult.text = ""
            }
        }

        // adding '*'
        val op = calcOperations.text.toString()
        if (op.isNotEmpty() && isDigit(op[op.length - 1].toString()) &&
            !isDigit(str) && (isNotOperation(str[0]) || str[0] == '√')
            && str [0]!= ')' && str[0] != '.')
            calcOperations.append("*")
        if (op.isNotEmpty() && isDigit(str) && !isDigit(op[op.length - 1].toString()) &&
            (isNotOperation(op[op.length - 1]) || op[op.length - 1] == '!') &&
            op[op.length - 1] != '(' && op[op.length - 1] != '.')
            calcOperations.append("*")
        calcOperations.append(str)
    }

    private fun isNotOperation(ch: Char): Boolean {
        return when(ch) {
            '!' -> false
            '^' -> false
            '/' -> false
            '-' -> false
            '+' -> false
            '*' -> false
            '%' -> false
            '√' -> false
            else -> true
        }
    }

    private fun isDigit(str: String): Boolean {
        return when (str) {
            "0"-> true
            "1"-> true
            "2"-> true
            "3"-> true
            "4"-> true
            "5"-> true
            "6"-> true
            "7"-> true
            "8"-> true
            "9"-> true
            else-> false
        }
    }

    private fun setSimpleOperator(op: Char) {
        try {
            if (calcResult.text != "") {
                if (calcResult.text == "Error") {
                    calcOperations.text = ""
                    calcResult.text = ""
                } else {
                    calcOperations.text = calcResult.text
                    calcResult.text = ""
                }
            }

            if (calcOperations.text == "")
                return

            val str = calcOperations.text.toString()
            if (str[str.length - 1] == op)
                return
            if (isNotOperation(str[str.length - 1]) && str[str.length - 1] != '.')
                if (op == '-' || str[str.length - 1] != '(')
                    calcOperations.text = str.plus(op)
        } catch (e: Exception) {
            Log.d("Error", "Message: ${e.message}")
        }
    }

    private fun parseFactorialExpr(): String {
        var str = calcOperations.text.toString()
        var lastIndex = str.indexOf('!')
        while (lastIndex != -1 )
        {
            var factStr: String
            var firstIndex = 0
            factStr = str.substring(0, lastIndex)
            if (factStr[factStr.length - 1] == ')') {
                try {
                    firstIndex = factStr.lastIndexOf('(')
                    factStr = factStr.substring(firstIndex, factStr.length)
                    val ex = ExpressionBuilder(factStr).build()
                    val res = ex.evaluate()

                    val longRes = res.toLong()
                    factStr = if (res == longRes.toDouble())
                        longRes.toString()
                    else
                        res.toString()
                    if (firstIndex != 0)
                        firstIndex -= 1
                } catch(e: Exception){
                    Log.d("Error", "Message: ${e.message}")
                    val error = "Error"
                    calcResult.text = error
                }
            }
            else {
                val listOfInd = listOf(factStr.lastIndexOf('+'),
                    factStr.lastIndexOf('-'),
                    factStr.lastIndexOf('*'),
                    factStr.lastIndexOf('/'))
                firstIndex = listOfInd.maxOrNull() ?: 0
                if (firstIndex == -1)
                    firstIndex = 0
                factStr = factStr.substring(firstIndex, factStr.length)
            }
            val firstPart = if (firstIndex != 0)
                str.substring(0, firstIndex + 1)
            else
                ""
            val lastPart = if (lastIndex != str.length - 1)
                str.substring(lastIndex + 1, str.length)
            else
                str.substring(lastIndex, str.length - 1)
            val num = factStr.toInt()
            if (num < 0) {
                return "error"
            }
            try {
                factStr = calcFactorial(num).toString()
            } catch (e: Exception) {
                Log.d("Error", "Message: ${e.message}")
                return "error"
            }
            str = firstPart.plus(factStr).plus(lastPart)
            lastIndex = str.indexOf('!')
        }
        return str
    }

    // calculate factorials
    private fun calcFactorial(number: Int): BigInteger {
        when (number) {
            0 -> return BigInteger.ONE
            1 -> return BigInteger.ONE
        }
        return calcFactorial(number - 1).multiply(BigInteger.valueOf(number.toLong()))
    }

    // calculate √x
    private fun calcSqrt(str: String): String {
        var sqrtStr = str
        while ( sqrtStr.lastIndexOf('√') != -1) {
            val firstIndex = sqrtStr.lastIndexOf('√')
            var lastIndex: Int
            var num: Double
            if (sqrtStr[firstIndex + 1] == '(')
            {
                var sqrtExpr = sqrtStr.substring(firstIndex + 1, sqrtStr.length)
                lastIndex = sqrtExpr.indexOf(')') + 1
                sqrtExpr = sqrtExpr.substring(0, lastIndex)
                val ex = ExpressionBuilder(sqrtExpr).build()
                num = ex.evaluate().pow(0.5)
                lastIndex += firstIndex + 1
            }
            else {
                val listOfInd = listOf(
                    sqrtStr.lastIndexOf('+'),
                    sqrtStr.lastIndexOf('-'),
                    sqrtStr.lastIndexOf('*'),
                    sqrtStr.lastIndexOf('/')
                )
                lastIndex = listOfInd.maxOrNull() ?: 0
                if (lastIndex == -1 || lastIndex < firstIndex)
                    lastIndex = sqrtStr.length
                num = sqrtStr.substring(firstIndex + 1, lastIndex).toDouble().pow(0.5)
            }
            val firstPart = if( firstIndex != 0)
                sqrtStr.substring(0, firstIndex)
            else
                ""
            val lastPart = if (lastIndex != sqrtStr.length)
                sqrtStr.substring(lastIndex, sqrtStr.length)
            else
                ""
            sqrtStr = firstPart.plus(num.toString()).plus(lastPart)
        }
        return sqrtStr
    }

    private fun degToRad(str: String): String {
        var newStr = str
        var firstIndex = newStr.indexOf('n')
        if (firstIndex == -1)
            firstIndex = newStr.indexOf('s')
        while (firstIndex != -1) {

            var lastIndex: Int
            var num: Double
            if (newStr[firstIndex + 1] == '(')
            {
                // expression in brackets
                var expr = newStr.substring(firstIndex + 1, newStr.length)
                lastIndex = expr.indexOf(')') + 1
                expr = expr.substring(0, lastIndex)
                var ex = ExpressionBuilder(expr).build()
                num = ex.evaluate() * PI / 180
                expr = "${newStr.substring(firstIndex - 2, firstIndex + 1)}$num"
                ex =  ExpressionBuilder(expr).build()
                num = ex.evaluate()
                lastIndex += firstIndex + 1
            }
            else {
                val listOfInd = listOf(
                    newStr.lastIndexOf('+'),
                    newStr.lastIndexOf('-'),
                    newStr.lastIndexOf('*'),
                    newStr.lastIndexOf('/')
                )
                lastIndex = listOfInd.maxOrNull() ?: 0
                if (lastIndex == -1 || lastIndex < firstIndex)
                    lastIndex = newStr.length
                num = newStr.substring(firstIndex + 1, lastIndex).toDouble() * PI / 180
                val expr = "${newStr.substring(firstIndex - 2, firstIndex + 1)}$num"
                val ex =  ExpressionBuilder(expr).build()
                num = ex.evaluate()
            }
            firstIndex -= 2
            val firstPart = if( firstIndex != 0)
                newStr.substring(0, firstIndex)
            else
                ""
            val lastPart = if (lastIndex != newStr.length)
                newStr.substring(lastIndex, newStr.length)
            else
                ""
            newStr = firstPart.plus(num.toString()).plus(lastPart)

            firstIndex = newStr.indexOf('n')
            if (firstIndex == -1)
                firstIndex = newStr.indexOf('s')
        }
        return newStr
    }

    private fun getBracketCount(str: String): Int {
        var bracketCount = 0
        for (ch in str) {
            if (ch == '(')
                bracketCount += 1
            if (ch == ')')
                bracketCount -= 1
        }
        return bracketCount
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        calcOperations.text = savedInstanceState.getString("operations")
        calcResult.text = savedInstanceState.getString("result")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("operations", calcOperations.text.toString())
        outState.putString("result", calcResult.text.toString())
    }
}