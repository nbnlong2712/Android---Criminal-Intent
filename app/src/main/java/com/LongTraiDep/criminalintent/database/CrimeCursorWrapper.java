package com.LongTraiDep.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.LongTraiDep.criminalintent.Crime;
import com.LongTraiDep.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Crime getCrime()       // Hàm dùng để đọc dữ liệu từ database (Read)
    {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString((getColumnIndex(CrimeTable.Cols.SUSPECT)));

        Crime crime = new Crime(UUID.fromString(uuidString));     //chuyển uuidstring về lại uuid
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(solved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
