package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.Entity.Listes;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ListeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void createListes(){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
