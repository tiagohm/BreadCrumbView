# BreadCrumbView

[![](https://jitpack.io/v/tiagohm/BreadCrumbView.svg)](https://jitpack.io/#tiagohm/BreadCrumbView)

## Dependencies

Adicione ao seu projeto:
```gradle
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
```gradle
	dependencies {
	        compile 'com.github.tiagohm:BreadCrumbView:VERSION'
	}
```

## How to use

```xml
<br.tiagohm.breadcrumbview.BreadCrumbView
    android:id="@+id/breadcrumbview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/colorPrimary"
    app:bcv_separatorColor="#DFFF"
    app:bcv_textColor="#DFFF"
    app:bcv_textSize="16sp"/>
```

```java
BreadCrumbView bcv = findViewById(R.id.breadcrumbview);
bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.home).build());
    bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("tiagohm").build());
    bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("GitHub", "Documentos", "Download", "Imagens", "Música").build());
    bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("Android", "Java", "C", "Arduino").build());
    bcv.addItem(new BreadCrumbItem.Builder().icon(R.drawable.folder).itens("BreadCrumbView", "MarkdownView", "CodeView", "BlueDroid").build());        
```

```java
bcv.setBreadCrumbListener(new BreadCrumbView.BreadCrumbListener() {
    @Override
    public void onItemClicked(BreadCrumbView view, BreadCrumbItem item, int level) {
        Toast.makeText(MainActivity.this, "pos: " + level + " " + item.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemValueChanged(BreadCrumbView view, BreadCrumbItem item, int level, Object oldSelectedItem, Object selectedItem) {
        Toast.makeText(MainActivity.this, "pos: " + level + " " + item.getText() + " old: " + oldSelectedItem + " new: " + selectedItem, Toast.LENGTH_SHORT).show();
        return true; //to apply the change of selected item
    }
        });
```
