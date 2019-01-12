package co.myahia.rssreader.features.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.myahia.rssreader.R;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.features.home.ArticleAdapter.ArticleClickListener;

public class ProviderAdapter extends Adapter<ProviderAdapter.ProviderViewHolder> {
    private List<String> providerNames;
    private HashMap<String, List<ApiArticle>> providersArticles;
    private ProvidersListener providersListener;

    class ProviderViewHolder extends ViewHolder {
        @BindView(R.id.provider_item_articles_recycler)
        RecyclerView articlesRecycler;
        @BindView(R.id.provider_item_name_tv)
        TextView providerName;
        @BindView(R.id.remove_img)
        ImageButton removeBtn;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.articlesRecycler.setHasFixedSize(true);
        }
    }

    public ProviderAdapter(@NonNull ProvidersListener listener) {
        providersArticles = new HashMap();
        providersListener = listener;
    }

    public void setItemList(HashMap<String, List<ApiArticle>> map) {
        this.providersArticles = map;
        this.providerNames = new ArrayList(map.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProviderViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.provider_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int i) {
        ArticleAdapter articleAdapter = new ArticleAdapter(providersListener);
        articleAdapter.setItemList(providersArticles.get(providerNames.get(holder.getAdapterPosition())));
        holder.articlesRecycler.setAdapter(articleAdapter);
        holder.articlesRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), 0, false));
        holder.providerName.setText(providerNames.get(holder.getAdapterPosition()).toUpperCase());
        holder.removeBtn.setOnClickListener(v -> { removeItemFromList(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return this.providersArticles.size();
    }

    public void removeItemFromList(int pos) {
        if (this.providerNames.size() > 3) {
            this.providersArticles.remove(this.providerNames.get(pos));
            this.providersListener.onRemoveProviderClicked(pos, providerNames.get(pos));
            this.providerNames.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, this.providersArticles.size());
        }
    }

    public interface ProvidersListener extends ArticleClickListener {
        void onRemoveProviderClicked(int i, String str);
    }
}
