package br.tiagohm.breadcrumbview.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.tiagohm.breadcrumbview.BreadCrumbItem;
import br.tiagohm.breadcrumbview.BreadCrumbView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        BreadCrumbView bcv = findViewById(R.id.breadcrumbview);
        bcv.setSeparatorColor(0xCCFFFFFF);
        bcv.setTextColor(0xCCFFFFFF);
        bcv.addItem(new BreadCrumbItem(R.drawable.home));
        bcv.addItem(new BreadCrumbItem<>("tiagohm"));
        bcv.addItem(new BreadCrumbItem<>("GitHub"));
        bcv.addItem(new BreadCrumbItem<>("Android"));
        bcv.addItem(new BreadCrumbItem<>("BreadCrumbView"));
    }
}
