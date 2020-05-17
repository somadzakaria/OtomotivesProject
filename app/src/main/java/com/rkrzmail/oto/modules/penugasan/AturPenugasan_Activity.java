package com.rkrzmail.oto.modules.penugasan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.MessageMsg;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.utils.Tools;

import java.util.Map;

public class AturPenugasan_Activity extends AppActivity implements View.OnClickListener {

    public static final String TAG = "AturPenugasan_Activity";
    private EditText etNama_Mekanik, etMulai_Kerja, etSelesai_Kerja, etMulai_istirahat, etSelesai_istirahat;
    private RadioGroup rg_status;
    private Spinner spTipe_antrian, spLokasi;
    private CheckBox cbHome, cbEmergency;

    /*
       Note : Update Methode belum work belum work
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_penugasan_);

        initToolbar();
        etNama_Mekanik = findViewById(R.id.et_namaMekanik);
        etMulai_Kerja = findViewById(R.id.et_mulaiKerja);
        etSelesai_Kerja = findViewById(R.id.et_selesaiKerja);
        etMulai_istirahat = findViewById(R.id.et_mulaistirahat);
        etSelesai_istirahat = findViewById(R.id.et_selesaistirahat);
        rg_status = findViewById(R.id.rg_status);
        spTipe_antrian = findViewById(R.id.sp_antrian);
        spLokasi = findViewById(R.id.sp_lokasi);
        cbHome = findViewById(R.id.cb_home);
        cbEmergency = findViewById(R.id.cb_emergency);

        etMulai_Kerja.setOnClickListener(this);
        etSelesai_Kerja.setOnClickListener(this);
        etMulai_istirahat.setOnClickListener(this);
        etSelesai_istirahat.setOnClickListener(this);

        final Nson data = Nson.readJson(getIntentStringExtra("ID"));
        final Intent i = getIntent();
        if(i.hasExtra("ID")){

            getSupportActionBar().setTitle("Edit Penugasan Mekanik");

            etNama_Mekanik.setText(data.get("NAMA_MEKANIK").asString());

            spLokasi.setSelection(Tools.getIndexSpinner(spLokasi, data.get("LOKASI").asString()));
            spTipe_antrian.setSelection(Tools.getIndexSpinner(spTipe_antrian, data.get("TIPE_ANTRIAN").asString()));
            etMulai_Kerja.setText(data.get("JAM_MASUK").asString());
            etSelesai_Kerja.setText(data.get("JAM_PULANG").asString());
//            etMulai_istirahat.setText(data.get("istirahat").asString());
//            etSelesai_istirahat.setText(data.get("selesai").asString());

//            if (data.get("status").asString().equalsIgnoreCase("ON")) {
//                find(R.id.rbOn, RadioButton.class).setChecked(true);
//            }
//            if (data.get("status").asString().equalsIgnoreCase("OFF")) {
//                find(R.id.rbOff, RadioButton.class).setChecked(true);
//            }
//            if (data.get("external").asString().equalsIgnoreCase("HOME")) {
//                cbHome.setChecked(true);
//            }
//            if (data.get("external").asString().equalsIgnoreCase("EMERGENCY")) {
//                cbEmergency.setChecked(true);
//            }
            Log.d(TAG, data.get("ID").asString());

            //gone visibility
            Tools.setViewAndChildrenEnabled(find(R.id.layout_penugasan, LinearLayout.class), false);
            find(R.id.layout_status, LinearLayout.class).setVisibility(View.GONE);
            find(R.id.layout_external, LinearLayout.class).setVisibility(View.GONE);
            find(R.id.layout_istirahat, LinearLayout.class).setVisibility(View.GONE);

            find(R.id.btn_hapus, Button.class).setVisibility(View.VISIBLE);
            find(R.id.btn_hapus, Button.class).setEnabled(true);
            find(R.id.btn_hapus, Button.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteData(Nson.readJson(getIntentStringExtra("ID")));
                }
            });
            find(R.id.btn_simpan, Button.class).setVisibility(View.GONE);
            find(R.id.btn_update, Button.class).setVisibility(View.VISIBLE);
            find(R.id.btn_update, Button.class).setEnabled(true);

            find(R.id.btn_update, Button.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Tools.setViewAndChildrenEnabled(find(R.id.layout_penugasan, LinearLayout.class), true);
                    find(R.id.layout_status, LinearLayout.class).setVisibility(View.VISIBLE);
                    find(R.id.layout_external, LinearLayout.class).setVisibility(View.VISIBLE);
                    find(R.id.layout_istirahat, LinearLayout.class).setVisibility(View.VISIBLE);
                    find(R.id.btn_hapus, Button.class).setVisibility(View.GONE);
                    find(R.id.btn_simpan, Button.class).setVisibility(View.VISIBLE);

                }
            });

            find(R.id.btn_simpan, Button.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData();
                }
            });

        }
        initComponent();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.et_mulaiKerja:
                Tools.getTimePickerDialog(getActivity(), etMulai_Kerja);
                break;
            case R.id.et_selesaiKerja:
                Tools.getTimePickerDialog(getActivity(), etSelesai_Kerja);
                break;
            case R.id.et_mulaistirahat:
                Tools.getTimePickerDialog(getActivity(), etMulai_istirahat);
                break;
            case R.id.et_selesaistirahat:
                Tools.getTimePickerDialog(getActivity(), etSelesai_istirahat);
                break;
        }
    }

    private void initComponent() {

        spLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Tenda")) {
                    Tools.setViewAndChildrenEnabled(find(R.id.layout_external), false);
                } else if (item.equalsIgnoreCase("Bengkel")) {
                    Tools.setViewAndChildrenEnabled(find(R.id.layout_external), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        find(R.id.btn_simpan, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etNama_Mekanik.getText().toString().equalsIgnoreCase("")) {
                    etNama_Mekanik.setError("Harus di isi");
                } else if (etMulai_Kerja.getText().toString().equalsIgnoreCase("")) {
                    find(R.id.et_mulaiKerja, EditText.class).setError("Harus di isi");
                } else if (etSelesai_Kerja.getText().toString().equalsIgnoreCase("")) {
                    etSelesai_Kerja.setError("Harus di isi");
                } else if (etMulai_istirahat.getText().toString().equalsIgnoreCase("")) {
                    etMulai_istirahat.setError("Harus di isi");
                } else if (etSelesai_istirahat.getText().toString().equalsIgnoreCase("")) {
                    etSelesai_istirahat.setError("Harus di isi");
                }
                insertData();
            }

        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_penugasan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Atur Penugasan Mekanik");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertData() {
        MessageMsg.showProsesBar(getActivity(), new Messagebox.DoubleRunnable() {
            Nson result;
            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();

                int selectedId = rg_status.getCheckedRadioButtonId();
                String nama = etNama_Mekanik.getText().toString().trim().toUpperCase();
                String antrian = spTipe_antrian.getSelectedItem().toString().toUpperCase();
                String lokasi = spLokasi.getSelectedItem().toString().toUpperCase();
                String masuk = etMulai_Kerja.getText().toString().trim();
                String selesai = etSelesai_Kerja.getText().toString().trim();
                String istirahat = etMulai_istirahat.getText().toString();
                String selesai_istirahat = etSelesai_istirahat.getText().toString();
                String home = cbHome.getText().toString();
                String emergency = cbEmergency.getText().toString();

                switch (selectedId) {
                    case R.id.rbOn:
                        String statusKerja = find(R.id.rbOn, RadioButton.class).getText().toString();
                        args.put("status", statusKerja);
                        Log.d(TAG, statusKerja);
                        break;
                    case R.id.rbOff:
                        String statusTidakKerja = find(R.id.rbOff, RadioButton.class).getText().toString();
                        args.put("status", statusTidakKerja);
                        Log.d(TAG, statusTidakKerja);
                        break;
                }

                args.put("namamekanik", nama);
                args.put("antrian", antrian);
                args.put("lokasi", lokasi);
                if (cbHome.isChecked()) {
                    args.put("external", home);
                    Log.d(TAG, home);
                } else if (cbEmergency.isChecked()) {
                    args.put("external", emergency);
                    Log.d(TAG, emergency);
                }
                args.put("masuk", masuk);
                args.put("pulang", selesai);
                args.put("istirahat", istirahat);
                args.put("selesai", selesai_istirahat);

                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturpenugasanmekanik"), args));
            }

            @Override
            public void runUI() {

                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    Log.d(TAG, "success add data" + result.get("status").asString());
                    startActivity(new Intent(AturPenugasan_Activity.this, PenugasanActivity.class));
                    finish();
                } else {
                    showError("Menambahkan data gagal!" + result.get("message").asString());
                    Log.d(TAG, "error");
                }
            }
        });
    }

    private void deleteData(final Nson nson) {
        newProses(new Messagebox.DoubleRunnable() {
            Nson data;
            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();
                args.put("action", "delete");
                args.put("id", nson.get("id").asString());
                data = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturpenugasanmekanik"), args));
            }

            @Override
            public void runUI() {
                if (data.get("status").asString().equalsIgnoreCase("OK")) {
                    Log.d(TAG, "success delete data" + data.get("ID").asString());
                    setResult(RESULT_OK);
                    startActivity(new Intent(AturPenugasan_Activity.this, PenugasanActivity.class));

                }else{
                    showError("Mohon Di Coba Kembali");
                }

            }
        });
    }

    private void updateData(){
        newProses(new Messagebox.DoubleRunnable() {
            Nson data;
            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();

                int selectedId = rg_status.getCheckedRadioButtonId();
                String nama = etNama_Mekanik.getText().toString().trim().toUpperCase();
                String antrian = spTipe_antrian.getSelectedItem().toString().toUpperCase();
                String lokasi = spLokasi.getSelectedItem().toString().toUpperCase();
                String masuk = etMulai_Kerja.getText().toString().trim();
                String selesai = etSelesai_Kerja.getText().toString().trim();
                String istirahat = etMulai_istirahat.getText().toString();
                String selesai_istirahat = etSelesai_istirahat.getText().toString();
                String home = cbHome.getText().toString();
                String emergency = cbEmergency.getText().toString();

                args.put("action", "update");
                switch (selectedId) {
                    case R.id.rbOn:
                        String statusKerja = find(R.id.rbOn, RadioButton.class).getText().toString();
                        args.put("status", statusKerja);
                        Log.d(TAG, statusKerja);
                        break;
                    case R.id.rbOff:
                        String statusTidakKerja = find(R.id.rbOff, RadioButton.class).getText().toString();
                        args.put("status", statusTidakKerja);
                        Log.d(TAG, statusTidakKerja);
                        break;
                }
                args.put("namamekanik", nama);
                args.put("antrian", antrian);
                args.put("lokasi", lokasi);
                if (cbHome.isChecked()) {
                    args.put("external", home);
                    Log.d(TAG, home);
                } else if (cbEmergency.isChecked()) {
                    args.put("external", emergency);
                    Log.d(TAG, emergency);
                }
                args.put("masuk", masuk);
                args.put("pulang", selesai);
                args.put("istirahat", istirahat);
                args.put("selesai", selesai_istirahat);
                data = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturpenugasanmekanik"), args));
            }

            @Override
            public void runUI() {
                if (data.get("status").asString().equalsIgnoreCase("OK")) {
                    Log.d(TAG, "success update data" + data.get("status").asString());
                    setResult(RESULT_OK);
                    Intent i = new Intent(AturPenugasan_Activity.this, PenugasanActivity.class);
                    startActivityForResult(i, RESULT_OK);
                }else{
                    showError("Mohon Di Coba Kembali");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}