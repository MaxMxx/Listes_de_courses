package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableLayout;

import com.example.myapplication.Entity.Produits;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class RecettesActivity extends AppCompatActivity {

    private static final String TAG = "RecettesActivity";
    private TableLayout tableLayoutRecettes;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recettes_main);
        tableLayoutRecettes = findViewById(R.id.tableLayoutRecettes);

        getProduitBySearch();
    }

    public void getProduitBySearch() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            List<Produits> produits = daoProduits.queryForAll();

            //Faire une recherche à chaque changement dans la searchbar

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}