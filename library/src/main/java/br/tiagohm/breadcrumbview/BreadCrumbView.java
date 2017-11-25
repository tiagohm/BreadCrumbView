package br.tiagohm.breadcrumbview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class BreadCrumbView<T> extends FrameLayout {

    private RecyclerView mBreadCrumb;
    private BreadCrumbAdapter mAdapter;
    private List<BreadCrumbItem<T>> itens = new LinkedList<>();
    private int textColor = Color.WHITE;
    private int separatorColor = Color.WHITE;

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
            textColor = a.getColor(R.styleable.BreadCrumbView_textColor, Color.WHITE);
            separatorColor = a.getColor(R.styleable.BreadCrumbView_separatorColor, Color.WHITE);
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
        //Testando...
        if (isInEditMode()) {
            itens.add(new BreadCrumbItem("Item 1", "Item 2", "Item 3"));
            itens.add(new BreadCrumbItem("Item 4", "Item 5"));
            itens.add(new BreadCrumbItem("Item 6", "Item 7", "Item 8", "Item 9"));
            update();
        }
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

    public List<BreadCrumbItem<T>> getItens() {
        return itens;
    }

    public void addItem(@NonNull BreadCrumbItem<T> item) {
        int oldSize = itens.size();
        itens.add(item);
        mAdapter.notifyItemRangeInserted(oldSize, 2);
        mAdapter.notifyItemChanged(oldSize - 1);
        smoothScrollToEndPosition();
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

    private static class BreadCrumbAdapter extends RecyclerView.Adapter<BreadCrumbAdapter.ItemHolder> {

        private BreadCrumbView<?> breadCrumbView;

        public BreadCrumbAdapter(BreadCrumbView<?> breadCrumbView) {
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
        public void onBindViewHolder(ItemHolder holder, int position) {
            if (holder instanceof SeparatorIconHolder) {
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

            public BreadCrumItemHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.breadcrumb_item_icon);
                text = itemView.findViewById(R.id.breadcrumb_item_text);
            }

            @Override
            public void setItem(BreadCrumbItem<?> item) {
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
                if (item.getSelectedItem() != null) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) icon.getLayoutParams();
                    float d = icon.getContext().getResources().getDisplayMetrics().density;
                    lp.setMarginEnd((int) (5 * d));
                    icon.setLayoutParams(lp);
                    text.setText(item.getSelectedItem().toString());
                    text.setTextColor(breadCrumbView.getTextColor());
                } else {
                    text.setText(null);
                }
            }
        }
    }
}
