package br.tiagohm.breadcrumbview;


import android.support.annotation.DrawableRes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BreadCrumbItem<T> {

    private int icon = 0;
    private List<T> itens;
    private int selectedIndex;

    public BreadCrumbItem(int icon) {
        this.icon = icon;
        this.selectedIndex = -1;
        this.itens = new LinkedList<>();
    }

    public BreadCrumbItem(List<T> itens) {
        this(itens.size() > 0 ? 0 : -1, itens);
    }

    public BreadCrumbItem(int selectedIndex, List<T> itens) {
        this.selectedIndex = selectedIndex;
        this.itens = itens;
    }

    public BreadCrumbItem(T... itens) {
        this(Arrays.asList(itens));
    }

    public BreadCrumbItem(int selectedIndex, T... itens) {
        this(selectedIndex, Arrays.asList(itens));
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public T getSelectedItem() {
        return getSelectedIndex() >= 0 ? itens.get(getSelectedIndex()) : null;
    }

    public void setSelectedItem(T item) {
        selectedIndex = itens.indexOf(item);
    }

    public List<T> getItens() {
        return itens;
    }
}
