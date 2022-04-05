package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Entity.Produits;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class ProduitsActivity extends AppCompatActivity {

    private static final String TAG = "ProduitsActivity";
    private TableLayout produitsTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits_main);
        produitsTableLayout = findViewById(R.id.produitsTableLayout);

        this.deleteDatabase("listeDeCourses.db");

        createData();

        getProduits();
    }

    public void createData() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao( Produits.class );
            Produits produits = new Produits();
            produits.setLibelle("Frommage");
            produits.setQuantite(5);
            produits.setPrix(12);
            daoProduits.create(produits);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void getProduits() {
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            List<Produits> produits = daoProduits.queryForAll();

            for (Produits produit: produits) {
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }
<<<<<<< Updated upstream
}

=======

    public void editProduit(int idProduit, TableRow tableRowProduit) {

    }

    public void deleteProduit(int idProduit, TableRow tableRowProduit) {
        /*DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Produits, Integer> daoProduits = linker.getDao(Produits.class);

            Produits produit = daoProduits.queryForId(idProduit);
            if (produit != null) {
                daoProduits.delete(produit);
                produitsTableLayout.removeView(tableRowProduit);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();*/
    }
}
>>>>>>> Stashed changes
