package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.Listes;
import com.example.myapplication.Entity.Produits;
import com.example.myapplication.Entity.Recettes;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<Produits> listeProduitsChoice = new ArrayList<Produits>();

    private EditText editTextSearch;

    private TableLayout tablayoutSearch;
    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tablayoutSearch = findViewById(R.id.tablayoutSearch);
        getSupportActionBar().hide();

        // A UTILISER QUE POUR FAIRE UN RESET DE LA BDD AVEC QUELQUE PRODUIT

        /*

        this.deleteDatabase("listeDeCourses.db");
        createData();

        */

        getSearch();

        getMenuSwitch();
    }

    public void getSearch() {
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
                    tablayoutSearch.removeAllViews();
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

    public void getMenuSwitch() {

        /*
        buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(monIntent);
            }
        });
        */

        buttonProduits = findViewById(R.id.buttonProduits);
        buttonProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this, ProduitsActivity.class);
                startActivity(monIntent);
            }
        });

        buttonRecettes = findViewById(R.id.buttonRecettes);
        buttonRecettes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this, RecettesActivity.class);
                startActivity(monIntent);
            }
        });

        buttonListe = findViewById(R.id.buttonListe);
        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this, ListeActivity.class);
                startActivity(monIntent);
            }
        });

    }

    public void createData() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );

            Produits produits = new Produits();
            produits.setLibelle("Fromage");
            produits.setQuantite(5);
            daoProduits.create(produits);

            Produits produits2 = new Produits();
            produits2.setLibelle("Patate");
            produits2.setQuantite(4);
            daoProduits.create(produits2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduits(Produits produit) {
        TableRow tableRowProduit = new TableRow(this);
        //tableRowProduit.setBackgroundColor(Color.RED);

        TextView value = new TextView(this);
        value.setText(produit.getLibelle()+" | "+produit.getQuantite());
        value.setTextSize(20);

        int idProduit = produit.getIdProduit();

        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = checkBox.isChecked();
                if (checked){
                    addProduitCheck(idProduit);
                }
                else{
                    removeProduitCheck(idProduit);
                }

            }
        });

        tableRowProduit.addView(checkBox);
        tableRowProduit.addView(value);

        tablayoutSearch.addView(tableRowProduit);
    }

    public void addProduitCheck(int idProduit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);
            Dao<Listes, Integer> daoListes = linker.getDao(Listes.class);
            Dao<Recettes, Integer> daoRecettes = linker.getDao(Recettes.class);
            /*
            Produits produit = daoProduits.queryForId(idProduit);
            if (produit != null) {
                listeProduitsChoice.add(produit);

                // SPINNER CADIE = BOOL ISCART

                Listes listes = new Listes();
                listes.setProduit(produit);
                daoListes.create(listes);

            }
            */


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeProduitCheck(int idProduit) {

    }

}