package com.example.myapplication.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Listes")
public class Listes {

    @DatabaseField( columnName = "idListe", generatedId = true )
    private int idListe;

    @DatabaseField( columnName="quantite")
    private int quantite;

    public Listes() {
    }

    public Listes(int quantite) {
        this.quantite = quantite;
    }

    public int getIdListe() {
        return idListe;
    }

    public void setIdListe(int idListe) {
        this.idListe = idListe;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
