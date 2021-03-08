package com.qcit.location.selector.libary.utils

import android.content.Context
import com.qcit.location.selector.libary.models.City
import jxl.Sheet
import jxl.Workbook
import java.io.File


object ExcelUtils {
    var citys: MutableList<City> = mutableListOf()
    fun readCitys(context: Context) :MutableList<City>{
        try {
            var file=context.resources.assets.open("city_list.xls")
            val wb = Workbook.getWorkbook(file)
            var sheet = wb.getSheet(0)
            citys.clear()
            for (row in 0..sheet.rows)
                    citys.add(
                        City(
                            sheet.getCell(0, row).contents,
                            sheet.getCell(1, row).contents,
                            sheet.getCell(2, row).contents
                        )
                    )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return citys
    }
}