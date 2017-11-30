package br.tiagohm.breadcrumbview;


import android.support.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreadCrumbItem<T> {

    private int icon = 0;
    private List<T> itens;
    private int selectedIndex;

    private BreadCrumbItem(Builder<T> builder) {
        this.icon = builder.icon;
        this.itens = builder.itens;
        this.selectedIndex = builder.selectedIndex;
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

    public String getText() {
        return getSelectedItem() != null ? getSelectedItem().toString() : null;
    }

    public static class Builder<T> {
        private int icon = 0;
        private List<T> itens = new ArrayList<>();
        private int selectedIndex = -1;

        public Builder<T> icon(int icon) {
            this.icon = icon;
            return this;
        }

        public Builder<T> itens(List<T> itens) {
            this.itens = itens;
            selectedIndex = itens != null && itens.size() > 0 ? 0 : -1;
            return this;
        }

        public Builder<T> itens(T... itens) {
            return itens(Arrays.asList(itens));
        }

        public Builder<T> selectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
            return this;
        }

        public BreadCrumbItem<T> build() {
            return new BreadCrumbItem<T>(this);
        }
    }
}
