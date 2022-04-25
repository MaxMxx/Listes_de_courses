package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.ContenirRecettes;
import com.example.myapplication.Entity.Listes;
import com.example.myapplication.Entity.ListesProduits;
import com.example.myapplication.Entity.ListesRecettes;
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

    private TableLayout tablayoutSearchProduits;
    private TableLayout tablayoutSearchRecettes;

    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tablayoutSearchProduits = findViewById(R.id.tablayoutSearchProduits);
        tablayoutSearchRecettes = findViewById(R.id.tablayoutSearchRecettes);
        getSupportActionBar().hide();

        //deleteDatabase("listeDeCourses.db");

        getSearch();

        getMenuSwitch();
    }

    public boolean verifInListeProduit(Produits produit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );

            List<Listes> listListes = daoListes.queryForAll();
            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();

            for(Listes listes : listListes) {
                for (ListesProduits listesProduits : listListesProduits) {
                    if(listesProduits.getListe() != null) {
                        if (listes.getIdListe() == listesProduits.getListe().getIdListe()) {
                            if (listesProduits.getProduit().getIdProduit() == produit.getIdProduit()) {
                                return false;
                            }
                        }
                    } else {
                        if (listesProduits.getProduit().getIdProduit() == produit.getIdProduit()) {
                            return false;
                        }
                    }
                }
            }

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
        return true;
    }

    public boolean verifInListeRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );

            List<Listes> listListes = daoListes.queryForAll();
            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();

            for(Listes listes : listListes) {
                for(ListesRecettes listesRecettes : listListesRecettes) {
                    if(listesRecettes.getListe() != null) {
                        if(listes.getIdListe() == listesRecettes.getListe().getIdListe()) {
                            if(listesRecettes.getRecette().getIdRecette() == recette.getIdRecette()) {
                                return false;
                            }
                        }
                    } else {
                        if(listesRecettes.getRecette().getIdRecette() == recette.getIdRecette()) {
                            return false;
                        }
                    }
                }
            }

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
        return true;
    }

    public void getSearch() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Produits> listProduits = daoProduits.queryForAll();
            List<Recettes> listRecettes = daoRecettes.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            for (Produits produit: listProduits) {
                getProduits(produit);
            }

            for (Recettes recette: listRecettes) {
                getRecette(recette, listContenirRecettes);
            }

            editTextSearch = findViewById(R.id.editTextSearch);

            editTextSearch.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tablayoutSearchProduits.removeAllViews();
                    tablayoutSearchRecettes.removeAllViews();
                    if(s != "") {
                        for (Produits produit: listProduits) {
                            if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                                getProduits(produit);
                            }
                        }
                        for (Recettes recette: listRecettes) {
                            if((recette.getLibelleRecette().toLowerCase()).contains(s.toString().toLowerCase())) {
                                getRecette(recette, listContenirRecettes);
                            }
                        }
                    } else {
                        for (Produits produit: listProduits) {
                            getProduits(produit);
                        }
                        for (Recettes recette: listRecettes) {
                            getRecette(recette, listContenirRecettes);
                        }
                    }
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getRecette(Recettes recette, List<ContenirRecettes> listContenirRecettes) {
        if(verifInListeRecette(recette)) {
            LinearLayout linearLayoutRecette = new LinearLayout(this);

            EditText quantite = new EditText(this);
            quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = checkBox.isChecked();
                    if (checked){
                        int qtn;
                        if(quantite.getText().toString().equals("0") || quantite.getText().toString().equals("")) {
                            qtn = 1;
                        } else {
                            qtn = Integer.parseInt(quantite.getText().toString());
                        }
                        addRecetteCheck(recette.getIdRecette(), qtn);
                    }
                }
            });

            TextView libelle = new TextView(this);
            libelle.setText(recette.getLibelleRecette());
            libelle.setTextSize(20);

            Button buttonInfo = new Button(this);
            buttonInfo.setText("Details");
            buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    infoRecettes(recette, listContenirRecettes);
                }
            });

            linearLayoutRecette.addView(checkBox);
            linearLayoutRecette.addView(libelle);
            linearLayoutRecette.addView(quantite);
            linearLayoutRecette.addView(buttonInfo);

            tablayoutSearchRecettes.addView(linearLayoutRecette);
        }
    }

    public void infoRecettes(Recettes recette, List<ContenirRecettes> listContenirRecettes) {
        AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
        infoRecettePopup.setTitle("Information de la recette "+recette.getLibelleRecette()+" :");

        TableLayout tableLayout = new TableLayout(this);

        for(ContenirRecettes contenirRecette : listContenirRecettes) {
            if(contenirRecette.getRecette().getIdRecette() == recette.getIdRecette()) {
                Produits produit = contenirRecette.getProduit();

                LinearLayout linearLayoutProduit = new LinearLayout(this);

                TextView libelle = new TextView(this);
                libelle.setText("Produit: " + produit.getLibelle() + " | Quantite: " + contenirRecette.getQuantite());
                linearLayoutProduit.addView(libelle);

                tableLayout.addView(linearLayoutProduit);
            }
        }

        infoRecettePopup.setView(tableLayout);
        infoRecettePopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Retour");
            }
        });

        infoRecettePopup.show();
    }

    public void getProduits(Produits produit) {
        if(verifInListeProduit(produit)) {
            TableRow tableRowProduit = new TableRow(this);

            TextView value = new TextView(this);
            value.setText(produit.getLibelle());
            value.setTextSize(20);

            EditText quantite = new EditText(this);
            quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = checkBox.isChecked();
                    if (checked){
                        int qtn;
                        if(quantite.getText().toString().equals("0") || quantite.getText().toString().equals("")) {
                            qtn = 1;
                        } else {
                            qtn = Integer.parseInt(quantite.getText().toString());
                        }
                        addProduitCheck(produit.getIdProduit(), qtn);
                    }
                }
            });

            tableRowProduit.addView(checkBox);
            tableRowProduit.addView(value);
            tableRowProduit.addView(quantite);

            tablayoutSearchProduits.addView(tableRowProduit);
        }
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

    public void addProduitCheck(int idProduit, int quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);
            Dao<Listes, Integer> daoListes = linker.getDao(Listes.class);
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao(ListesProduits.class);

            Listes listeCreate = new Listes();
            listeCreate.setQuantite(quantite);
            listeCreate.setCart(false);
            daoListes.create(listeCreate);

            List<Listes> listListes = daoListes.queryForAll();

            Listes liste = listListes.get(listListes.size()-1);
            Produits produit = daoProduits.queryForId(idProduit);

            ListesProduits listesProduits = new ListesProduits();
            listesProduits.setProduit(produit);
            listesProduits.setListe(liste);
            daoListesProduits.create(listesProduits);

            reloadAllPage();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addRecetteCheck(int idRecette, int quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao(Recettes.class);
            Dao<Listes, Integer> daoListes = linker.getDao(Listes.class);
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao(ListesRecettes.class);

            Listes listeCreate = new Listes();
            listeCreate.setQuantite(quantite);
            listeCreate.setCart(false);
            daoListes.create(listeCreate);

            List<Listes> listListes = daoListes.queryForAll();

            Recettes recette = daoRecettes.queryForId(idRecette);
            Listes liste = listListes.get(listListes.size()-1);

            ListesRecettes listesRecettes = new ListesRecettes();
            listesRecettes.setRecette(recette);
            listesRecettes.setListe(liste);
            daoListesRecettes.create(listesRecettes);

            reloadAllPage();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void reloadAllPage() {
        tablayoutSearchProduits.removeAllViews();
        tablayoutSearchRecettes.removeAllViews();

        getSearch();
    }

}