package com.rkrzmail.oto.modules.rekening_bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.oto.modules.discount.SpotDiscount_Activity;
import com.rkrzmail.srv.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Map;

public class AturRekening_Activity extends AppActivity {

    private Spinner spBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_rekening_);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_atur_rekening);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Atur Rekening Bank");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        EditText etNoRek = findViewById(R.id.et_noRek_rekening);
        spBank = findViewById(R.id.sp_bank_rekening);
        EditText etNamaRek = findViewById(R.id.et_namaRek_rekening);

        find(R.id.cb_offUs_rekening, CheckBox.class);
        find(R.id.cb_edc_rekening, CheckBox.class);


        setSpinnerFromApi(spBank, "nama", "BANK", "viewmst", "BANK_NAME");

        find(R.id.btn_simpan_rekening, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void saveData() {
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;

            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturrekening"), args));
            }

            @Override
            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    startActivity(new Intent(getActivity(), SpotDiscount_Activity.class));
                } else {
                    showInfo("GAGAL");
                }
            }
        });

    }
}
