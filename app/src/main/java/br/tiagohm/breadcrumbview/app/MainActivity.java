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
        bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.home).build());
        bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("tiagohm").build());
        bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("GitHub", "Documentos", "Download", "Imagens", "Música", "Fotos de Bichinhos Fofinhos").build());
        bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("Android", "Java", "C", "Arduino").build());
        bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("BreadCrumbView", "MarkdownView", "CodeView", "BlueDroid").build());
        bcv.setBreadCrumbListener(new BreadCrumbView.BreadCrumbListener() {
            @Override
            public void onItemClicked(BreadCrumbView view, BreadCrumbItem item, int level) {
                Toast.makeText(MainActivity.this, "pos: " + level + " " + item.getText(), Toast.LENGTH_SHORT).show();
                //view.removeItemsAfter(level);
            }

            @Override
            public boolean onItemValueChanged(BreadCrumbView view, BreadCrumbItem item, int level, Object oldSelectedItem, Object selectedItem) {
                Toast.makeText(MainActivity.this, "pos: " + level + " " + item.getText() + " old: " + oldSelectedItem + " new: " + selectedItem, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
