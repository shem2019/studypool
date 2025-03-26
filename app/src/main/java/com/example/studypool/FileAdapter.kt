package com.example.studypool

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class FileAdapter(private val context: Context, private val fileList: List<String>, private val onFileClick: ((String) -> Unit)? = null) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileIcon: ImageView = view.findViewById(R.id.fileIcon)
        val fileName: TextView = view.findViewById(R.id.fileName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileUri = fileList[position]
        val fileName = getFileName(context, Uri.parse(fileUri))  // ✅ Extract only file name

        holder.fileName.text = fileName  // ✅ Show only file name, not full URI

        holder.itemView.setOnClickListener {
            val fileUri = fileList[position]
            onFileClick?.invoke(fileUri)
            Log.d("FILE_CLICK", "Clicked: $fileUri")
        }



        // Set file icon based on type
        when {
            fileName.endsWith(".pdf") -> holder.fileIcon.setImageResource(R.drawable.pdf)
            fileName.endsWith(".doc") || fileName.endsWith(".docx") -> holder.fileIcon.setImageResource(R.drawable.word)
            fileName.endsWith(".ppt") || fileName.endsWith(".pptx") -> holder.fileIcon.setImageResource(R.drawable.ppt)
            else -> holder.fileIcon.setImageResource(R.drawable.folder)
        }
    }

    override fun getItemCount(): Int = fileList.size

    private fun getFileName(context: Context, uri: Uri): String {
        var name: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name ?: uri.path?.substringAfterLast("/") ?: "Unknown File"
    }
}
