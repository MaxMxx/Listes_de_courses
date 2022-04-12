package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.Produits;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class RecettesActivity extends AppCompatActivity {

    private static final String TAG = "RecettesActivity";
    private TableLayout tableLayoutRecettes;
    private TableLayout tableLayoutProduits;
    private EditText searchBar;

    private EditText editTextSearch;

    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    private Button buttonCreateRecette;
    private TextView textViewSelectRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recettes_main);
        tableLayoutRecettes = findViewById(R.id.tableLayoutRecettes);
        getSupportActionBar().hide();

        getProduitBySearch();
    }

    public void getRecettes() {

    }

    public void getProduits(Produits produit) {
        TableRow tableRowProduit = new TableRow(this);

        TextView value = new TextView(this);
        value.setText(produit.getLibelle()+" | "+produit.getQuantite());
        value.setTextSize(20);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduitRecette(produit);
            }
        });

        tableRowProduit.addView(checkBox);
        tableRowProduit.addView(value);

        tableLayoutProduits.addView(tableRowProduit);
    }

    public void addProduitRecette(Produits produit){

    }

    public void getProduitBySearch() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );

            List<Produits> produits = daoProduits.queryForAll();

            for (Produits produit: produits) {
                getProduits(produit);
            }

            editTextSearch = findViewById(R.id.editTextSearch);

            editTextSearch.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getTableLayoutProduits.removeAllViews();
                    if(s != "") {
                        for (Produits produit: produits) {
                            if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                                getProduits(produit);
                            }
                        }
                    } else {
                        for (Produits produit: produits) {
                            getProduits(produit);
                        }
                    }
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}