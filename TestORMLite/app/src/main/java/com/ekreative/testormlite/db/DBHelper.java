package com.ekreative.testormlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ekreative.testormlite.entities.TeacherDetails;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by cheb on 4/14/17.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    // Fields

    private static final String DATABASE_NAME = "studentdir.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<TeacherDetails, Integer> teacherDao;
    // Public methods

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {

            // Create Table with given table name with columnName
            TableUtils.createTable(connectionSource, TeacherDetails.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, TeacherDetails.class, true);
            onCreate(db, connectionSource);

        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        }
    }


    public Dao<TeacherDetails, Integer> getTeacherDao() throws SQLException {
        if (teacherDao == null) {
            teacherDao = getDao(TeacherDetails.class);
        }
        return teacherDao;
    }

}
