package com.example.myapplication.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produits")
public class Produits {

    @DatabaseField( columnName = "idProduit", generatedId = true )
    private int idProduit;

    @DatabaseField( columnName="libelle")
    private String libelle;

    @DatabaseField( columnName="prix")
    private float prix;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idQuantite", foreignAutoCreate = true )
    private Quantite quantite;

    public Produits() {
    }

    public Produits(String libelle, float prix, Quantite quantite) {
        this.libelle = libelle;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Quantite getQuantite() {
        return quantite;
    }

    public void setQuantite(Quantite quantite) {
        this.quantite = quantite;
    }
}
