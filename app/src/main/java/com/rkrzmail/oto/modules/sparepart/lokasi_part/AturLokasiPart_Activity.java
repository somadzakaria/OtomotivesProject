package com.rkrzmail.oto.modules.sparepart.lokasi_part;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AturLokasiPart_Activity extends AppActivity {

    private static final String TAG = "Lokasi___";
    private static final int REQUEST_ATUR_LOKASI_PART = 3232;
    private EditText no_part, nama_part, merk_part;
    private Spinner sp_lokasi_part, sp_penempatan_part, sp_noRakPalet_part, sp_tinggiRak_part, sp_noFolder_part;
    private boolean isLokasi, isRak = false;
    private String noRak = "", noFolder = "", tinggkatRak = "", tempatPart = "", lokasiPart = "", penempatanPart = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_lokasi_part);
        initToolbar();
        Tools.setViewAndChildrenEnabled(find(R.id.ly_lokasiPart, LinearLayout.class), false);
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Atur Lokasi Part");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        no_part = findViewById(R.id.et_no_part);
        nama_part = findViewById(R.id.et_nama_part);
        merk_part = findViewById(R.id.et_merk_part);
        sp_lokasi_part = findViewById(R.id.sp_lokasiPart);
        sp_penempatan_part = findViewById(R.id.sp_penempatan_part);
        sp_noRakPalet_part = findViewById(R.id.sp_norakPalet_part);
        sp_tinggiRak_part = findViewById(R.id.sp_tinggiRak);
        sp_noFolder_part = findViewById(R.id.sp_noFolder_part);

        final Nson nonLokasi = Nson.readJson(getIntentStringExtra("NON_ALOKASI"));
        final Nson teralokasi = Nson.readJson(getIntentStringExtra("TERALOKASIKAN"));
        if (getIntent().hasExtra("NON_ALOKASI")) {
            isLokasi = false;
            find(R.id.btn_simpan_lokasi_part, Button.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData(nonLokasi);
                }
            });
        }else{
            isLokasi = true;
            Tools.setViewAndChildrenEnabled(find(R.id.ly_lokasiPart, LinearLayout.class), true);
            find(R.id.btn_simpan_lokasi_part, Button.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData(teralokasi);
                }
            });

            noRak = teralokasi.get("RAK").asString();
            tinggkatRak = teralokasi.get("").asString();
            noFolder = teralokasi.get("NO_FOLDER").asString();
            lokasiPart = teralokasi.get("LOKASI").asString();
            penempatanPart = teralokasi.get("PENEMPATAN").asString();
        }

        no_part.setText(isLokasi ? teralokasi.get("NOMOR_PART_NOMOR").asString() : nonLokasi.get("NO_PART").asString());
        nama_part.setText(isLokasi ? teralokasi.get("NAMA_MASTER").asString() : nonLokasi.get("NAMA_PART").asString());
        merk_part.setText(isLokasi ? teralokasi.get("MERK").asString() : nonLokasi.get("MERK").asString());
        Log.d(TAG, "initComponent: " + nonLokasi);
        spinnerView();
        sp_penempatan_part.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("PALET")) {
                    tempatPart = item;
                    sp_tinggiRak_part.setEnabled(false);
                } else if (item.equalsIgnoreCase("RAK")) {
                    tempatPart = item;
                    sp_tinggiRak_part.setEnabled(true);
                }else{
                    sp_tinggiRak_part.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateData(final Nson data) {
        String tinggirak = "";
        if(sp_tinggiRak_part.isEnabled()){
            tinggirak = sp_tinggiRak_part.getSelectedItem().toString().toUpperCase();
        }else{
            tinggirak = "0";
        }
        final String nofolder = sp_noFolder_part.getSelectedItem().toString().toUpperCase();
        final String rakOrPalet = sp_noRakPalet_part.getSelectedItem().toString();
        final String finalTinggirak = tinggirak;
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;
            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();

                args.put("action", "update");
                if(isLokasi){
                    args.put("lokasi", sp_lokasi_part.getSelectedItem().toString());
                    args.put("id", data.get("ID").asString());
                }
                args.put("tempat", tempatPart);
                if(tempatPart.equalsIgnoreCase("PALET")){
                    args.put("palet", rakOrPalet);
                    args.put("rak", "0");
                }else{
                    args.put("rak", rakOrPalet);
                    args.put("palet", "0");
                }
                args.put("folder", nofolder);
                args.put("tingkatrak", finalTinggirak);
                args.put("partid", data.get("PART_ID").asString());
                args.put("stock", data.get("STOCK").asString());
                args.put("tanggal", currentDateTime());
                args.put("kode", kodePenempatan(tempatPart, rakOrPalet, finalTinggirak, nofolder));

                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturlokasipart"), args));
            }

            @Override
            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    showSuccess("Berhasil Menempatkan Part");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showError(result.get("status").asString());
                }
            }
        });
    }

    private String kodePenempatan(String tempat, String no, String tingkat, String folder){
        String kode = "";
        if(tempat.equals("RAK")){
            kode = "R" + "." + no + "." + tingkat + "."  + folder;
        }else{
            kode = "P" + "." + no +  "." + folder;
        }
        return kode;
    }

    private void spinnerView() {
        List<String> noRakList = new ArrayList<String>();
        List<String> tinggiRakList = new ArrayList<String>();
        List<String> noFolderList = new ArrayList<String>();
        List<String> lokasiList = Arrays.asList(getResources().getStringArray(R.array.lokasi_simpan));
        List<String> penempatanList = Arrays.asList(getResources().getStringArray(R.array.penempatan_lokasi_part));
        for (int i = 1; i <= 100; i++) {
            if(!noRakList.contains(i)){
                noRakList.add(String.valueOf(i));
            }
            if(!noFolderList.contains(i)){
                noFolderList.add(String.valueOf(i));
            }
            if(i <= 10){
                tinggiRakList.add(String.valueOf(i));
            }
        }
        setSpinnerOffline(lokasiList, sp_lokasi_part, lokasiPart);
        setSpinnerOffline(penempatanList,sp_penempatan_part, penempatanPart);
        setSpinnerOffline(noRakList, sp_noRakPalet_part, noRak);
        setSpinnerOffline(tinggiRakList, sp_tinggiRak_part, tinggkatRak);
        setSpinnerOffline(noFolderList, sp_noFolder_part, noFolder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Nson nson = Nson.readJson(getIntentStringExtra("row"));
            no_part.setText(nson.get("NO_PART_ID").asString());
            nama_part.setText(nson.get("NAMA").asString());
            merk_part.setText(nson.get("MERK_PART").asString());
        }
    }
}
