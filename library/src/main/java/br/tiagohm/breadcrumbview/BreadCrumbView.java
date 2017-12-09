package br.tiagohm.breadcrumbview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class BreadCrumbView<T> extends FrameLayout {

    public interface BreadCrumbListener<T> {

        void onItemClicked(BreadCrumbView<T> view, BreadCrumbItem<T> item, int level);

        boolean onItemValueChanged(BreadCrumbView<T> view, BreadCrumbItem<T> item, int level, T oldSelectedItem, T selectedItem);
    }

    private RecyclerView mBreadCrumb;
    private BreadCrumbAdapter mAdapter;
    private List<BreadCrumbItem<T>> itens = new LinkedList<>();
    private int textColor = Color.WHITE;
    private int separatorColor = Color.WHITE;
    private float textSize = 30;
    private BreadCrumbListener<T> listener;

    public BreadCrumbView(@NonNull Context context) {
        this(context, null);
    }

    public BreadCrumbView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreadCrumbView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //Atributos.
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BreadCrumbView, defStyleAttr, 0);
            textColor = a.getColor(R.styleable.BreadCrumbView_bcv_textColor, Color.WHITE);
            separatorColor = a.getColor(R.styleable.BreadCrumbView_bcv_separatorColor, Color.WHITE);
            textSize = a.getDimensionPixelSize(R.styleable.BreadCrumbView_bcv_textSize, 30);
            a.recycle();
        }

        //Adiciona a view.
        addView(LayoutInflater.from(context).inflate(R.layout.view_breadcrumb, this, false));
        //Views.
        mBreadCrumb = findViewById(R.id.breadcrumb);
        LinearLayoutManager llm = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
        mBreadCrumb.setLayoutManager(llm);
        mBreadCrumb.setOverScrollMode(OVER_SCROLL_NEVER);
        mBreadCrumb.setAdapter(mAdapter = new BreadCrumbAdapter(this));
    }

    public void setBreadCrumbListener(BreadCrumbListener<T> listener) {
        this.listener = listener;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        update();
    }

    public int getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(@ColorInt int separatorColor) {
        this.separatorColor = separatorColor;
        update();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        update();
    }

    public List<BreadCrumbItem<T>> getItens() {
        return itens;
    }

    public void addItem(@NonNull BreadCrumbItem<T> item) {
        int oldSize = itens.size();
        itens.add(item);
        mAdapter.notifyDataSetChanged();
        smoothScrollToEndPosition();
    }

    public void removeItemsAfter(int level) {
        if (level < itens.size()) {
            while (itens.size() > level) {
                itens.remove(itens.size() - 1);
            }
            mAdapter.notifyDataSetChanged();
            smoothScrollToEndPosition();
        }
    }

    public void removeLastItem() {
        removeItemsAfter(itens.size() - 1);
    }

    private void smoothScrollToEndPosition() {
        smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void smoothScrollToStartPosition() {
        smoothScrollToPosition(0);
    }

    private void smoothScrollToPosition(final int position) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBreadCrumb.smoothScrollToPosition(position);
            }
        }, 500);
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }

    private static class BreadCrumbAdapter<T> extends RecyclerView.Adapter<BreadCrumbAdapter.ItemHolder> {

        private BreadCrumbView<T> breadCrumbView;

        public BreadCrumbAdapter(BreadCrumbView<T> breadCrumbView) {
            this.breadCrumbView = breadCrumbView;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            if (viewType == R.layout.view_separator_item) {
                return new SeparatorIconHolder(li.inflate(viewType, parent, false));
            } else {
                return new BreadCrumItemHolder(li.inflate(viewType, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(BreadCrumbAdapter.ItemHolder holder, int position) {
            if (holder instanceof BreadCrumbAdapter.SeparatorIconHolder) {
                holder.setItem(breadCrumbView.itens.get((position - 1) / 2 + 1));
            } else {
                holder.setItem(breadCrumbView.itens.get(position / 2));
            }
        }

        @Override
        public int getItemCount() {
            return breadCrumbView.itens.size() * 2 - 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 1 ? R.layout.view_separator_item : R.layout.view_breadcrumb_item;
        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            public ItemHolder(View itemView) {
                super(itemView);
            }

            public void setItem(BreadCrumbItem<?> item) {
            }

            public Context getContext() {
                return breadCrumbView.getContext();
            }
        }

        public class SeparatorIconHolder extends ItemHolder {

            private ImageView icon;

            public SeparatorIconHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.breadcrumb_item_separator);
            }

            @Override
            public void setItem(BreadCrumbItem<?> item) {
                icon.setColorFilter(breadCrumbView.getSeparatorColor(), PorterDuff.Mode.SRC_ATOP);
                icon.setImageAlpha(Color.alpha(breadCrumbView.getSeparatorColor()));
            }
        }

        public class BreadCrumItemHolder extends ItemHolder {

            private ImageView icon;
            private TextView text;
            private ListPopupWindow popupWindow;

            public BreadCrumItemHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.breadcrumb_item_icon);
                text = itemView.findViewById(R.id.breadcrumb_item_text);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (breadCrumbView.listener != null) {
                            BreadCrumbItem item = (BreadCrumbItem) view.getTag();
                            breadCrumbView.listener.onItemClicked(breadCrumbView, item, breadCrumbView.itens.indexOf(item));
                        }
                    }
                });
                itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        popupWindow.show();
                        return true;
                    }
                });
                createPopupWindow();
            }

            @Override
            public void setItem(BreadCrumbItem<?> item) {
                itemView.setTag(item);
                //Seta o ícone.
                if (item.getIcon() != 0) {
                    icon.setVisibility(VISIBLE);
                    icon.setImageResource(item.getIcon());
                    icon.setColorFilter(breadCrumbView.getTextColor(), PorterDuff.Mode.SRC_ATOP);
                    icon.setImageAlpha(Color.alpha(breadCrumbView.getTextColor()));
                } else {
                    icon.setVisibility(GONE);
                }
                //Seta o texto.
                final String texto = item.getText();
                if (texto != null) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) icon.getLayoutParams();
                    float d = icon.getContext().getResources().getDisplayMetrics().density;
                    lp.setMarginEnd((int) (5 * d));
                    icon.setLayoutParams(lp);
                    text.setText(texto);
                    text.setTextColor(breadCrumbView.getTextColor());
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, breadCrumbView.textSize);
                } else {
                    text.setText(null);
                }
                popupWindow.setAdapter(null);
                //Preenche o popup.
                if (item.getItens().size() > 1) {
                    ListAdapter adapter = new ArrayAdapter(getContext(), R.layout.view_breadcrumb_dropdown_item, android.R.id.text1, item.getItens()) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            TextView textView = v.findViewById(android.R.id.text1);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, breadCrumbView.textSize);
                            return v;
                        }
                    };
                    popupWindow.setAdapter(adapter);
                    //Calcula a largura ideal do popup baseado no comprimento do texto dos itens.
                    final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    View itemView = null;
                    int maxWidth = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        itemView = adapter.getView(i, itemView, null);
                        itemView.measure(widthMeasureSpec, heightMeasureSpec);
                        maxWidth = Math.max(maxWidth, itemView.getMeasuredWidth());
                    }
                    //Seta a largura ideal do popup.
                    popupWindow.setWidth(maxWidth);
                }
            }

            private void createPopupWindow() {
                popupWindow = new ListPopupWindow(getContext());
                popupWindow.setAnchorView(itemView);
                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (breadCrumbView.listener != null) {
                            final BreadCrumbItem<T> item = (BreadCrumbItem<T>) itemView.getTag();
                            final T selectedItem = item.getItens().get(i);
                            final T oldSelectedItem = item.getSelectedItem();
                            final int level = breadCrumbView.itens.indexOf(item);
                            if (breadCrumbView.listener != null) {
                                if (breadCrumbView.listener.onItemValueChanged(breadCrumbView, item, level, oldSelectedItem, selectedItem)) {
                                    item.setSelectedIndex(i);
                                    breadCrumbView.mAdapter.notifyDataSetChanged();
                                }
                            }
                            popupWindow.dismiss();
                        }
                    }
                });
            }
        }
    }
}
