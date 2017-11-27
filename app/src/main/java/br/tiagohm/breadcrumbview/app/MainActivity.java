package br.tiagohm.breadcrumbview.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
        bcv.addItem(new BreadCrumbItem(R.drawable.folder, "tiagohm"));
        bcv.addItem(new BreadCrumbItem(R.drawable.folder, "GitHub"));
        bcv.addItem(new BreadCrumbItem(R.drawable.folder, "Android"));
        bcv.addItem(new BreadCrumbItem(R.drawable.folder, "BreadCrumbView"));
        bcv.setBreadCrumbListener(new BreadCrumbView.BreadCrumbListener() {
            @Override
            public void onItemClicked(BreadCrumbView view, BreadCrumbItem item, int level) {
                Toast.makeText(MainActivity.this, "pos: " + level + " " + item.getText(), Toast.LENGTH_SHORT).show();
                view.removeItemsAfter(level);
            }
        });
    }
}
