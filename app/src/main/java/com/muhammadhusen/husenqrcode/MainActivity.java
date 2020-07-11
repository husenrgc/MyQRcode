package com.muhammadhusen.husenqrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import android.widget.TextView;

import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonScan;
    private TextView textViewNama, textViewTelp,
            textViewKelas, textViewNim;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewKelas = (TextView) findViewById(R.id.textViewKelas);
        textViewNim = (TextView) findViewById(R.id.textViewNim);


        buttonScan.setOnClickListener(this);
        qrScan = new IntentIntegrator(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {            //jika qrcode tidak ada sama sekali

            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil SCAN tidak ada", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewNama.setText(obj.getString("nama"));
                    textViewKelas.setText(obj.getString("kelas"));
                    textViewNim.setText(obj.getString("nim"));



                                 } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
            if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            } else {
                try {
                    Uri geoIntent = Uri.parse(result.getContents() + "?z=11");
                    Intent visitGeo = new Intent(Intent.ACTION_VIEW, geoIntent);
                    visitGeo.setPackage("com.google.android.apps.maps");
                    startActivity(visitGeo);
                } catch (ActivityNotFoundException e) {
                    Log.d("Lokasi tidak ditemukan", result.getContents());
                }
            }

          if(Patterns.EMAIL_ADDRESS.matcher(result.getContents()).matches()){

              Uri mail =Uri.fromParts("mailto",result.getContents(), null);
              Intent emailIntent = new Intent(Intent.ACTION_SENDTO,mail);
              emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Judul Email");
              if (emailIntent.resolveActivity(getPackageManager()) != null) {
                  startActivity(Intent.createChooser(emailIntent,"Send email..."));
              }

          }
           if(Patterns.PHONE.matcher(result.getContents()).matches()) {



                   String qrcod = result.getContents();

                   Intent telp = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+qrcod));
                   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                       startActivity(telp);
                       return;
                   }
                   startActivity(telp);
               }
            }


        }




        @Override


    public void onClick(View view) {              qrScan.initiateScan();
    }
}



