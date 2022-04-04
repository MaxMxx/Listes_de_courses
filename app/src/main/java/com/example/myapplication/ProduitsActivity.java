package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.Entity.Produits;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ProduitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits_main);

        getProduits();
    }

    public <Client> void getProduits() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}

