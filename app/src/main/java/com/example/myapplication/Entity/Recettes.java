package com.example.myapplication.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Recettes")
public class Recettes {

    @DatabaseField( columnName = "idRecette", generatedId = true )
    private int idRecette;

    @DatabaseField( columnName="idLaRecette")
    private int idLaRecette;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idProduit", foreignAutoCreate = true )
    private Produits produit;

    public Recettes() {
    }

    public Recettes(int idLaRecette, Produits produit) {
        this.idLaRecette = idLaRecette;
        this.produit = produit;
    }


    public int getIdRecette() {
        return idRecette;
    }

    public void setIdRecette(int idRecette) {
        this.idRecette = idRecette;
    }

    public int getIdLaRecette() {
        return idLaRecette;
    }

    public void setIdLaRecette(int idLaRecette) {
        this.idLaRecette = idLaRecette;
    }

    public Produits getProduit() {
        return produit;
    }

    public void setProduit(Produits produit) {
        this.produit = produit;
    }
}
