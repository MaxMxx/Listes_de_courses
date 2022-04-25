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

    //POPUP XML
    private AlertDialog.Builder infoRecettePopup;
    private AlertDialog dialog;

    private EditText textSearchProduitYes;
    private EditText textSearchProduitNo;
    private LinearLayout linearLayoutYes;
    private LinearLayout linearLayoutNo;
    private Button buttonRetour;
    private TextView textViewSelect;


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
            infoLibelle.setText("Nom: ");
            infoLibelle.setTextSize(18);
            tableRowLibelle.addView(infoLibelle);

            EditText createLibelle = new EditText(this);
            createLibelle.setTextSize(18);
            createLibelle.setHint("Nom de votre recette");
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
                    buttonEdit.setText("MODIFIER");
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
                if (contenirRecette.getRecette().getIdRecette() == recette.getIdRecette()) {
                    daoContenirRecettes.delete(contenirRecette);
                }
            }

            for(ListesRecettes listesRecettes : listListesRecettes) {
                if(listesRecettes.getRecette().getIdRecette() == recette.getIdRecette()) {
                    for(Listes listes : listListes) {
                        if(listes.getIdListe() == listesRecettes.getListe().getIdListe()) {
                            daoListesRecettes.delete(listesRecettes);
                            daoListes.delete(listes);
                        }
                    }
                }
            }

            daoRecettes.delete(recette);

            reloadPageAll();

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
        libelleText.setText(recette.getLibelleRecette());
        linearLayout.addView(libelleText);

        updateRecettePopup.setView(linearLayout);

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

        getMenuSwitch();
        getRecettes();
        createRecette();
    }

    public void reloadPopupInfo(Recettes recette) {
        dialog.dismiss();
        linearLayoutYes.removeAllViews();
        linearLayoutNo.removeAllViews();
        infoRecette(recette);
    }

    public void infoRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Log.i(TAG, "LIGNE 394: infoRecette: RECETTE AVEC L'ID: "+recette.getIdRecette());
            infoRecettePopup = new AlertDialog.Builder(this);
            View infoRecettePopupView = getLayoutInflater().inflate(R.layout.popup, null);

            textSearchProduitYes = infoRecettePopupView.findViewById(R.id.textSearchProduitYes);
            textSearchProduitNo = infoRecettePopupView.findViewById(R.id.textSearchProduitNo);
            linearLayoutYes = infoRecettePopupView.findViewById(R.id.linearLayoutYes);
            linearLayoutNo = infoRecettePopupView.findViewById(R.id.linearLayoutNo);
            buttonRetour = infoRecettePopupView.findViewById(R.id.buttonRetour);

            textViewSelect = infoRecettePopupView.findViewById(R.id.textViewSelect);
            textViewSelect.setText("Recette sélectionnée: "+recette.getLibelleRecette());

            buttonRetour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "RETOUR NOW");
                    dialog.dismiss();
                }
            });

            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();


            if(listContenirRecettes.size() == 0) {
                getProduitInfoPopup(recette, linearLayoutNo, false, listContenirRecettes);
            } else {
                getProduitInRecettePopup(recette, linearLayoutYes, listContenirRecettes);
                getProduitInfoPopup(recette, linearLayoutNo, true, listContenirRecettes);
            }

            infoRecettePopup.setView(infoRecettePopupView);
            dialog = infoRecettePopup.create();
            dialog.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void editQuantiteInRecette(ContenirRecettes contenirRecette, int quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            contenirRecette.setQuantite(quantite);
            daoContenirRecettes.update(contenirRecette);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduitInRecettePopup(Recettes recette, LinearLayout linearLayoutYes, List<ContenirRecettes> listContenirRecettes) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            List<Produits> listProduit = daoProduits.queryForAll();

            for(ContenirRecettes contenirRecette : listContenirRecettes) {
                if(contenirRecette.getRecette().getIdRecette() == recette.getIdRecette()) {
                    boolean verification = false;
                    for(Produits produit : listProduit) {
                        if(contenirRecette.getProduit().getIdProduit() == produit.getIdProduit()) {
                            verification = true;
                        }
                    }
                    if(verification) {
                        LinearLayout tableRow = new LinearLayout(this);
                        TextView libelle = new TextView(this);
                        EditText quantite = new EditText(this);
                        CheckBox checkBox = new CheckBox(this);

                        libelle.setText(contenirRecette.getProduit().getLibelle());
                        quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                        quantite.setText(""+contenirRecette.getQuantite());
                        checkBox.setChecked(true);

                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean checked = checkBox.isChecked();
                                if (!checked){
                                    removeProduitRecette(contenirRecette.getProduit(), recette);
                                }
                            }
                        });

                        quantite.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable s) {
                            }

                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                boolean verification = true;
                                if(s.toString().equals("0")) {
                                    verification = false;
                                }
                                if(s.toString().length() == 0) {
                                    verification = false;
                                }
                                if(verification) {
                                    editQuantiteInRecette(contenirRecette, Integer.parseInt(s.toString()));
                                }
                            }
                        });

                        textSearchProduitYes.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable s) {}
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                linearLayoutYes.removeAllViews();
                                if(s != "" || s != " ") {
                                    if((contenirRecette.getProduit().getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                                        libelle.setText(contenirRecette.getProduit().getLibelle());
                                        quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                                        quantite.setText(""+contenirRecette.getQuantite());
                                        checkBox.setChecked(true);

                                        checkBox.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                boolean checked = checkBox.isChecked();
                                                if (!checked){
                                                    removeProduitRecette(contenirRecette.getProduit(), recette);
                                                }
                                            }
                                        });

                                        quantite.addTextChangedListener(new TextWatcher() {
                                            public void afterTextChanged(Editable s) {
                                            }

                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            }

                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                boolean verification = true;
                                                if(s.toString().equals("0")) {
                                                    verification = false;
                                                }
                                                if(s.toString().length() == 0) {
                                                    verification = false;
                                                }
                                                if(verification) {
                                                    editQuantiteInRecette(contenirRecette, Integer.parseInt(s.toString()));
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    libelle.setText(contenirRecette.getProduit().getLibelle());
                                    quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                                    quantite.setText(""+contenirRecette.getQuantite());
                                    checkBox.setChecked(true);

                                    checkBox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            boolean checked = checkBox.isChecked();
                                            if (!checked){
                                                removeProduitRecette(contenirRecette.getProduit(), recette);
                                            }
                                        }
                                    });

                                    quantite.addTextChangedListener(new TextWatcher() {
                                        public void afterTextChanged(Editable s) {
                                        }

                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            boolean verification = true;
                                            if(s.toString().equals("0")) {
                                                verification = false;
                                            }
                                            if(s.toString().length() == 0) {
                                                verification = false;
                                            }
                                            if(verification) {
                                                editQuantiteInRecette(contenirRecette, Integer.parseInt(s.toString()));
                                            }
                                        }
                                    });
                                }
                            }
                        });

                        tableRow.addView(checkBox);
                        tableRow.addView(libelle);
                        tableRow.addView(quantite);
                        linearLayoutYes.addView(tableRow);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduitInfoPopup(Recettes recette, LinearLayout linearLayoutNo, boolean contains, List<ContenirRecettes> listContenirRecettes) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            List<Produits> listProduit = daoProduits.queryForAll();

            for(Produits produit : listProduit) {
                if(contains) {
                    boolean verification = true;
                    for(ContenirRecettes contenirRecette : listContenirRecettes) {
                        if(contenirRecette.getRecette().getIdRecette() == recette.getIdRecette()) {
                            if(contenirRecette.getProduit().getIdProduit() == produit.getIdProduit()) {
                                verification = false;
                            }
                        }
                    }
                    if(verification) {
                        LinearLayout tableRow = new LinearLayout(this);
                        TextView libelle = new TextView(this);
                        EditText quantite = new EditText(this);
                        CheckBox checkBox = new CheckBox(this);

                        libelle.setText(produit.getLibelle());

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
                                    addProduitRecette(produit, recette, qtn);
                                }
                            }
                        });

                        textSearchProduitNo.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable s) {}
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                linearLayoutNo.removeAllViews();
                                if(s != "" || s != " ") {
                                    if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                                        libelle.setText(produit.getLibelle());
                                        quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

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
                                                    addProduitRecette(produit, recette, qtn);
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    libelle.setText(produit.getLibelle());
                                    quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

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
                                                addProduitRecette(produit, recette, qtn);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        tableRow.addView(checkBox);
                        tableRow.addView(libelle);
                        tableRow.addView(quantite);
                        linearLayoutNo.addView(tableRow);
                    }
                } else {
                    LinearLayout tableRow = new LinearLayout(this);
                    TextView libelle = new TextView(this);
                    EditText quantite = new EditText(this);
                    CheckBox checkBox = new CheckBox(this);

                    libelle.setText(produit.getLibelle());

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
                                addProduitRecette(produit, recette, qtn);
                            }
                        }
                    });

                    textSearchProduitNo.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {}
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            linearLayoutNo.removeAllViews();
                            if(s != "" || s != " ") {
                                if((produit.getLibelle().toLowerCase()).contains(s.toString().toLowerCase())) {
                                    libelle.setText(produit.getLibelle());
                                    quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

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
                                                addProduitRecette(produit, recette, qtn);
                                            }
                                        }
                                    });
                                }
                            } else {
                                libelle.setText(produit.getLibelle());
                                quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

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
                                            addProduitRecette(produit, recette, qtn);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    tableRow.addView(checkBox);
                    tableRow.addView(libelle);
                    tableRow.addView(quantite);
                    linearLayoutNo.addView(tableRow);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addProduitRecette(Produits produit, Recettes recette, int quantite){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);

            ContenirRecettes contenirRecette = new ContenirRecettes();
            contenirRecette.setQuantite(quantite);
            contenirRecette.setRecette(recette);
            contenirRecette.setProduit(produit);
            daoContenirRecettes.create(contenirRecette);

            reloadPopupInfo(recette);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeProduitRecette(Produits produit, Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            for(ContenirRecettes contenirRecettes : listContenirRecettes) {
                if(contenirRecettes.getRecette().getIdRecette() == recette.getIdRecette()) {
                    if(contenirRecettes.getProduit().getIdProduit() == produit.getIdProduit()) {
                        daoContenirRecettes.delete(contenirRecettes);
                        reloadPopupInfo(recette);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}