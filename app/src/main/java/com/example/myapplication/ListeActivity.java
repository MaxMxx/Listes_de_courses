package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    private TableLayout containerListe;
    private Button buttonSupprimerListe;

    private Button buttonMain;
    private Button buttonProduits;
    private Button buttonRecettes;
    private Button buttonListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste);
        this.deleteDatabase("listeDeCourses.db");
        DataBaseLinker linker = new DataBaseLinker(this);
        getSupportActionBar().hide();

        containerListe = findViewById(R.id.container_liste);
        buttonSupprimerListe = findViewById(R.id.button_supprimer_liste);

        getMenuSwitch();

        getListeAll();

    }

    public void getMenuSwitch() {

        buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ListeActivity.this, MainActivity.class);
                startActivity(monIntent);
            }
        });

        buttonProduits = findViewById(R.id.buttonProduits);
        buttonProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ListeActivity.this, ProduitsActivity.class);
                startActivity(monIntent);
            }
        });


        buttonRecettes = findViewById(R.id.buttonRecettes);
        buttonRecettes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ListeActivity.this, RecettesActivity.class);
                startActivity(monIntent);
            }
        });

        /*
        buttonListe = findViewById(R.id.buttonListe);
        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(ListeActivity.this, ListeActivity.class);
                startActivity(monIntent);
            }
        });
        */

    }


    private void getListeAll(){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<Listes> listListes = daoListes.queryForAll();
            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();
            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();
            List<Recettes> listRecettes = daoRecettes.queryForAll();
            List<Produits> listProduits = daoProduits.queryForAll();
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            for(Listes liste: listListes) {
                for(ListesProduits listesProduit: listListesProduits) {
                    Listes listeSelect = listesProduit.getListe();
                    if(liste.getIdListe() == listeSelect.getIdListe()) {
                        for(Produits produit: listProduits) {
                            Produits produitSelect = listesProduit.getProduit();
                            if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                                LinearLayout linearLayout = new LinearLayout(this);

                                TextView libelle = new TextView(this);
                                libelle.setText(produitSelect.getLibelle());
                                linearLayout.addView(libelle);

                                EditText quantite = new EditText(this);
                                quantite.setText(liste.getQuantite());
                                linearLayout.addView(quantite);

                                CheckBox addCart = new CheckBox(this);
                                addCart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean checked = addCart.isChecked();
                                        if(checked){
                                            addInCart();
                                        } else {
                                            removeInCart();
                                        }
                                    }
                                });
                                linearLayout.addView(addCart);

                                containerListe.addView(linearLayout);
                            }
                        }
                    }
                }
                for(ListesRecettes listesRecettes: listListesRecettes) {
                    Listes listeSelect = listesRecettes.getListe();
                    if(liste.getIdListe() == listeSelect.getIdListe()) {
                        if(liste.getIdListe() == listeSelect.getIdListe()) {
                            for(Recettes recettes: listRecettes) {
                                Recettes recetteSelect = listesRecettes.getRecette();
                                if(recettes.getIdRecette() == recetteSelect.getIdRecette()) {
                                    for(ContenirRecettes contenirRecettes: listContenirRecettes) {
                                        Recettes recetteContenirSelect = contenirRecettes.getRecette();
                                        if(recetteContenirSelect.getIdRecette() == recetteSelect.getIdRecette()) {
                                            LinearLayout linearLayout = new LinearLayout(this);

                                            TextView libelle = new TextView(this);
                                            libelle.setText(recetteContenirSelect.getLibelleRecette());
                                            linearLayout.addView(libelle);

                                            EditText quantite = new EditText(this);
                                            quantite.setText(liste.getQuantite());
                                            linearLayout.addView(quantite);

                                            Button info = new Button(this);
                                            info.setText("Info");
                                            info.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                   viewProduitInfo();
                                                }
                                            });
                                            linearLayout.addView(info);

                                            CheckBox addCart = new CheckBox(this);
                                            addCart.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    boolean checked = addCart.isChecked();
                                                    if(checked){
                                                        addInCart();
                                                    } else {
                                                        removeInCart();
                                                    }
                                                }
                                            });
                                            linearLayout.addView(addCart);

                                            containerListe.addView(linearLayout);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            buttonSupprimerListe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListe();
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addInCart() {

    }

    public void removeInCart() {

    }

    public void viewProduitInfo() {

    }

    public void deleteListe() {

    }

}

