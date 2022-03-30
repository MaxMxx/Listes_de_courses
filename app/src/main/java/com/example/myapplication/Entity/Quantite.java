package com.example.myapplication.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Quantite")
public class Quantite {

    @DatabaseField( columnName = "idQuantite", generatedId = true )
    private int idQuantite;

    @DatabaseField( columnName="nbQuantite")
    private int nbQuantite;

    public Quantite() {
    }

    public Quantite(int nbQuantite) {
        this.nbQuantite = nbQuantite;
    }

    public int getIdQuantite() {
        return idQuantite;
    }

    public void setIdQuantite(int idQuantite) {
        this.idQuantite = idQuantite;
    }

    public int getNbQuantite() {
        return nbQuantite;
    }

    public void setNbQuantite(int nbQuantite) {
        this.nbQuantite = nbQuantite;
    }
}
