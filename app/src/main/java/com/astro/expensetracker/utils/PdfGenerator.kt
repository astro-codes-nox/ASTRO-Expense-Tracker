package com.astro.expensetracker.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import com.astro.expensetracker.data.Transaction

object PdfGenerator {

    fun createPdf(
        context: Context,
        income: Double,
        expense: Double,
        balance: Double,
        transactions: List<Transaction>
    ): File {

        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 24f
        paint.isFakeBoldText = true

        canvas.drawText(
            "ASTRO Expense Report",
            40f,
            60f,
            paint
        )

        paint.textSize = 18f
        paint.isFakeBoldText = false

        canvas.drawText(
            "Total Income : ৳$income",
            40f,
            130f,
            paint
        )

        canvas.drawText(
            "Total Expense : ৳$expense",
            40f,
            170f,
            paint
        )

        canvas.drawText(
            "Balance : ৳$balance",
            40f,
            210f,
            paint
        )

        var y = 280f

        paint.textSize = 20f
        paint.isFakeBoldText = true

        canvas.drawText(
            "Transactions",
            40f,
            y,
            paint
        )

        y += 40f

        paint.textSize = 16f
        paint.isFakeBoldText = false

        transactions.forEach {

            canvas.drawText(
                "${it.date} | ${it.title}",
                40f,
                y,
                paint
            )

            y += 25f

            canvas.drawText(
                "${it.type} : ৳${it.amount}",
                60f,
                y,
                paint
            )

            y += 35f
        }

        document.finishPage(page)

        val file = File(
            context.getExternalFilesDir(null),
            "Expense_Report.pdf"
        )

        document.writeTo(file.outputStream())
        document.close()

        return file
    }
}