package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.myapplication.Entity.ContenirRecettes;
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
    private EditText searchBar;

    private EditText editTextSearch;

    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    private Button buttonCreateRecette;

    private LinearLayout tableLayoutProduits;
    private TextView textViewSelectRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recettes_main);
        tableLayoutRecettes = findViewById(R.id.tableLayoutRecettes);
        tableLayoutProduits = findViewById(R.id.tableLayoutProduits);
        getSupportActionBar().hide();

        textViewSelectRecette = findViewById(R.id.textViewSelectRecette);

        getRecettes();

        createRecette();

        getMenuSwitch();
    }

    public void createRecette() {
        buttonCreateRecette = findViewById(R.id.buttonCreateRecette);
        buttonCreateRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecette();
            }
        });
    }

    public void addRecette() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            AlertDialog.Builder editPopup = new AlertDialog.Builder(this);
            editPopup.setTitle("Voulez-vous créer une recette ?");

            //TableLayout CREATE

            TableLayout tableLayout = new TableLayout(this);

            // CREATION LIBELLE RECETTE

            TableRow tableRowLibelle = new TableRow(this);

            TextView infoLibelle = new TextView(this);
            infoLibelle.setText("Libelle: ");
            tableRowLibelle.addView(infoLibelle);

            EditText createLibelle = new EditText(this);
            tableRowLibelle.addView(createLibelle);

            tableLayout.addView(tableRowLibelle);

            // ADD VIEW IN POPUP EDIT

            editPopup.setView(tableLayout);

            //deletePopup.setMessage("Cliquez sur oui ou non");
            editPopup.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { ;
                    try {
                        //createLibelle.getText().toString()

                        Recettes recette = new Recettes();
                        recette.setLibelleRecette(createLibelle.getText().toString());
                        daoRecettes.create(recette);

                        List<Recettes> listRecettes = daoRecettes.queryForAll();

                        Log.i(TAG, "TAUILLE DE LA LISTE: "+listRecettes.size());

                        for (Recettes selectRecette : listRecettes) {
                            Log.i(TAG, "RECETTES DANS LA LISTE: " + selectRecette.getIdRecette());
                        }

                        selectRecette = listRecettes.get(listRecettes.size()-1).getIdRecette();

                        Log.i(TAG, "DONNE LID LA, ICI: "+selectRecette);

                        List<Produits> listProduits = daoProduits.queryForAll();
                        List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

                        tableLayoutRecettes.removeAllViews();
                        getRecettesAfterSelect();
                        reloadRecetteAndProduit(listRecettes.get(listRecettes.size()-1).getLibelleRecette(), listProduits, listContenirRecettes);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            editPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "Recette non crée");
                }
            });
            editPopup.show();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void reloadRecetteAndProduit(String libelle, List<Produits> listProduits, List<ContenirRecettes> listContenirRecettes) {
        tableLayoutProduits.removeAllViews();
        textViewSelectRecette.setText("Recettes sélectionné: "+libelle);

        if(listProduits.size() != 0) {
            for(Produits produit: listProduits) {
                if(listContenirRecettes.size() != 0) {
                    for(ContenirRecettes contenirRecette: listContenirRecettes) {
                        Produits produitSelect = contenirRecette.getProduit();
                        if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                            getProduitsCheck(produitSelect);
                        }
                    }
                }
                getProduitsNoCheck(produit);
            }
        }
    }

    public void getMenuSwitch() {

        buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(RecettesActivity.this, MainActivity.class);
                startActivity(monIntent);
            }
        });

        buttonProduits = findViewById(R.id.buttonProduits);
        buttonProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(RecettesActivity.this, ProduitsActivity.class);
                startActivity(monIntent);
            }
        });

        /*
        buttonRecettes = findViewById(R.id.buttonRecettes);
        buttonRecettes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(RecettesActivity.this, RecettesActivity.class);
                startActivity(monIntent);
            }
        });*/

        buttonListe = findViewById(R.id.buttonListe);
        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(RecettesActivity.this, ListeActivity.class);
                startActivity(monIntent);
            }
        });

    }

    public void getRecettes() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Recettes> listRecette = daoRecettes.queryForAll();
            List<Produits> listProduit = daoProduits.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            if(listRecette.size() != 0) {

                // SELECTION DE LA PREMIERE RECETTE POUR RECETTES SELECTIONNE
                selectRecette = listRecette.get(0).getIdRecette();

                Recettes recetteSelect = daoRecettes.queryForId(selectRecette);

                textViewSelectRecette.setText("Recettes sélectionné: "+recetteSelect.getLibelleRecette());

                for (Recettes recette: listRecette) {
                    LinearLayout tableRecetteName = new LinearLayout(this);

                    // LIBELLE DE LA RECETTE

                    TextView libelleRecette = new TextView(this);
                    libelleRecette.setText(recette.getLibelleRecette());
                    tableRecetteName.addView(libelleRecette);

                    // BUTTON SELECT DE LA RECETTE

                    if(selectRecette != recette.getIdRecette()) {
                        Button buttonSelect = new Button(this);
                        buttonSelect.setText("SELECT");
                        buttonSelect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectRecette(recette.getIdRecette(), recette.getLibelleRecette(), listProduit, listContenirRecettes);
                            }
                        });

                        tableRecetteName.addView(buttonSelect);
                    }

                    // BUTTON REMOVE DE LA RECETTE

                    Button buttonRemove = new Button(this);
                    buttonRemove.setText("SUPPRIMER");
                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeRecette(recette);
                        }
                    });

                    tableRecetteName.addView(buttonRemove);

                    // BUTTON REMOVE DE LA RECETTE

                    Button buttonInfo = new Button(this);
                    buttonInfo.setText("INFO");
                    buttonInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            infoRecette(recette);
                        }
                    });

                    tableRecetteName.addView(buttonInfo);

                    tableLayoutRecettes.addView(tableRecetteName);
                }

                reloadRecetteAndProduit(recetteSelect.getLibelleRecette(), listProduit, listContenirRecettes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void infoRecette(Recettes recette) {
        AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
        infoRecettePopup.setTitle("Voulez-vous modifier les produits ajoutés à la recette ?");

        TableLayout tableLayout = new TableLayout(this);

        // LIBELLE DU PRODUIT

        TableRow tableRowLibelle = new TableRow(this);

        TextView infoLibelle = new TextView(this);
        infoLibelle.setText("Libelle: ");
        tableRowLibelle.addView(infoLibelle);

        // FAIRE CHECKBOX

        infoRecettePopup.setView(tableLayout);

        infoRecettePopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Retour");
            }
        });
        infoRecettePopup.show();
    }

    public void selectRecette(int idRecette, String libelle, List<Produits> listProduits, List<ContenirRecettes> listContenirRecettes) {
        Log.i(TAG, "TTTTTT"+libelle+"TTTTTT"+idRecette+"TTTTTT");
        selectRecette = idRecette;
        tableLayoutRecettes.removeAllViews();
        getRecettesAfterSelect();
        reloadRecetteAndProduit(libelle, listProduits, listContenirRecettes);

    }

    public void getRecettesAfterSelect() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Recettes> listRecette = daoRecettes.queryForAll();
            List<Produits> listProduit = daoProduits.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            if(listRecette.size() != 0) {

                Recettes recetteSelect = daoRecettes.queryForId(selectRecette);

                textViewSelectRecette.setText("Recettes sélectionné: "+recetteSelect.getLibelleRecette());

                for (Recettes recette: listRecette) {
                    LinearLayout tableRecetteName = new LinearLayout(this);

                    // LIBELLE DE LA RECETTE

                    TextView libelleRecette = new TextView(this);
                    libelleRecette.setText(recette.getLibelleRecette());
                    tableRecetteName.addView(libelleRecette);

                    // BUTTON SELECT DE LA RECETTE

                    if(selectRecette != recette.getIdRecette()) {
                        Button buttonSelect = new Button(this);
                        buttonSelect.setText("SELECT");
                        buttonSelect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectRecette(recette.getIdRecette(), recette.getLibelleRecette(), listProduit, listContenirRecettes);
                            }
                        });

                        tableRecetteName.addView(buttonSelect);
                    }

                    // BUTTON REMOVE DE LA RECETTE

                    Button buttonRemove = new Button(this);
                    buttonRemove.setText("SUPPRIMER");
                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeRecette(recette);
                        }
                    });

                    tableRecetteName.addView(buttonRemove);

                    //SPINNER DE LA RECETTE AVEC TOUT LES PRODUITS / QUI SONT MODIFIABLES

                    /*LinearLayout tableRecetteSpinner = new LinearLayout(this);
                    Spinner spinnerRecette = new Spinner(this);
                    tableRecetteSpinner.addView(spinnerRecette);*/

                    tableLayoutRecettes.addView(tableRecetteName);
                    //tableLayoutRecettes.addView(tableRecetteSpinner);
                }

                reloadRecetteAndProduit(recetteSelect.getLibelleRecette(), listProduit, listContenirRecettes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Recettes> listRecette = daoRecettes.queryForAll();
            List<Produits> listProduits = daoProduits.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            if (recette != null) {
                if (recette.getIdRecette() == selectRecette) {
                    if(listRecette.size() == 1) {
                        selectRecette = 0;
                        reloadRecetteAndProduit("Aucune recette", listProduits, listContenirRecettes);
                        tableLayoutRecettes.removeAllViews();
                        getRecettesAfterSelect();
                    } else {
                        selectRecette = listRecette.get(listRecette.size()-1).getIdRecette();
                        reloadRecetteAndProduit(recette.getLibelleRecette(), listProduits, listContenirRecettes);
                        tableLayoutRecettes.removeAllViews();
                        getRecettesAfterSelect();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduit(Produits produit, boolean checkValue) {
        LinearLayout tableRowProduit = new LinearLayout(this);

        TextView value = new TextView(this);
        value.setText(produit.getLibelle()+" | "+produit.getQuantite());
        //value.setTextSize(20);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(checkValue);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = checkBox.isChecked();
                if (checked){
                    addProduitRecette(produit);
                }
                else{
                    removeProduitRecette(produit);
                }
            }
        });

        tableRowProduit.addView(value);
        tableRowProduit.addView(checkBox);

        tableLayoutProduits.addView(tableRowProduit);
    }

    public void getProduitsCheck(Produits produit) {
        editTextSearch = findViewById(R.id.editTextSearch);

        if(!editTextSearch.getText().toString().equals("")) {

            editTextSearch.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tableLayoutProduits.removeAllViews();
                    if(s != "") {
                        if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                            getProduit(produit, true);
                        }
                    } else {
                        getProduit(produit, true);
                    }
                }
            });
        } else {
            getProduit(produit, true);
        }
    }

    public void getProduitsNoCheck(Produits produit) {
        editTextSearch = findViewById(R.id.editTextSearch);

        if(!editTextSearch.getText().toString().equals("")) {

            editTextSearch.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tableLayoutProduits.removeAllViews();
                    if(s != "") {
                        if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                            getProduit(produit, false);
                        }
                    } else {
                        getProduit(produit, false);
                    }
                }
            });
        } else {
            getProduit(produit, false);
        }
    }

    public void addProduitRecette(Produits produit){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);
            Dao<Recettes, Integer> daoRecettes = linker.getDao(Recettes.class);

            Recettes recetteSelect = daoRecettes.queryForId(selectRecette);

            ContenirRecettes contenirRecette = new ContenirRecettes();
            contenirRecette.setQuantite(1);
            contenirRecette.setRecette(recetteSelect);
            contenirRecette.setProduit(produit);
            daoContenirRecettes.create(contenirRecette);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeProduitRecette(Produits produit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);
            Dao<Recettes, Integer> daoRecettes = linker.getDao(Recettes.class);
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            if (produit != null) {
                Recettes recette = daoRecettes.queryForId(selectRecette);
                ContenirRecettes contenirRecette = daoContenirRecettes.queryForId(recette.getIdRecette());
                Produits produitSelect = contenirRecette.getProduit();

                if(produit.getIdProduit() == produitSelect.getIdProduit()) {
                    daoContenirRecettes.delete(contenirRecette);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}