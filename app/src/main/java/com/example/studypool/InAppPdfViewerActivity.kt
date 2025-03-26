package com.example.studypool

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.studypool.utils.XPManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class InAppPdfViewerActivity : AppCompatActivity() {


    private lateinit var pdfImageView: ImageView
    private lateinit var nextBtn: Button
    private lateinit var prevBtn: Button
    private var currentPage: PdfRenderer.Page? = null
    private var renderer: PdfRenderer? = null
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PDF_VIEWER", "Activity started") // ← MUST show in log
        setContentView(R.layout.activity_in_app_pdf_viewer)

        pdfImageView = findViewById(R.id.pdfImageView)
        nextBtn = findViewById(R.id.btnNext)
        prevBtn = findViewById(R.id.btnPrev)

        val fileUri = intent.getStringExtra("fileUri") ?: return
        val uri = Uri.parse(fileUri)

        openPdfFromUri(uri)

        nextBtn.setOnClickListener {
            showPage(currentIndex + 1)
        }

        prevBtn.setOnClickListener {
            showPage(currentIndex - 1)
        }

        XPManager(this).addXP(5)
    }

    private fun openPdfFromUri(uri: Uri) {
        try {
            val input: InputStream? = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "temp.pdf")
            val output = FileOutputStream(file)
            input?.copyTo(output)
            input?.close()
            output.close()

            val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(descriptor)
            showPage(0)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open PDF: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun showPage(index: Int) {
        renderer?.let { pdfRenderer ->
            if (index < 0 || index >= pdfRenderer.pageCount) return
            currentPage?.close()
            currentPage = pdfRenderer.openPage(index)
            val bitmap = Bitmap.createBitmap(
                currentPage!!.width,
                currentPage!!.height,
                Bitmap.Config.ARGB_8888
            )
            currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfImageView.setImageBitmap(bitmap)
            currentIndex = index
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage?.close()
        renderer?.close()
    }
}
