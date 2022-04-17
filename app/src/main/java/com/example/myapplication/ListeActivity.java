package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

    private static final String TAG = "ListeActivity";
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

            Log.i(TAG, "getListeAll: 0");

            if(listListes.size() != 0) {
                for(Listes liste: listListes) {
                    Log.i(TAG, "getListeAll: 1");
                    for(ListesProduits listesProduit: listListesProduits) {
                        Log.i(TAG, "getListeAll: 2");
                        Listes listeSelect = listesProduit.getListe();
                        if(liste.getIdListe() == listeSelect.getIdListe()) {
                            Log.i(TAG, "getListeAll: 3");
                            for(Produits produit: listProduits) {
                                Log.i(TAG, "getListeAll: 4");
                                Produits produitSelect = listesProduit.getProduit();
                                if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                                    Log.i(TAG, "getListeAll: 5");
                                    LinearLayout linearLayout = new LinearLayout(this);



                                    TextView libelle = new TextView(this);
                                    if(listeSelect.isCart()) {
                                        libelle.setText(Html.fromHtml("<strike>"+produitSelect.getLibelle()+"</strike>"));
                                    } else {
                                        libelle.setText(produitSelect.getLibelle());
                                    }
                                    linearLayout.addView(libelle);

                                    EditText quantite = new EditText(this);
                                    quantite.setText(""+liste.getQuantite());
                                    linearLayout.addView(quantite);

                                    CheckBox addCart = new CheckBox(this);
                                    addCart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            boolean checked = addCart.isChecked();
                                            if(checked){
                                                addInCart(listeSelect.getIdListe());
                                            } else {
                                                removeInCart(listeSelect.getIdListe());
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
                                                if(listeSelect.isCart()) {
                                                    libelle.setText(Html.fromHtml("<strike>"+recetteContenirSelect.getLibelleRecette()+"</strike>"));
                                                } else {
                                                    libelle.setText(recetteContenirSelect.getLibelleRecette());
                                                }
                                                linearLayout.addView(libelle);

                                                EditText quantite = new EditText(this);
                                                quantite.setText(""+liste.getQuantite());
                                                linearLayout.addView(quantite);

                                                Button info = new Button(this);
                                                info.setText("Info");
                                                info.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        viewProduitInfo(recettes.getIdRecette());
                                                    }
                                                });
                                                linearLayout.addView(info);

                                                CheckBox addCart = new CheckBox(this);
                                                addCart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        boolean checked = addCart.isChecked();
                                                        if(checked){
                                                            addInCart(listeSelect.getIdListe());
                                                        } else {
                                                            removeInCart(listeSelect.getIdListe());
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
            } else {
                LinearLayout linearLayout = new LinearLayout(this);
                TextView liste = new TextView(this);
                liste.setText("Votre liste est vide !");
                linearLayout.addView(liste);
                containerListe.addView(linearLayout);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addInCart(int idListe) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );

            Listes listes = daoListes.queryForId(idListe);
            listes.setCart(true);
            daoListes.update(listes);

            reloadListeAfterChange();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeInCart(int idListe) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );

            Listes listes = daoListes.queryForId(idListe);
            listes.setCart(false);
            daoListes.update(listes);

            reloadListeAfterChange();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void viewProduitInfo(int idRecette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            Recettes recettes = daoRecettes.queryForId(idRecette);
            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
            infoRecettePopup.setTitle("Produits dans la recette "+recettes.getLibelleRecette()+" :");

            for(ContenirRecettes contenirRecette:listContenirRecettes) {
                if(recettes.getIdRecette() == contenirRecette.getIdContenirRecettes()) {
                    TableLayout tableLayout = new TableLayout(this);

                    Produits produit = contenirRecette.getProduit();

                    TextView libelle = new TextView(this);
                    libelle.setText(produit.getLibelle() + " | " + contenirRecette.getQuantite());

                    tableLayout.addView(libelle);
                    infoRecettePopup.setView(tableLayout);
                }
            }

            infoRecettePopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i("TAG", "Retour");
                }
            });

            infoRecettePopup.show();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void deleteListe() {
        AlertDialog.Builder deletePopup = new AlertDialog.Builder(this);
        deletePopup.setTitle("Etes vous sur de vouloir supprimer la Liste ?");

        deletePopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeAllListe();
            }
        });

        deletePopup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i("TAG", "Retour");
            }
        });

        deletePopup.show();
    }

    public void removeAllListe() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );

            List<Listes> listListes = daoListes.queryForAll();
            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();
            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();

            Log.i(TAG, "removeAllListeAVANTTTTTTTTTTTTT: "+listListes.size()+" "+listListesRecettes.size()+" "+listListesProduits.size());

            for(Listes listes : listListes) {
                daoListes.delete(listes);
            }

            for(ListesProduits listesProduits : listListesProduits) {
                daoListesProduits.delete(listesProduits);
            }

            for(ListesRecettes listesRecettes : listListesRecettes) {
                daoListesRecettes.delete(listesRecettes);
            }

            reloadListeAfterChange();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void reloadListeAfterChange() {

        containerListe.removeAllViews();

        getListeAll();


    }

}

