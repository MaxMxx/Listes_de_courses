package Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produits")
public class Produits {

    @DatabaseField( columnName = "idProduit", generatedId = true )
    private int idProduit;

    @DatabaseField( columnName="nom")
    private String nom;

    @DatabaseField( columnName="prenom")
    private String prenom;

    @DatabaseField( columnName="telephone")
    private String telephone;

    @DatabaseField( columnName="adresse")
    private String adresse;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idVille", foreignAutoCreate = true )
    private Ville ville;

}
