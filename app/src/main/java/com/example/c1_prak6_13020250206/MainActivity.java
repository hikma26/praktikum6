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
