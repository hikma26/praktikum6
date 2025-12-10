package com.example.c1_prak6_13020250206;

public class MainActivity extends AppCompatActivity {

    private EditText txtStb, txtNama, txtAngkatan;
    private RestHelper restHelper;
    private Mahasiswa mhs;
    private Intent intentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restHelper = new RestHelper(this);
        intentEdit = null;

        txtStb = findViewById(R.id.txt_stb);
        txtNama = findViewById(R.id.txt_nama);
        txtAngkatan = findViewById(R.id.txt_angkatan);
    }

    private void clearData() {
        txtStb.setText("");
        txtNama.setText("");
        txtAngkatan.setText("");
        intentEdit = null;
        txtStb.requestFocus();
    }

    public void btnSimpanClick(View view) {
        mhs = new Mahasiswa(
                txtStb.getText().toString(),
                txtNama.getText().toString(),
                Integer.parseInt(txtAngkatan.getText().toString())
        );

        try {
            if (intentEdit == null)
                restHelper.insertData(mhs.toJSON());
            else
                restHelper.editData(
                        intentEdit.getStringExtra("stb"),
                        new Mahasiswa(
                                txtStb.getText().toString(),
                                txtNama.getText().toString(),
                                Integer.parseInt(txtAngkatan.getText().toString())
                        )
                );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        clearData();
    }

    public void btnTampilDataClick(View view) {
        intentEdit = null;
        Intent intent = new Intent(this, TampilDataActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            intentEdit = data;
            txtStb.setText(data.getStringExtra("stb"));
            txtNama.setText(data.getStringExtra("nama"));
            txtAngkatan.setText(data.getStringExtra("angkatan"));
        }
    }
}

public class TampilDataActivity extends AppCompatActivity {

    private TableLayout tblMhs;
    private TableRow tr;
    private TextView col1, col2, col3;
    private RestHelper restHelper;
    private String stb, nama, angkatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);

        restHelper = new RestHelper(this);
        tblMhs = findViewById(R.id.tbl_mhs);

        tampilData();
    }

    private void tampilTblMhs(ArrayList<Mahasiswa> arrListMhs) {
        tblMhs.removeAllViews();

        tr = new TableRow(this);
        col1 = new TextView(this);
        col2 = new TextView(this);
        col3 = new TextView(this);

        col1.setText("Stambuk");
        col2.setText("Nama Mahasiswa");
        col3.setText("Angkatan");

        col1.setWidth(200);
        col2.setWidth(300);
        col3.setWidth(150);

        tr.addView(col1);
        tr.addView(col2);
        tr.addView(col3);
        tblMhs.addView(tr);

        for (final Mahasiswa mhs : arrListMhs) {
            tr = new TableRow(this);
            col1 = new TextView(this);
            col2 = new TextView(this);
            col3 = new TextView(this);

            col1.setText(mhs.getStb());
            col2.setText(mhs.getNama());
            col3.setText(String.valueOf(mhs.getAngkatan()));

            col1.setWidth(200);
            col2.setWidth(300);
            col3.setWidth(150);

            tr.addView(col1);
            tr.addView(col2);
            tr.addView(col3);

            tblMhs.addView(tr);

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stb = mhs.getStb();
                    nama = mhs.getNama();
                    angkatan = String.valueOf(mhs.getAngkatan());
                }
            });
        }
    }

    private void tampilData() {
        restHelper.getDataMhs(new RestCallbackMahasiswa() {
            @Override
            public void requestDataMhsSuccess(ArrayList<Mahasiswa> arrayList) {
                tampilTblMhs(arrayList);
            }
        });
    }

    public void btnEditClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("stb", stb);
        intent.putExtra("nama", nama);
        intent.putExtra("angkatan", angkatan);
        setResult(1, intent);
        finish();
    }

    public void btnHapusClick(View view) {
        if (stb == null) return;

        restHelper.hapusData(stb, new RestCallbackMahasiswa() {
            @Override
            public void requestDataMhsSuccess(ArrayList<Mahasiswa> arrayList) {
                tampilTblMhs(arrayList);
            }
        });
    }
}

