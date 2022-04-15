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
import com.example.myapplication.Entity.Listes;
import com.example.myapplication.Entity.ListesRecettes;
import com.example.myapplication.Entity.Produits;
import com.example.myapplication.Entity.Recettes;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.List;


public class RecettesActivity extends AppCompatActivity {

    private static final String TAG = "RecettesActivity";

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
        getSupportActionBar().hide();

        getMenuSwitch();
        getRecettes();
        createRecette();

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
                        Recettes recette = new Recettes();
                        recette.setLibelleRecette(createLibelle.getText().toString());
                        daoRecettes.create(recette);

                        reloadPageAll();

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
                for (Recettes recette: listRecette) {
                    LinearLayout tableRecetteName = new LinearLayout(this);

                    // LIBELLE DE LA RECETTE

                    TextView libelleRecette = new TextView(this);
                    libelleRecette.setText(recette.getLibelleRecette());
                    tableRecetteName.addView(libelleRecette);

                    // BUTTON REMOVE DE LA RECETTE

                    Button buttonEdit = new Button(this);
                    buttonEdit.setText("SUPPRIMER");
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editRecettePopup(recette);
                        }
                    });

                    tableRecetteName.addView(buttonEdit);

                    // BUTTON REMOVE DE LA RECETTE

                    Button buttonRemove = new Button(this);
                    buttonRemove.setText("SUPPRIMER");
                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeRecettePopup(recette);
                        }
                    });

                    tableRecetteName.addView(buttonRemove);

                    // BUTTON INFO DE LA RECETTE

                    Button buttonInfo = new Button(this);
                    buttonInfo.setText("DETAILS");
                    buttonInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            infoRecette(recette);
                        }
                    });

                    tableRecetteName.addView(buttonInfo);

                    tableLayoutRecettes.addView(tableRecetteName);
                }

            } else {
                LinearLayout nothingRecette = new LinearLayout(this);

                TextView infoText = new TextView(this);
                infoText.setText("Aucune recette n'est disponibe, vous pouvez en créer une !");
                nothingRecette.addView(infoText);

                tableLayoutRecettes.addView(nothingRecette);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeRecettePopup(Recettes recette) {
        AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
        infoRecettePopup.setTitle("Etes vous sur de vouloir supprimer "+recette.getLibelleRecette()+" ?");

        infoRecettePopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeRecette(recette);
            }
        });

        infoRecettePopup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Non");
            }
        });

        infoRecettePopup.show();
    }

    public void removeRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );

            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();
            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();
            List<Listes> listListes = daoListes.queryForAll();

            for(ContenirRecettes contenirRecette : listContenirRecettes) {
                Recettes recetteSelect = contenirRecette.getRecette();
                if(recetteSelect.getIdRecette() == recette.getIdRecette()) {
                    for(Listes liste: listListes) {
                        for(ListesRecettes listesRecette : listListesRecettes) {
                            if(liste.getIdListe() == listesRecette.getListe().getIdListe()) {
                                daoContenirRecettes.delete(contenirRecette);
                                daoListesRecettes.delete(listesRecette);
                                daoListes.delete(liste);
                                daoRecettes.delete(recette);
                            }
                        }
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void editRecettePopup(Recettes recette) {
        AlertDialog.Builder updateRecettePopup = new AlertDialog.Builder(this);
        updateRecettePopup.setTitle("Voulez vous mettre à jour "+recette.getLibelleRecette()+" ?");

        LinearLayout linearLayout = new LinearLayout(this);

        TextView libelle = new TextView(this);
        libelle.setText("Nom de la recette: ");
        linearLayout.addView(libelle);

        EditText libelleText = new EditText(this);
        linearLayout.addView(libelleText);

        updateRecettePopup.setPositiveButton("Mettre à jour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editRecette(recette, libelleText.getText().toString());
            }
        });

        updateRecettePopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Retour");
            }
        });

        updateRecettePopup.show();
    }

    public void editRecette(Recettes recette, String libelle) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );

            Recettes recetteChange = daoRecettes.queryForId(recette.getIdRecette());
            recetteChange.setLibelleRecette(libelle);
            daoRecettes.update(recetteChange);

            reloadPageAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void reloadPageAll() {

        tableLayoutRecettes.removeAllViews();

        getRecettes();

    }

    public void reloadPopupInfo() {







    }

    public void infoRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
            infoRecettePopup.setTitle("Information sur la recette");
    
            TableLayout tableLayout = new TableLayout(this);
            
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Produits> listProduit = daoProduits.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            for(ContenirRecettes contenirRecette:listContenirRecettes) {
                Recettes recetteSelect = contenirRecette.getRecette();
                if(recetteSelect.getIdRecette() == recette.getIdRecette()) {
                    for(Produits produit: listProduit) {
                       Produits produitSelect = contenirRecette.getProduit();
                       if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                           // LIBELLE DU PRODUIT

                           TableRow tableRowLibelle = new TableRow(this);

                           TextView infoLibelle = new TextView(this);
                           infoLibelle.setText("Libelle: "+produitSelect.getLibelle());
                           tableRowLibelle.addView(infoLibelle);

                           // CHECK BOX DU PRODUIT

                           CheckBox checkBox = new CheckBox(this);
                           checkBox.setChecked(true);
                           checkBox.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   boolean checked = checkBox.isChecked();
                                   if (!checked){
                                       removeProduitRecette(produit);

                                   }
                               }
                           });

                           tableRowLibelle.addView(checkBox);

                           tableLayout.addView(tableRowLibelle);

                           infoRecettePopup.setView(tableLayout);

                           infoRecettePopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   Log.i(TAG, "Retour");
                               }
                           });
                       }
                    }
                }
            }

            infoRecettePopup.show();
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

            //Recettes recetteSelect = daoRecettes.queryForId(selectRecette);
            Recettes recetteSelect = daoRecettes.queryForId(1);

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
                //Recettes recette = daoRecettes.queryForId(selectRecette);
                Recettes recette = daoRecettes.queryForId(1);
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