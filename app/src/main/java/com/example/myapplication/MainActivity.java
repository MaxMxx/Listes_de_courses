package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Entity.Produits;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TableLayout tablayoutSearch;
    private com.google.android.material.tabs.TabItem Produits;
    private com.google.android.material.tabs.TabItem Recettes;
    private com.google.android.material.tabs.TabItem Listes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tablayoutSearch = findViewById(R.id.tablayoutSearch);

        this.deleteDatabase("listeDeCourses.db");

        createData();

        getMenuSwitch();

        getProduits();
    }

    public void getMenuSwitch() {

        Produits = findViewById(R.id.Produits);
        Recettes = findViewById(R.id.Recettes);
        Listes = findViewById(R.id.Listes);

        Produits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: A cliqué sur Produits");
            }
        });

        Recettes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: A cliqué sur Recettes");
            }
        });

        Listes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: A cliqué sur Listes");
            }
        });

    }

    public void createData() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );

            Produits produits = new Produits();
            produits.setLibelle("Frommage");
            produits.setQuantite(5);
            produits.setPrix(12);
            daoProduits.create(produits);

            Produits produits2 = new Produits();
            produits.setLibelle("Patate");
            produits.setQuantite(4);
            produits.setPrix(12);
            daoProduits.create(produits2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduits() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            List<Produits> produits = daoProduits.queryForAll();


            for (Produits produit: produits) {
                TableRow tableRowProduit = new TableRow(this);
                //tableRowProduit.setBackgroundColor(Color.RED);



                TextView value = new TextView(this);
                value.setText(produit.getLibelle()+" | "+produit.getPrix()+"€ | "+produit.getQuantite());
                value.setTextSize(20);

                int idProduit = produit.getIdProduit();

                CheckBox checkBox = new CheckBox(this);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProduitCheck(idProduit);
                    }
                });

                tableRowProduit.addView(checkBox);
                tableRowProduit.addView(value);

                tablayoutSearch.addView(tableRowProduit);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addProduitCheck(int idProduit) {
        /*DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            Produits produit = daoProduits.queryForId(idProduit);
            if (produit != null) {
                daoProduits.delete(produit);
                produitsTableLayout.removeView(tableRowProduit);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();*/
    }

}