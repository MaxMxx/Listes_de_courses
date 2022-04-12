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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.Produits;
import com.example.myapplication.Entity.Recettes;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.List;


public class RecettesActivity extends AppCompatActivity {

    private static final String TAG = "RecettesActivity";
    private int selectRecette;

    private LinearLayout tableLayoutRecettes;
    private LinearLayout tableLayoutProduits;
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
        tableLayoutProduits = findViewById(R.id.tableLayoutProduits);
        getSupportActionBar().hide();

        getProduitBySearch();

        getRecettes();
    }

    public void addRecette() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );

            Produits test = daoProduits.queryForId(1);

            Recettes recette = new Recettes();
            recette.setIdLaRecette(1);
            recette.setProduit(test);
            daoRecettes.create(recette);

            List<Recettes> recettes = daoRecettes.queryForAll();

            Recettes recetteRecup = daoRecettes.queryForId(recettes.get(recettes.size()-1).getIdRecette());

            Log.i(TAG, "addRecette: "+recetteRecup.getIdRecette());

            if (recetteRecup != null) {
                Log.i(TAG, "addRecette: IN THE IF");
                recetteRecup.setIdLaRecette(recetteRecup.getIdRecette());
                daoRecettes.update(recetteRecup);

                selectRecette = recetteRecup.getIdRecette();
                tableLayoutRecettes.removeAllViews();
                getRecettes();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getRecettes() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );

            List<Recettes> listRecette = daoRecettes.queryForAll();

            Log.i(TAG, "TAILLE DE LA LISTE: "+listRecette.size());

            buttonCreateRecette = findViewById(R.id.buttonCreateRecette);
            buttonCreateRecette.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRecette();
                }
            });

            if(listRecette.size() != 0) {

                // SELECTION DE LA PREMIERE RECETTE POUR RECETTES SELECTIONNE
                selectRecette = listRecette.get(0).getIdRecette();

                Spinner spinnerRecette = new Spinner(this);

                for (Recettes recette: listRecette) {
                    TableRow tableRecette = new TableRow(this);

                    Log.i(TAG, "GeTrEcEtTe "+recette.getIdRecette());

                    /*TextView libelleRecette = new TextView(this);
                    libelleRecette.setText(recette.getIdRecette()+"");
                    tableRecette.addView(libelleRecette);

                    spinnerRecette.addView(tableRecette);
                    tableLayoutRecettes.addView(spinnerRecette);*/
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduits(Produits produit) {
        TableRow tableRowProduit = new TableRow(this);

        Log.i(TAG, "GeTpRoDuItS "+produit.getIdProduit()+" : "+produit.getLibelle());

        TextView value = new TextView(this);
        value.setText(produit.getLibelle()+" | "+produit.getQuantite());
        //value.setTextSize(20);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduitRecette(produit);
            }
        });
        
        tableRowProduit.addView(value);
        tableRowProduit.addView(checkBox);

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
                    tableLayoutProduits.removeAllViews();
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