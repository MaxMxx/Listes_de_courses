package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
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


            if(listListes.size() != 0) {
                for(Listes liste: listListes) {
                    for(ListesProduits listesProduit: listListesProduits) {
                        Listes listeSelect = listesProduit.getListe();
                        if(liste.getIdListe() == listeSelect.getIdListe()) {
                            for(Produits produit: listProduits) {
                                Produits produitSelect = listesProduit.getProduit();
                                if(produitSelect.getIdProduit() == produit.getIdProduit()) {
                                    LinearLayout linearLayout = new LinearLayout(this);

                                    TextView libelle = new TextView(this);
                                    if(listeSelect.isCart()) {
                                        libelle.setText(Html.fromHtml("<strike>"+produitSelect.getLibelle()+"</strike>"));
                                    } else {
                                        libelle.setText(produitSelect.getLibelle());
                                    }
                                    linearLayout.addView(libelle);

                                    EditText quantite = new EditText(this);
                                    quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                                    quantite.setText(""+liste.getQuantite());
                                    linearLayout.addView(quantite);

                                    quantite.addTextChangedListener(new TextWatcher() {
                                        public void afterTextChanged(Editable s) {
                                        }

                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            if(quantite.getText().toString() != "" || quantite.getText().toString() != " " || quantite.getText().toString() != "0") {
                                                editQuantite(listeSelect, Integer.parseInt(quantite.getText().toString()));
                                            }
                                        }
                                    });

                                    Button deleteButton = new Button(this);
                                    deleteButton.setText("SUPPRIMER");
                                    deleteButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            removeProduit(produit);
                                        }
                                    });

                                    linearLayout.addView(deleteButton);

                                    CheckBox addCart = new CheckBox(this);
                                    if(liste.isCart()) {
                                        addCart.setChecked(true);
                                    }
                                    addCart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            boolean checked = addCart.isChecked();
                                            if(checked){
                                                addInCartProduit(listeSelect.getIdListe(), produit);
                                            } else {
                                                removeInCartProduit(listeSelect.getIdListe(), produit);
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

                            LinearLayout linearLayout = new LinearLayout(this);

                            TextView libelle = new TextView(this);
                            if(listeSelect.isCart()) {
                                libelle.setText(Html.fromHtml("<strike>"+listesRecettes.getRecette().getLibelleRecette()+"</strike>"));
                            } else {
                                libelle.setText(listesRecettes.getRecette().getLibelleRecette());
                            }
                            linearLayout.addView(libelle);

                            EditText quantite = new EditText(this);
                            quantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                            quantite.setText(""+liste.getQuantite());
                            linearLayout.addView(quantite);

                            quantite.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if(quantite.getText().toString() != "" || quantite.getText().toString() != " " || quantite.getText().toString() != "0") {
                                        editQuantite(listeSelect, Integer.parseInt(quantite.getText().toString()));
                                    }
                                }
                            });

                            Button info = new Button(this);
                            info.setText("Info");
                            info.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewProduitInfo(listesRecettes.getRecette());
                                }
                            });
                            linearLayout.addView(info);

                            Button deleteButton = new Button(this);
                            deleteButton.setText("SUPPRIMER");
                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeRecette(listesRecettes.getRecette());
                                }
                            });

                            linearLayout.addView(deleteButton);

                            CheckBox addCart = new CheckBox(this);
                            if(liste.isCart()) {
                                addCart.setChecked(true);
                            }
                            addCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    boolean checked = addCart.isChecked();
                                    if(checked){
                                        addInCartRecette(listeSelect.getIdListe(), listesRecettes.getRecette());
                                    } else {
                                        removeInCartRecette(listeSelect.getIdListe(), listesRecettes.getRecette());
                                    }
                                }
                            });
                            linearLayout.addView(addCart);

                            containerListe.addView(linearLayout);

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

    public void addInCartProduit(int idListe, Produits produit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );

            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();

            for(ListesProduits listesProduit : listListesProduits) {
                if(produit.getIdProduit() == listesProduit.getProduit().getIdProduit()) {
                    Listes listes = daoListes.queryForId(idListe);
                    listes.setCart(true);
                    daoListes.update(listes);

                    reloadListeAfterChange();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeInCartProduit(int idListe, Produits produit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );

            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();

            for(ListesProduits listesProduit : listListesProduits) {
                if(produit.getIdProduit() == listesProduit.getProduit().getIdProduit()) {
                    Listes listes = daoListes.queryForId(idListe);
                    listes.setCart(false);
                    daoListes.update(listes);

                    reloadListeAfterChange();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addInCartRecette(int idListe, Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );

            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();

            for(ListesRecettes listesRecettes : listListesRecettes) {
                if(recette.getIdRecette() == listesRecettes.getRecette().getIdRecette()) {
                    Listes listes = daoListes.queryForId(idListe);
                    listes.setCart(true);
                    daoListes.update(listes);

                    reloadListeAfterChange();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void removeInCartRecette(int idListe, Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );

            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();

            for(ListesRecettes listesRecettes : listListesRecettes) {
                if(recette.getIdRecette() == listesRecettes.getRecette().getIdRecette()) {
                    Listes listes = daoListes.queryForId(idListe);
                    listes.setCart(false);
                    daoListes.update(listes);

                    reloadListeAfterChange();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void viewProduitInfo(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Recettes, Integer> daoRecettes = linker.getDao( Recettes.class );
            Dao<ContenirRecettes, Integer> daoContenirRecettes = linker.getDao( ContenirRecettes.class );

            List<ContenirRecettes> listContenirRecettes = daoContenirRecettes.queryForAll();

            AlertDialog.Builder infoRecettePopup = new AlertDialog.Builder(this);
            infoRecettePopup.setTitle("Produits dans la recette "+recette.getLibelleRecette()+" :");


            TableLayout tableLayout = new TableLayout(this);

            for(ContenirRecettes contenirRecette:listContenirRecettes) {
                Log.i(TAG, "viewProduitInfo: "+contenirRecette.getRecette().getIdRecette());
                if(recette.getIdRecette() == contenirRecette.getRecette().getIdRecette()) {

                    LinearLayout linearLayout = new LinearLayout(this);

                    Produits produit = contenirRecette.getProduit();

                    TextView libelle = new TextView(this);
                    libelle.setText("Libelle: "+produit.getLibelle() + " | Quantite: " + contenirRecette.getQuantite());

                    linearLayout.addView(libelle);
                    tableLayout.addView(linearLayout);

                }
            }

            infoRecettePopup.setView(tableLayout);

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

    public void removeProduit(Produits produit) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesProduits, Integer> daoListesProduits = linker.getDao( ListesProduits.class );

            List<ListesProduits> listListesProduits = daoListesProduits.queryForAll();
            List<Listes> listListes = daoListes.queryForAll();

            for(Listes listes : listListes) {
                for(ListesProduits listesProduit : listListesProduits) {
                    if(produit.getIdProduit() == listesProduit.getProduit().getIdProduit()) {
                        if(listes.getIdListe() == listesProduit.getListe().getIdListe()) {
                            Listes listeSelect = daoListes.queryForId(listesProduit.getListe().getIdListe());

                            daoListesProduits.delete(listesProduit);
                            daoListes.delete(listeSelect);

                            reloadListeAfterChange();
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }


    public void removeRecette(Recettes recette) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            Dao<ListesRecettes, Integer> daoListesRecettes = linker.getDao( ListesRecettes.class );

            List<ListesRecettes> listListesRecettes = daoListesRecettes.queryForAll();
            List<Listes> listListes = daoListes.queryForAll();


            for(Listes listes : listListes) {
                for (ListesRecettes listesRecettes : listListesRecettes) {
                    if (recette.getIdRecette() == listesRecettes.getRecette().getIdRecette()) {
                        if(listes.getIdListe() == listesRecettes.getListe().getIdListe()) {
                            daoListesRecettes.delete(listesRecettes);
                            daoListes.delete(listesRecettes.getListe());

                            reloadListeAfterChange();
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void editQuantite(Listes listes, int quantite) {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );

            listes.setQuantite(quantite);
            daoListes.update(listes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }


}

