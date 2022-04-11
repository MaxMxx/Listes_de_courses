package com.example.myapplication.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Listes")
public class Listes {

    @DatabaseField( columnName = "idListe", generatedId = true )
    private int idListe;

    @DatabaseField( columnName="quantite")
    private int quantite;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idRecette", foreignAutoCreate = true )
    private Recettes recette;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idProduit", foreignAutoCreate = true )
    private Produits produit;

    public Listes() {
    }

    public Listes(Recettes recette, Produits produit, int quantite) {
        this.recette = recette;
        this.produit = produit;
        this.quantite = quantite;
    }

    public int getIdListe() {
        return idListe;
    }

    public void setIdListe(int idListe) {
        this.idListe = idListe;
    }

    public Recettes getRecette() {
        return recette;
    }

    public void setRecette(Recettes recette) {
        this.recette = recette;
    }

    public Produits getProduit() {
        return produit;
    }

    public void setProduit(Produits produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
