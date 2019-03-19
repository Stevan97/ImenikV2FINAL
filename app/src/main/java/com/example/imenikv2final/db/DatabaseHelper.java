package com.example.imenikv2final.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.imenikv2final.db.model.BrojeviTelefona;
import com.example.imenikv2final.db.model.Kontakt;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db.imenik.v2.final";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private Dao<Kontakt, Integer> getmKontaktDao = null;
    private Dao<BrojeviTelefona, Integer> getmBrojeviTelefona = null;

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Kontakt.class);
            TableUtils.createTable(connectionSource, BrojeviTelefona.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, BrojeviTelefona.class, true);
            TableUtils.dropTable(connectionSource, Kontakt.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Kontakt, Integer> getKontakti() throws SQLException {
        if (getmKontaktDao == null) {
            getmKontaktDao = getDao(Kontakt.class);
        }
        return getmKontaktDao;
    }

    public Dao<BrojeviTelefona, Integer> getBrojeviTelefona() throws SQLException {
        if (getmBrojeviTelefona == null) {
            getmBrojeviTelefona = getDao(BrojeviTelefona.class);
        }
        return getmBrojeviTelefona;
    }

    @Override
    public void close() {
        getmBrojeviTelefona = null;
        getmKontaktDao = null;
        super.close();
    }
}
