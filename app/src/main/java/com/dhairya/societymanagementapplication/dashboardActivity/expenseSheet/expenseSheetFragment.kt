package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class expenseSheetFragment : Fragment(R.layout.fragment_expense_sheet) {

    private val viewModel: noticeViewModel by viewModels()
    private lateinit var binding: FragmentExpenseSheetBinding
    private var expense_data = FirebaseFirestore.getInstance().collection("transactionData")
    private var expenseDataArrayList: MutableList<transactionData> = mutableListOf()
    private lateinit var tableRowAdapter: TableRowAdapter


    var sDate = ""
    var eDate = ""
    val myFormat = "dd/MM/yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val directory =
            File(Environment.getExternalStorageDirectory().toString() + "/Aalishan Committee")
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs()
        }

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
                    sDate = sdf.format(cal.getTime())
                    startDate.text = sdf.format(cal.getTime())
                }

            val dateSetListener1 =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    endDate.textSize = 17F
                    eDate = sdf.format(cal.getTime())
                    endDate.text = sdf.format(cal.getTime())
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
                var newArrayList: MutableList<transactionData> = mutableListOf()

                if (startDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter starting date!!", Toast.LENGTH_SHORT)
                        .show()
                } else if (endDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter ending date!!", Toast.LENGTH_SHORT).show()
                } else {

                    var startingDate: Date = sdf.parse(sDate)
                    var endingDate: Date = sdf.parse(eDate)

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

                        tableRowAdapter =
                            TableRowAdapter(newArrayList as ArrayList<transactionData>)
                        binding.tableRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.tableRecyclerView.adapter = tableRowAdapter


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

            }

            btnBack.setOnClickListener {
                findNavController().navigate(expenseSheetFragmentDirections.actionExpenseSheetFragmentToDashBoardFragment())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                    dialog.setMessage("This permission is required to export data to excel file, if you deny you will not be able to use that feature")
                        .setTitle("Important permission required")
                    dialog.setPositiveButton("Okay",
                        DialogInterface.OnClickListener { dialog, which -> requestPermission() })
                    dialog.setNegativeButton("NO Thanks!",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@MainActivity,
                                "You will not be able to use some features!",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    dialog.show()
                } else {
                    Toast.makeText(
                        this,
                        "Please accept storage permission to use some features!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            UNIQUE_CODE
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.faqs) {
            startActivity(Intent(this@MainActivity, FAQs::class.java))
        } else if (item.getItemId() === R.id.contactDeveloper) {
            startActivity(Intent(this@MainActivity, ContactDeveloper::class.java))
        } else if (item.getItemId() === R.id.createExcel) {
            val filePath = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Aalishan Committee/Data.xls"
            )
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                val hssfWorkbook = HSSFWorkbook()
                val hssfSheet: HSSFSheet = hssfWorkbook.createSheet("Data")
                var hssfRow: HSSFRow = hssfSheet.createRow(0)
                var hssfCell: HSSFCell = hssfRow.createCell(0)
                hssfCell.setCellValue("Flat No")
                hssfCell = hssfRow.createCell(1)
                hssfCell.setCellValue("Owner Name")
                hssfCell = hssfRow.createCell(2)
                hssfCell.setCellValue("Owner Number")
                hssfCell = hssfRow.createCell(3)
                hssfCell.setCellValue("Living in house")
                hssfCell = hssfRow.createCell(4)
                hssfCell.setCellValue("Maintenance")
                hssfSheet.setColumnWidth(0, 15 * 150)
                hssfSheet.setColumnWidth(1, 15 * 200)
                hssfSheet.setColumnWidth(2, 15 * 220)
                hssfSheet.setColumnWidth(3, 15 * 230)
                hssfSheet.setColumnWidth(4, 15 * 210)
                for (i in 0 until ApplicationClass.flats.size()) {
                    var j = 0
                    hssfRow = hssfSheet.createRow(i + 1)
                    hssfCell = hssfRow.createCell(j)
                    j++
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getFlatNo())
                    hssfCell = hssfRow.createCell(j)
                    j++
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerName())
                    hssfCell = hssfRow.createCell(j)
                    j++
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerNumber())
                    hssfCell = hssfRow.createCell(j)
                    j++
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getLivingInHouse())
                    hssfCell = hssfRow.createCell(j)
                    hssfCell.setCellValue(
                        ApplicationClass.flats.get(i).getLastMaintenanceMonth().toString() + " " +
                                ApplicationClass.flats.get(i).getLastMaintenanceYear()
                    )
                }
                try {
                    if (!filePath.exists()) {
                        filePath.createNewFile()
                    }
                    val fileOutputStream: FileOutputStream = FileOutputStream(filePath)
                    hssfWorkbook.write(fileOutputStream)
                    Toast.makeText(
                        this,
                        "File created at Storage/Aalishan Committee/Data.xml",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (fileOutputStream != null) {
                        fileOutputStream.flush()
                        fileOutputStream.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (item.getItemId() === R.id.createExcelFilter) {
            val filePath = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Aalishan Committee/Data with filter.xls"
            )
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                val hssfWorkbook = HSSFWorkbook()
                val hssfSheet: HSSFSheet = hssfWorkbook.createSheet("Data with filter")
                var k = 0
                for (i in 0 until ApplicationClass.flats.size()) {
                    val year1: Int = ApplicationClass.flats.get(i).getLastMaintenanceYear().toInt()
                    val month1: String =
                        ApplicationClass.flats.get(i).getLastMaintenanceMonth().trim()
                    val mon: Int = getMonthIndex(month1)
                    if (year1 <= year && mon <= month) {
                        var hssfRow: HSSFRow = hssfSheet.createRow(0)
                        var hssfCell: HSSFCell = hssfRow.createCell(0)
                        hssfCell.setCellValue("Flat No")
                        hssfCell = hssfRow.createCell(1)
                        hssfCell.setCellValue("Owner Name")
                        hssfCell = hssfRow.createCell(2)
                        hssfCell.setCellValue("Owner Number")
                        hssfCell = hssfRow.createCell(3)
                        hssfCell.setCellValue("Living in house")
                        hssfCell = hssfRow.createCell(4)
                        hssfCell.setCellValue("Maintenance")
                        hssfSheet.setColumnWidth(0, (15 * 150))
                        hssfSheet.setColumnWidth(1, (15 * 200))
                        hssfSheet.setColumnWidth(2, (15 * 220))
                        hssfSheet.setColumnWidth(3, (15 * 230))
                        hssfSheet.setColumnWidth(4, (15 * 210))
                        var j = 0
                        hssfRow = hssfSheet.createRow(k + 1)
                        hssfCell = hssfRow.createCell(j)
                        j++
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getFlatNo())
                        hssfCell = hssfRow.createCell(j)
                        j++
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerName())
                        hssfCell = hssfRow.createCell(j)
                        j++
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerNumber())
                        hssfCell = hssfRow.createCell(j)
                        j++
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getLivingInHouse())
                        hssfCell = hssfRow.createCell(j)
                        hssfCell.setCellValue(
                            (ApplicationClass.flats.get(i).getLastMaintenanceMonth()
                                .toString() + " " +
                                    ApplicationClass.flats.get(i).getLastMaintenanceYear())
                        )
                        k++
                        try {
                            if (!filePath.exists()) {
                                filePath.createNewFile()
                            }
                            val fileOutputStream: FileOutputStream = FileOutputStream(filePath)
                            hssfWorkbook.write(fileOutputStream)
                            Toast.makeText(
                                this,
                                "File created at Storage/Aalishan Committee/Data with filter.xml",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (fileOutputStream != null) {
                                fileOutputStream.flush()
                                fileOutputStream.close()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } else if (item.getItemId() === R.id.societyRules) {
            startActivity(Intent(this@MainActivity, SocietyRules::class.java))
        } else if (item.getItemId() === R.id.logout) {
            showProgress(true)
            tvLoad.setText("Logging you out... Please wait...")
            Backendless.UserService.logout(object : AsyncCallback<Void?>() {
                fun handleResponse(response: Void?) {
                    Toast.makeText(
                        this@MainActivity,
                        "Logged out successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    this@MainActivity.finish()
                }

                fun handleFault(fault: BackendlessFault) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: " + fault.getDetail(),
                        Toast.LENGTH_SHORT
                    ).show()
                    showProgress(false)
                }
            })
        }
        return super.onOptionsItemSelected(item)
    }


}