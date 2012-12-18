package com.example.Test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

public class MyActivity extends Activity {

    private String ean;
    private static int TAKE_PICTURE = 1;
    private static int SCAN_BARCOD = 49374;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();

        Button bt = (Button)findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE){
           //Register picture

        } else if(requestCode == SCAN_BARCOD){
            if (resultCode == RESULT_OK)
            {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (scanResult != null) {
                    ean = scanResult.getContents();

                    Document doc = null;
                    try {
                        doc = Jsoup.connect("http://www.amazon.fr/s/keywords="+ ean).get();

                        Element nom = doc.getElementsByAttributeValue("class","lrg bold").first();

                        if (nom.childNodes().size() > 0){
                            String url = nom.childNode(0).toString();
                            url = Html.fromHtml(url).toString();

                            Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this,"Produit introuvable",Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
