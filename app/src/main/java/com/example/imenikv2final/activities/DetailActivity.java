package com.example.imenikv2final.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imenikv2final.R;
import com.example.imenikv2final.adapters.BrojeviTelefonaAdapter;
import com.example.imenikv2final.db.DatabaseHelper;
import com.example.imenikv2final.db.model.BrojeviTelefona;
import com.example.imenikv2final.db.model.Kontakt;
import com.example.imenikv2final.dialogs.AboutDialog;
import com.example.imenikv2final.model.NavigationItems;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String[] tipBrojeva = null;

    private static final String NOTIFY_MESSAGE = "notify";
    private static final String TOAST_MESSAGE = "toast";
    private static final int SELECT_PICTURE = 1;
    private static final int MY_PERMISSION_REQUEST_MEDINA = 1;

    private int position = 1;
    private DatabaseHelper databaseHelper = null;
    private Kontakt kontakt = null;
    private BrojeviTelefona brojeviTelefona = null;
    private BrojeviTelefonaAdapter brojeviTelefonaAdapter = null;

    private ForeignCollection<BrojeviTelefona> brojeviForeignCollection = null;
    private List<BrojeviTelefona> brojeviTelefonaList = null;
    private ListView listViewDetail = null;

    private String tip = null;
    private Spinner spinner = null;
    private SpinnerAdapter spinnerAdapter = null;

    private SharedPreferences sharedPreferences = null;
    private boolean showMessage = false;
    private boolean showNotify = false;

    private Spannable message1 = null;
    private Spannable message2 = null;
    private Spannable message3 = null;
    private Toast toast = null;
    private View toastView = null;
    private TextView textToast = null;

    private Intent intentPosition = null;
    private int idPosition = 0;

    private String imagePath = null;
    private ImageView preview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_DETAIL);
        setSupportActionBar(toolbar);

        prikaziDetaljeKontakta();

    }

    private void prikaziDetaljeKontakta() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            kontakt = getDatabaseHelper().getKontakti().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView naziv = findViewById(R.id.detail_text_view_naziv);
        message1 = new SpannableString("Naziv Kontakta: ");
        message2 = new SpannableString(kontakt.getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView adresa = findViewById(R.id.detail_text_view_adresa);
        message1 = new SpannableString("Adresa Kontakta: ");
        message2 = new SpannableString(kontakt.getAdresa());
        spannableStyle();
        adresa.setText(message1);
        adresa.append(message2);

        final ImageView imageView = findViewById(R.id.detail_image_view);
        imageView.setImageURI(Uri.parse(kontakt.getSlika()));

        listViewDetail = findViewById(R.id.list_view_detail);
        try {
            brojeviForeignCollection = getDatabaseHelper().getKontakti().queryForId(idPosition).getBrojeviTelefona();
            brojeviTelefonaList = new ArrayList<>(brojeviForeignCollection);
            brojeviTelefonaAdapter = new BrojeviTelefonaAdapter(this, brojeviTelefonaList);
            listViewDetail.setAdapter(brojeviTelefonaAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void dodajBroj() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dodaj_broj);
        dialog.show();

        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            kontakt = getDatabaseHelper().getKontakti().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tipBrojeva = getResources().getStringArray(R.array.telefoni);

        spinner = dialog.findViewById(R.id.dodaj_broj_spinner);
        spinnerAdapter = new ArrayAdapter<>(DetailActivity.this, android.R.layout.simple_spinner_item, tipBrojeva);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tip = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText editBroj = dialog.findViewById(R.id.dodaj_broj_broj);

        Button confirm = dialog.findViewById(R.id.dodaj_broj_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editBroj.getText().toString().isEmpty() || editBroj.getText().toString().length() < 5) {
                    editBroj.setError("Broj mora biti duzi od 5 !");
                    return;
                }
                if (editBroj.getText().toString().length() > 10) {
                    editBroj.setError("Ne sme biti veci od 10");
                    return;
                }

                int broj = Integer.parseInt(editBroj.getText().toString());

                brojeviTelefona = new BrojeviTelefona();
                brojeviTelefona.setBroj(broj);
                brojeviTelefona.setTipBroja(tip);
                brojeviTelefona.setKontakt(kontakt);

                try {
                    getDatabaseHelper().getBrojeviTelefona().create(brojeviTelefona);
                    osveziListuBrojeva();
                    dialog.dismiss();

                    message1 = new SpannableString("Uspesno kreiran broj: ");
                    message2 = new SpannableString(String.valueOf(brojeviTelefona.getBroj()));
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Kreirani broj: " + brojeviTelefona.getBroj());
                        message3.setSpan(new ForegroundColorSpan(Color.RED), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        Button cancel = dialog.findViewById(R.id.dodaj_broj_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void izbrisiKontakt() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            kontakt = getDatabaseHelper().getKontakti().queryForId(idPosition);
            brojeviForeignCollection = getDatabaseHelper().getKontakti().queryForId(idPosition).getBrojeviTelefona();
            brojeviTelefonaList = new ArrayList<>(brojeviForeignCollection);
            getDatabaseHelper().getBrojeviTelefona().delete(brojeviTelefonaList);
            getDatabaseHelper().getKontakti().delete(kontakt);
            onBackPressed();

            message1 = new SpannableString("Uspesno Izbrisan Kontakt: ");
            message2 = new SpannableString(kontakt.getNaziv());
            spannableStyle();

            if (showMessage) {
                toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                toastView = toast.getView();

                textToast = toastView.findViewById(android.R.id.message);
                textToast.setText(message1);
                textToast.append(message2);
                toast.show();
            }
            if (showNotify) {
                message3 = new SpannableString("Izbrisan kontakt: " + kontakt.getNaziv());
                message3.setSpan(new ForegroundColorSpan(Color.RED), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                showstatusBar2(message3);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void izmenaKontakta() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izmena_kontakta);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button gallery = dialog.findViewById(R.id.izmena_kontakt_button_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelfPermission();
                preview = dialog.findViewById(R.id.izmena_kontakt_preview);
                select_picture();
            }
        });

        final EditText editNaziv = dialog.findViewById(R.id.izmena_naziv);
        final EditText editAdresa = dialog.findViewById(R.id.izmena_adresa);

        Button confirm = dialog.findViewById(R.id.izmena_kontakt_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje naziv ne sme biti prazno !");
                    return;
                }
                if (editAdresa.getText().toString().isEmpty()) {
                    editAdresa.setError("Polje adresa ne sme biti prazno !");
                    return;
                }
                if (imagePath == null || imagePath.isEmpty() || preview == null) {
                    Toast.makeText(DetailActivity.this, "Morate odabrati sliku", Toast.LENGTH_LONG).show();
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String adresa = editAdresa.getText().toString();

                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");

                try {
                    kontakt = getDatabaseHelper().getKontakti().queryForId(idPosition);

                    kontakt.setNaziv(naziv);
                    kontakt.setAdresa(adresa);
                    kontakt.setSlika(imagePath);

                    getDatabaseHelper().getKontakti().update(kontakt);
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(0,0);
                    resetImage();

                    message1 = new SpannableString("Izmena Uspesna");
                    message2 = new SpannableString("Izmena Uspesna");
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        showstatusBar2(message1);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancel = dialog.findViewById(R.id.izmena_kontakt_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void osveziListuBrojeva() {
        listViewDetail = findViewById(R.id.list_view_detail);
        if (listViewDetail != null) {
            brojeviTelefonaAdapter = (BrojeviTelefonaAdapter) listViewDetail.getAdapter();
            if (brojeviTelefonaAdapter != null) {
                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");
                try {
                    brojeviForeignCollection = getDatabaseHelper().getKontakti().queryForId(idPosition).getBrojeviTelefona();
                    brojeviTelefonaList = new ArrayList<>(brojeviForeignCollection);
                    brojeviTelefonaAdapter.refreshList(brojeviTelefonaList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void consultPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
        showNotify = sharedPreferences.getBoolean(NOTIFY_MESSAGE, false);
    }

    @Override
    protected void onResume() {
        osveziListuBrojeva();
        consultPreferences();
        super.onResume();
    }

    private void resetImage() {
        imagePath = "";
        preview = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_detail_delete:
                izbrisiKontakt();
                break;
            case R.id.menu_detail_update:
                izmenaKontakta();
                break;
            case R.id.menu_detail_add_broj:
                dodajBroj();
                break;
            case R.id.menu_detail_overflow:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_detail_dialog:
                AlertDialog aboutDialog = new AboutDialog(DetailActivity.this).prepareDialog();
                aboutDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

        super.onDestroy();
    }

    private void select_picture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void checkSelfPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_MEDINA);
        } else {
            // if not accepted, do something
        }

    }

    /**
     * <- Metoda za ucitavanje slike * Cuvanje putanje do slike. * ->
     */
    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    if (selectedImageUri != null) {
                        imagePath = getImagePath(selectedImageUri);
                    }
                    if (preview != null) {
                        preview.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showstatusBar2(Spannable string) {


        NotificationChannel notificationChannel = new NotificationChannel("NOTIFY_ID", "ReserveNotify", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.GREEN);

        message1 = new SpannableString("Uspesno! ");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);
        builder.setChannelId("NOTIFY_ID");

        nm.notify(1, builder.build());
    }

}
