package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.calculator.OneRMDate

@Dao
interface OneRMDateDao {

    @Query("SELECT * FROM caldateTable GROUP BY date")
    fun getAllDate() : LiveData<List<OneRMDate>>
    /*
    * private String[] getDate() {
        SimpleDateFormat old_sdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat new_sdf = new SimpleDateFormat("mm/dd");

        Cursor c = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " group by "+ CalDateContract.Entry.COLUMN_DATE, null);
        String[] dateArray = new String[c.getCount()];
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            int i = 0;
            do {
                String date = c.getString(c.getColumnIndex(CalContract.Entry.COLUMN_DATE));
                try {
                    Date original_date = old_sdf.parse(date);
                    dateArray[i] =  new_sdf.format(original_date);
                }catch (ParseException e){e.printStackTrace();}
                i++;
            } while (c.moveToNext());
            xLabelCount=i-1;
        }
        c.close();
        return dateArray;
    }
    * */

    @Query("SELECT * FROM caldateTable WHERE date like :date")
    fun getDate(date: String) : OneRMDate?

    @Insert
    fun insert(oneRMDate: OneRMDate) : Long
}