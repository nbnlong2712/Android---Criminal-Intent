package com.LongTraiDep.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.LongTraiDep.criminalintent.database.CrimeBaseHelper;
import com.LongTraiDep.criminalintent.database.CrimeCursorWrapper;
import com.LongTraiDep.criminalintent.database.CrimeDbSchema;
import com.LongTraiDep.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab==null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase(); //tạo database(nếu chưa tạo)
    }

    public Crime getCrime(UUID mid)       //lấy ra hành vi vi phạm có id = mid
    {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID+" = ?", new String[]{mid.toString()});
        try{
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime)
    {
        File filesDir = mContext.getFilesDir();  //lấy directory sẽ lưu trữ ảnh
        return new File(filesDir, crime.getPhotoFilename());  //lưu trữ ảnh vào directory fileDir, sau đó trả về
    }

    public void addCrime(Crime crime)  //insert một hành vi phạm tội (object) nào đó vào database
    {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }
    private static ContentValues getContentValues(Crime crime) //hàm trả về biến crime, dữ liệu của nó sẽ được ghi ra database (id, title,...)
    {
        ContentValues values = new ContentValues();     //tạo đối tượng
        // dùng để ghi dữ liệu ra database (write)
        values.put(CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }
    public void updateCrime(Crime crime)   //update một hàng nào đó
    {
        String uuidString = crime.getID().toString();
        ContentValues values = getContentValues(crime);
        // các tham số: tên bảng, object chứa thông tin muốn update vào bảng, mệnh đề where trong sql (cập nhật info tại dòng có id = uuid),
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,    //table
                null,       //columns
                whereClause,        //where
                whereArgs,          //whereArgs
                null,      //groupBy
                null,       //having
                null       //orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes(){      //hàm này dễ đọc, đại loại là duyệt qua một đống cursor từ first->last, rồi lưu từng cái cho đến hết vào list
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return crimes;
    }
}