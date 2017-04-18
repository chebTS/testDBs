package com.ekreative.testormlite.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ekreative.testormlite.R;
import com.ekreative.testormlite.db.DBHelper;
import com.ekreative.testormlite.entities.TeacherDetails;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    DBHelper dbHelper;
    ArrayAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_read).setOnClickListener(this);
        list = (ListView) findViewById(R.id.list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                TeacherDetails teacherDetails = new TeacherDetails();
                teacherDetails.teacherName = "Antoha";
                teacherDetails.address = "Lenna 44";

                try {
                    final Dao<TeacherDetails, Integer> techerDao = dbHelper.getTeacherDao();
                    techerDao.create(teacherDetails);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_read:
                try {
                    final Dao<TeacherDetails, Integer> techerDao = dbHelper.getTeacherDao();
                    List<TeacherDetails> teacherDetails1 = techerDao.queryForAll();
                    Log.i(TAG, "Teachers count :" + teacherDetails1.size());
                    for (int i = 0; i < teacherDetails1.size(); i++) {
                        Log.i(TAG, teacherDetails1.get(i).teacherName);
                    }
                    adapter = new TeacherAdapter(MainActivity.this, R.layout.item_list, R.id.txt_id, teacherDetails1);
                    list.setAdapter(adapter);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
        super.onDestroy();
    }

    private class TeacherAdapter extends ArrayAdapter<TeacherDetails> {

        public TeacherAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<TeacherDetails> objects) {
            super(context, resource, textViewResourceId, objects);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TeacherDetails teacher = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, null);
            }
            ((TextView)convertView.findViewById(R.id.txt_id)).setText(String.valueOf(teacher.teacherId));
            ((TextView)convertView.findViewById(R.id.txt_title)).setText(teacher.teacherName);
            ((TextView)convertView.findViewById(R.id.txt_subtitle)).setText(teacher.address);
            return convertView;

        }
    }
}
