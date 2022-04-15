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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.ContenirRecettes;
import com.example.myapplication.Entity.ListesProduits;
import com.example.myapplication.Entity.Produits;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class ProduitsActivity extends AppCompatActivity {

    private static final String TAG = "ProduitsActivity";
    private TableLayout produitsTableLayout;

    private EditText editTextSearch;

    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    private Button buttonCreateProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits_main);
        produitsTableLayout = findViewById(R.id.produitsTableLayout);
        getSupportActionBar().hide();

        buttonCreate();

        getMenuSwitch();

        getEditSearch();
    }

    public void buttonCreate() {
        buttonCreateProduit = findViewById(R.id.buttonCreateProduit);
        buttonCreateProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopupCreate();
            }
        });
    }

    public void getPopupCreate() {
        AlertDialog.Builder createPopup = new AlertDialog.Builder(this);
        createPopup.setTitle("Voulez-vous créer un produit ?");

        //TableLayout EDIT

        TableLayout tableLayout = new TableLayout(this);

        // MODIFICATION LIBELLE PRODUIT

        TableRow tableRowLibelle = new TableRow(this);

        TextView infoLibelle = new TextView(this);
        infoLibelle.setText("Libelle: ");
        tableRowLibelle.addView(infoLibelle);

        EditText createLibelle = new EditText(this);
        tableRowLibelle.addView(createLibelle);

        tableLayout.addView(tableRowLibelle);

        // MODIFICATION QUANTITE PRODUIT

        TableRow tableRowQuantite = new TableRow(this);

        TextView infoQuantite = new TextView(this);
        infoQuantite.setText("Quantite: ");
        tableRowQuantite.addView(infoQuantite);

        EditText createQuantite = new EditText(this);
        tableRowQuantite.addView(createQuantite);

        tableLayout.addView(tableRowQuantite);

        // ADD VIEW IN POPUP EDIT

        createPopup.setView(tableLayout);

        //deletePopup.setMessage("Cliquez sur oui ou non");
        createPopup.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { ;
                createProduit(createLibelle.getText().toString(), createQuantite.getText().toString());
            }
        });
        createPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Produit non crée");
            }
        });
        createPopup.show();
    }

    public void createProduit(String libelle, String quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            Produits produit = new Produits();
            produit.setLibelle(libelle);
            produit.setQuantite(Integer.parseInt(quantite));
            daoProduits.create(produit);

            reloadProduitsLayout();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void reloadProduitsLayout() {
        produitsTableLayout.removeAllViews();
        getEditSearch();
    }

    public void getMenuSwitch() {
        buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ProduitsActivity.this, MainActivity.class);
                startActivity(monIntent);
            }
        });

        /*
        buttonProduits = findViewById(R.id.buttonProduits);
        buttonProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ProduitsActivity.this, ProduitsActivity.class);
                startActivity(monIntent);
            }
        });
        */

        buttonRecettes = findViewById(R.id.buttonRecettes);
        buttonRecettes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ProduitsActivity.this, RecettesActivity.class);
                startActivity(monIntent);
            }
        });

        buttonListe = findViewById(R.id.buttonListe);
        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ProduitsActivity.this, ListeActivity.class);
                startActivity(monIntent);
            }
        });

    }

    public void getProduits(Produits produit) {
        TableRow tableRowProduit = new TableRow(this);

        TextView value = new TextView(this);
        value.setText(produit.getLibelle() + " " + produit.getQuantite());

        tableRowProduit.addView(value);

        int idProduit = produit.getIdProduit();

        Button deleteProduit = new Button(this);
        deleteProduit.setText("Delete");
        deleteProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduit(idProduit, tableRowProduit);
            }
        });

        tableRowProduit.addView(deleteProduit);

        Button editProduit = new Button(this);
        editProduit.setText("Edit");
        editProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduit(idProduit, tableRowProduit);
            }
        });

        tableRowProduit.addView(editProduit);

        produitsTableLayout.addView(tableRowProduit);
    }

    public void getEditSearch() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

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
                    produitsTableLayout.removeAllViews();
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

    public void editProduit(int idProduit, TableRow tableRowProduit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            Produits produit = daoProduits.queryForId(idProduit);

            if (produit != null) {
                AlertDialog.Builder editPopup = new AlertDialog.Builder(this);
                editPopup.setTitle("Voulez vous modifier "+produit.getLibelle()+" ?");

                //TableLayout EDIT

                TableLayout tableLayout = new TableLayout(this);

                // MODIFICATION LIBELLE PRODUIT

                TableRow tableRowLibelle = new TableRow(this);

                TextView infoLibelle = new TextView(this);
                infoLibelle.setText("Libelle: ");
                tableRowLibelle.addView(infoLibelle);

                EditText changeLibelle = new EditText(this);
                changeLibelle.setText(produit.getLibelle());
                tableRowLibelle.addView(changeLibelle);

                tableLayout.addView(tableRowLibelle);

                // MODIFICATION QUANTITE PRODUIT

                TableRow tableRowQuantite = new TableRow(this);

                TextView infoQuantite = new TextView(this);
                infoQuantite.setText("Quantite: ");
                tableRowQuantite.addView(infoQuantite);

                EditText changeQuantite = new EditText(this);
                changeQuantite.setText(""+produit.getQuantite());
                tableRowQuantite.addView(changeQuantite);

                tableLayout.addView(tableRowQuantite);

                // ADD VIEW IN POPUP EDIT

                editPopup.setView(tableLayout);

                //deletePopup.setMessage("Cliquez sur oui ou non");
                editPopup.setPositiveButton("Mettre à jour", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { ;
                        createProduit(idProduit, changeLibelle.getText().toString(), Integer.parseInt(changeQuantite.getText().toString()));
                    }
                });
                editPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Produit non mis à jour");
                    }
                });
                editPopup.show();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void createProduit(int idProduit, String libelle, int quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            Produits produit = daoProduits.queryForId(idProduit);

            produit.setLibelle(libelle);
            produit.setQuantite(quantite);
            daoProduits.update(produit);

            reloadProduitsLayout();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeP(List<ContenirRecettes> listContenirRecettes, List<ListesProduits> listCoursesProduits, Produits produit, TableRow tableRowProduit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao(ListesProduits.class);

            for(ContenirRecettes contenirRecette: listContenirRecettes) {
                Produits produitSelect = contenirRecette.getProduit();
                if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                    daoContenirRecettes.delete(contenirRecette);
                }
            }

            for(ListesProduits liste: listCoursesProduits) {
                Produits produitSelect = liste.getProduit();
                if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                    daoListesProduits.delete(liste);
                }
            }

            daoProduits.delete(produit);

            Log.i(TAG, "Vous avez suprimmé !");

            reloadProduitsLayout();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void deleteProduit(int idProduit, TableRow tableRowProduit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao(ContenirRecettes.class);
            Dao<ListesProduits, Integer> daoListes = linker.getDao(ListesProduits.class);

            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();
            List<ListesProduits> listCourses = daoListes.queryForAll();

            Produits produit = daoProduits.queryForId(idProduit);
            if (produit != null) {
                AlertDialog.Builder deletePopup = new AlertDialog.Builder(this);
                deletePopup.setTitle("Etes vous sur de vouloir supprimer "+produit.getLibelle()+" ?");
                //deletePopup.setMessage("Cliquez sur oui ou non");
                deletePopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeP(listContenirRecettes, listCourses, produit, tableRowProduit);
                    }
                });
                deletePopup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Vous n'avez pas suprimmé !");
                    }
                });
                deletePopup.show();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
}
