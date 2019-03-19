package com.example.imenikv2final.db.model;

import com.example.imenikv2final.db.DatabaseHelper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Kontakt.TABLE_KONTAKT)
public class Kontakt {

    public static final String TABLE_KONTAKT = "kontakt";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_ADRESA = "adresa";
    private static final String FIELD_SLIKA = "slika";
    private static final String FIELD_BROJEVI_TELEFONA = "brojeviTelefona";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_ADRESA)
    private String adresa;

    @DatabaseField(columnName = FIELD_SLIKA)
    private String slika;

    @ForeignCollectionField(columnName = FIELD_BROJEVI_TELEFONA, eager = true)
    private ForeignCollection<BrojeviTelefona> brojeviTelefona;

    public Kontakt() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public ForeignCollection<BrojeviTelefona> getBrojeviTelefona() {
        return brojeviTelefona;
    }

    public void setBrojeviTelefona(ForeignCollection<BrojeviTelefona> brojeviTelefona) {
        this.brojeviTelefona = brojeviTelefona;
    }

    public String toString() {
        return this.naziv;
    }

}
