package com.example.imenikv2final.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = BrojeviTelefona.TABLE_BROJEVI)
public class BrojeviTelefona {

    public static final String TABLE_BROJEVI = "tabelaBrojevi";
    private static final String FIELD_ID = "id";
    private static final String FIELD_BROJ = "broj";
    private static final String FIELD_TIP = "tipBroja";
    private static final String FIELD_KONTAKT = "kontakt";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_BROJ)
    private int broj;

    @DatabaseField(columnName = FIELD_TIP)
    private String tipBroja;

    @DatabaseField(columnName = FIELD_KONTAKT, foreignAutoRefresh = true, foreign = true)
    private Kontakt kontakt;

    public BrojeviTelefona() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBroj() {
        return broj;
    }

    public void setBroj(int broj) {
        this.broj = broj;
    }

    public String getTipBroja() {
        return tipBroja;
    }

    public void setTipBroja(String tipBroja) {
        this.tipBroja = tipBroja;
    }

    public Kontakt getKontakt() {
        return kontakt;
    }

    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    public String toString() {
        String text;
        text = "Broj: " + broj + "Tip: " + tipBroja;
        return text;
    }
}
