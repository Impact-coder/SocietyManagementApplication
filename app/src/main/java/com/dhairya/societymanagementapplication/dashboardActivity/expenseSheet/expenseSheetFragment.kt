package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.TableRowAdapter
import com.dhairya.societymanagementapplication.dashboardActivity.notice.noticeViewModel
import com.dhairya.societymanagementapplication.data.transactionData
import com.dhairya.societymanagementapplication.databinding.FragmentExpenseSheetBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class expenseSheetFragment : Fragment(R.layout.fragment_expense_sheet) {

    private val viewModel: noticeViewModel by viewModels()
    private lateinit var binding: FragmentExpenseSheetBinding
    private var expense_data = FirebaseFirestore.getInstance().collection("transactionData")
    private var expenseDataArrayList: MutableList<transactionData> = mutableListOf()
    private lateinit var tableRowAdapter: TableRowAdapter
    private var newArrayList: MutableList<transactionData> = mutableListOf()
    private var fileName = ""

    var sDate = ""
    var eDate = ""
    val myFormat = "dd/MM/yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        var cal = Calendar.getInstance()

        binding = FragmentExpenseSheetBinding.bind(view)
        binding.apply {

//            startDate.setText(viewModel.startDate)
//            endDate.setText(viewModel.endDate)
//
//            startDate.addTextChangedListener {
//                viewModel.startDate = it.toString()
//            }
//
//            endDate.addTextChangedListener {
//                viewModel.endDate = it.toString()
//            }

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    startDate.textSize = 17F
                    sDate = sdf.format(cal.time)
                    startDate.text = sdf.format(cal.time)
                }

            val dateSetListener1 =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    endDate.textSize = 17F
                    eDate = sdf.format(cal.time)
                    endDate.text = sdf.format(cal.time)
                }

            startDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            endDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener1,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            btnView.setOnClickListener {
                newArrayList = mutableListOf()

                if (startDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter starting date!!", Toast.LENGTH_SHORT)
                        .show()
                } else if (endDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter ending date!!", Toast.LENGTH_SHORT).show()
                } else {

                    var startingDate: Date = sdf.parse(sDate)
                    var endingDate: Date = sdf.parse(eDate)

                    fileName = "${sDate}-${eDate}.xls"

                    if (startingDate.before(endingDate) || startingDate.equals(endingDate)) {

                        CoroutineScope(Dispatchers.IO).launch {
                            expenseDataArrayList = expense_data.get().await()
                                .toObjects(transactionData::class.java) as MutableList<transactionData>
                        }

                        for (i in expenseDataArrayList) {
                            var dat: String = i.date
                            var d = sdf.parse(dat)

                            if (d.before(endingDate) && d.after(startingDate)) {
                                newArrayList.add(i)
                            }
                        }

                        if( newArrayList.size != 0)
                        {
                            tableRowAdapter =
                                TableRowAdapter(newArrayList as ArrayList<transactionData>)
                            binding.tableRecyclerView.layoutManager = LinearLayoutManager(context)
                            binding.tableRecyclerView.adapter = tableRowAdapter
                        }
                        else{
                            emptyMessage.text = "There is no data available between " + sDate + " and " + eDate +"!!"
                        }




                    } else {
                        Toast.makeText(
                            context,
                            "Please enter valid ending date!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            btnDownload.setOnClickListener {
                createExcelFile()
            }

            btnBack.setOnClickListener {
                findNavController().navigate(expenseSheetFragmentDirections.actionExpenseSheetFragmentToDashBoardFragment())
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createExcelFile() {
        if (newArrayList.isEmpty()) {
            Toast.makeText(requireActivity(), "There is no data available between " + sDate + " and " + eDate +"!!", Toast.LENGTH_SHORT).show()
            return
        }
        val hssfWorkbook = HSSFWorkbook()
        val hssfSheet: HSSFSheet = hssfWorkbook.createSheet("Data")
        var hssfRow: HSSFRow = hssfSheet.createRow(0)
        var hssfCell: HSSFCell = hssfRow.createCell(0)
        hssfCell.setCellValue("Date")
        hssfCell = hssfRow.createCell(1)
        hssfCell.setCellValue("Particular")
        hssfCell = hssfRow.createCell(2)
        hssfCell.setCellValue("Amount")
        hssfSheet.setColumnWidth(0, 15 * 250)
        hssfSheet.setColumnWidth(1, 15 * 500)
        hssfSheet.setColumnWidth(2, 15 * 250)
        for (i in 0 until newArrayList.size) {
            var j = 0
            hssfRow = hssfSheet.createRow(i + 1)
            hssfCell = hssfRow.createCell(j)
            j++
            hssfCell.setCellValue(newArrayList[i].date)
            hssfCell = hssfRow.createCell(j)
            j++
            hssfCell.setCellValue(newArrayList[i].particular)
            hssfCell = hssfRow.createCell(j)
            hssfCell.setCellValue(newArrayList[i].amount)
        }
        try {

            val externalUri: Uri =
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            val relativeLocation = Environment.DIRECTORY_DOCUMENTS
            val contentValues = ContentValues()

            contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/text");
            contentValues.put(MediaStore.Files.FileColumns.TITLE, "Test");
            contentValues.put(
                MediaStore.Files.FileColumns.DATE_ADDED,
                System.currentTimeMillis() / 1000
            )
            contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativeLocation);
            contentValues.put(
                MediaStore.Files.FileColumns.DATE_TAKEN,
                System.currentTimeMillis()
            )

            val fileUri: Uri =
                requireActivity().contentResolver.insert(externalUri, contentValues)!!
            try {
                val outputStream: OutputStream =
                    requireActivity().contentResolver.openOutputStream(fileUri)!!
                hssfWorkbook.write(outputStream)
                Toast.makeText(requireActivity(), "Downloaded succesfully", Toast.LENGTH_SHORT)
                    .show()
                outputStream.close()
            } catch (e: IOException) {
                Log.d("TAG_EXCEL", "createExcelFile: $e")
                e.printStackTrace()
            }


        } catch (e: Exception) {
            Log.d("TAG_EXCEL", "createExcelFile: $e")
            e.printStackTrace()
        }
    }



}