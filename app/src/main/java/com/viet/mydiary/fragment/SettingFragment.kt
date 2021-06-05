package com.viet.mydiary.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.viet.mydiary.DBOpenHelper
import com.viet.mydiary.Events
import com.viet.mydiary.MainActivity
import com.viet.mydiary.R
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.dialog_create_password.view.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SettingFragment : Fragment() {

    lateinit var alertDialog: AlertDialog
    private lateinit var mainActivity: MainActivity
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dbOpenHelper: DBOpenHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_setting, container, false)

        dbOpenHelper = DBOpenHelper(this.context!!)

        mainActivity = activity as MainActivity

        initPreferences()
        val data = sharedPreferences.getString("DATA","")
        if(data!!.isEmpty()){
            view.btnChangePassword.visibility = View.GONE
            view.btnPassword.visibility = View.VISIBLE
        }
        else{
            view.btnPassword.visibility = View.GONE
            view.btnChangePassword.visibility = View.VISIBLE
        }

        view.btnPassword.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
            builder.setCancelable(true)
            val dialogView: View =
                LayoutInflater.from(this.context).inflate(R.layout.dialog_create_password, null)

            dialogView.btnCreatePW.setOnClickListener {
                if(dialogView.editTextCreatePw.text.toString().isEmpty())
                    Toast.makeText(this.context,"Please enter password", Toast.LENGTH_SHORT).show()
                else{
                    editor.putString("DATA", dialogView.editTextCreatePw.text.toString())
                    editor.commit()
                    view.btnPassword.visibility = View.GONE
                    view.btnChangePassword.visibility = View.VISIBLE
                    alertDialog.cancel()
                }
            }
            builder.setView(dialogView)
            alertDialog = builder.create()
            alertDialog.show()
        }

        view.btnChangePassword.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
            builder.setCancelable(true)
            val dialogView: View =
                LayoutInflater.from(this.context).inflate(R.layout.dialog_change_password, null)

            dialogView.btnChangePW.setOnClickListener {
                val data = sharedPreferences.getString("DATA","")
                if(dialogView.editTextOldPw.text.toString() != data)
                    Toast.makeText(this.context,"wrong old password", Toast.LENGTH_SHORT).show()
                else if(dialogView.editTextChangePw.text.toString().isNotEmpty()){
                    editor.putString("DATA", dialogView.editTextChangePw.text.toString())
                    editor.commit()
                    alertDialog.cancel()
                }else{
                    Toast.makeText(this.context,"Please enter new password", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setView(dialogView)
            alertDialog = builder.create()
            alertDialog.show()
        }

        view.btnCreateBackUp.setOnClickListener {
            exportCSV()
        }

        view.btnBackUp.setOnClickListener {
            importCSV()
        }
        return view
    }

    private fun importCSV() {
        val filePathAndName = "${Environment.getExternalStorageDirectory()}/SQLiteBackup/SQLite_Backup.csv"
        val csvFile = File(filePathAndName)

        if(csvFile.exists()){
            dbOpenHelper.deleteAll()
            try {
                val br = BufferedReader(FileReader(filePathAndName))
                var line: String?
                while (true){
                    line = br.readLine()
                    if(line!=null){
                        val result: MutableList<String> = parseCsvLine(line)
                        val event = Events(result[1], result[2], textToDate(result[3]))
                        dbOpenHelper.saveEvent(event)
                    }else{
                        break
                    }
                }
                Toast.makeText(this.context, "Backup Restored...", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(this.context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this.context, "No backup found...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportCSV() {
        val folder = File("${Environment.getExternalStorageDirectory()}/SQLiteBackup")
        var isFolderCreated = false
        if(!folder.exists())
            isFolderCreated = folder.mkdir()

        Log.e("CSC_TAG", "exportCSV $isFolderCreated")

        val csvFileName = "SQLite_Backup.csv"

        val filePathAndName = "$folder/$csvFileName"

        var eventsList: MutableList<Events> = mutableListOf()
        eventsList.clear()
        eventsList = dbOpenHelper.allEvent()
        try {
            val fw = FileWriter(filePathAndName)
            for (i in 0 until eventsList.size){
                fw.append(""+eventsList[i].id)
                fw.append(",")
                fw.append(""+eventsList[i].title)
                fw.append(",")
                fw.append(""+eventsList[i].event)
                fw.append(",")
                fw.append(""+coverDate(eventsList[i].date))
                fw.append("\n")
            }
            fw.flush()
            fw.close()
            Toast.makeText(this.context, "Backup Exported to: $filePathAndName", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this.context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initPreferences(){
        sharedPreferences = mainActivity.getSharedPreferences("Password", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun coverDate(date: Date): String{
        return SimpleDateFormat("dd/MM/yyyy hh:mm").format(date)
    }

    fun textToDate(text: String): Date{
        return SimpleDateFormat("dd/MM/yyyy hh:mm").parse(text)
    }

    fun parseCsvLine(csvLine: String?): MutableList<String>{
        val result: MutableList<String> = mutableListOf()
        if(csvLine != null){
            val splitData : List<String> = csvLine.split(",")
            for (element in splitData){
                result.add(element)
            }
        }
        return result
    }
}