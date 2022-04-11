package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Entity.Listes;
import com.example.myapplication.Entity.Produits;
import com.example.myapplication.Entity.Recettes;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    private TableLayout containerListe;
    private Button buttonSupprimerListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste);
        this.deleteDatabase("listeDeCourses.db");
        DataBaseLinker linker = new DataBaseLinker(this);
        containerListe = findViewById(R.id.container_liste);
        buttonSupprimerListe = findViewById(R.id.button_supprimer_liste);
        Dao<Listes, Integer> daoListes = null;
        try {
            daoListes = linker.getDao( Listes.class );
            Produits produit = new Produits("Pizza",4,"image",45);
            Recettes recette = new Recettes(1,produit);
            Listes test = new Listes(1,recette,produit,45);
            daoListes.create(test);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        createListes();

    }

    private void createListes(){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Listes, Integer> daoListes = linker.getDao( Listes.class );
            List<Listes> listes = daoListes.queryForAll();//récupérer tableau de maxence
            Log.i("test", "test"+listes);
            for (Listes liste : listes) {
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);


                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );
                /*Produits p = new Produits ;
                TextView labelNom = new TextView(this);
                labelNom.setLayoutParams(param);
                labelNom.setText(liste.getProduit());
                row.addView(labelNom);
                containerListe.addView(row);*/



            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }
}

