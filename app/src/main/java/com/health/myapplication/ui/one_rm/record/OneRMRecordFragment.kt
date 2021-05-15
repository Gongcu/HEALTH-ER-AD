package com.health.myapplication.ui.one_rm.record

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentOneRmChartBinding
import com.health.myapplication.databinding.FragmentOneRmRecordBinding
import com.health.myapplication.db.DbHelper_Calculator
import com.health.myapplication.db.DbHelper_Calculator_sub
import com.health.myapplication.dialog.CalculatorDialog
import com.health.myapplication.listener.CalculatorDialogListener
import com.health.myapplication.model.calculator.CalContract
import com.health.myapplication.model.calculator.CalDateContract
import com.health.myapplication.ui.one_rm.OneRMViewModel
import kotlinx.android.synthetic.main.fragment_one_rm_record.*
import java.util.*

class OneRMRecordFragment : Fragment() {
    private var mDb: SQLiteDatabase? = null
    private var nDb: SQLiteDatabase? = null
    private var dbHelper_note: DbHelper_Calculator_sub? = null
    private var dbHelper_date: DbHelper_Calculator? = null
    private var dialog: CalculatorDialog? = null
    private var adapter: CalculatorAdapter? = null
    private var date_list: ArrayList<String?>? = null

    private val viewModel : OneRMViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOneRmRecordBinding>(layoutInflater,R.layout.fragment_one_rm_record,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        /*
        date_list = ArrayList()
        dbHelper_date = DbHelper_Calculator(activity)
        mDb = dbHelper_date!!.writableDatabase
        dbHelper_note = DbHelper_Calculator_sub(activity)
        nDb = dbHelper_note!!.writableDatabase*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adView.loadAd(AdRequest.Builder().build())
        /*
        recyclerView.setHasFixedSize(true)
        setList()
        adapter = CalculatorAdapter(activity, allWeight, date_list)
        recyclerView.adapter = adapter
        AddButton.setOnClickListener(View.OnClickListener {
            dialog = CalculatorDialog(requireActivity())
            dialog!!.setDialogListener(object : CalculatorDialogListener {
                // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                override fun onPositiveClicked() {}
                override fun onPositiveClicked(date: String, name: String, one_rm: Double) {
                    addData(date, name, one_rm)
                }

                override fun onNegativeClicked() {}
            })
            dialog!!.show()
        })
*/
    }

    fun update() {
        if (adapter != null) {
            adapter = null
            date_list!!.clear()
            setList()
            adapter = CalculatorAdapter(activity, allWeight, date_list)
            recyclerView!!.adapter = adapter
        }
    }

    private fun setList() {
        val cursor = mDb!!.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " group by " + CalDateContract.Entry.COLUMN_DATE, null)
        if (cursor != null && cursor.count != 0) {
            cursor.moveToFirst()
            do {
                date_list!!.add(cursor.getString(cursor.getColumnIndex(CalDateContract.Entry.COLUMN_DATE)))
            } while (cursor.moveToNext())
        }
        Collections.sort<String>(date_list)
        Collections.reverse(date_list)
        cursor!!.close()
    }

    fun addData(date: String, name: String, one_rm: Double) {
        val cv = ContentValues()
        val c2 = mDb!!.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " where " + CalDateContract.Entry.COLUMN_DATE + "='" + date + "'", null)

        /*같은 날이 있을경우 note만 추가*/if (c2.count > 0) {
            c2.moveToFirst()
            val id = c2.getLong(c2.getColumnIndex(CalDateContract.Entry._ID))
            c2.close()
            val check = nDb!!.rawQuery("select * from " + CalContract.Entry.TABLE_NAME + " where " + CalContract.Entry.COLUMN_DATE + "='" + date + "' and " +
                    CalContract.Entry.COLUMN_EXERCISE + "='" + name + "'", null)
            if (check.count > 0) {
                Toast.makeText(activity, "이미 같은 운동이 존재 합니다.", Toast.LENGTH_LONG).show()
                return
            }
            check.close()
            cv.put(CalContract.Entry.COLUMN_DATE, date)
            cv.put(CalContract.Entry.COLUMN_EXERCISE, name)
            cv.put(CalContract.Entry.COLUMN_ONERM, one_rm)
            cv.put(CalContract.Entry.COLUMN_KEY, id)
            nDb!!.insert(CalContract.Entry.TABLE_NAME, null, cv)
        } else {
            if (!date_list!!.contains(date)) date_list!!.add(0, date)
            cv.put(CalDateContract.Entry.COLUMN_DATE, date)
            mDb!!.insert(CalDateContract.Entry.TABLE_NAME, null, cv)
            cv.clear()
            val c3 = mDb!!.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME, null)
            if (c3.count > 0) {
                c3.moveToLast()
                val recentId = c3.getLong(c3.getColumnIndex(CalDateContract.Entry._ID)) //가장 최신에 삽입된 날짜의 ID를 얻어옴
                cv.put(CalContract.Entry.COLUMN_DATE, date)
                cv.put(CalContract.Entry.COLUMN_EXERCISE, name)
                cv.put(CalContract.Entry.COLUMN_ONERM, one_rm)
                cv.put(CalContract.Entry.COLUMN_KEY, recentId)
                nDb!!.insert(CalContract.Entry.TABLE_NAME, null, cv)
            }
        }
        adapter = null
        adapter = CalculatorAdapter(activity, allWeight, date_list)
        recyclerView!!.adapter = adapter
    }

    // 두번째 파라미터 (Projection array)는 여러 열들 중에서 출력하고 싶은 것만 선택해서 출력할 수 있게 한다.
    // 모든 열을 출력하고 싶을 때는 null 을 입력한다.
    private val allWeight: Cursor
        private get() =// 두번째 파라미터 (Projection array)는 여러 열들 중에서 출력하고 싶은 것만 선택해서 출력할 수 있게 한다.
                // 모든 열을 출력하고 싶을 때는 null 을 입력한다.
            nDb!!.query(
                    CalContract.Entry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )

    companion object{
        private var INSTANCE : OneRMRecordFragment? = null

        fun getInstance(): OneRMRecordFragment {
            if(INSTANCE ==null)
                INSTANCE = OneRMRecordFragment()
            return INSTANCE!!
        }
    }
}