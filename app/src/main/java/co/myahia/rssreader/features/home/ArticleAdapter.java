package co.myahia.rssreader.features.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import co.myahia.rssreader.R;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<ApiArticle> apiArticleList = new ArrayList();
    private ArticleClickListener mArticleListener;

    public interface ArticleClickListener {
        void onArticleClicked(ApiArticle apiArticle);
    }

    class ArticleViewHolder extends ViewHolder {

        @BindView(R.id.article_item_img)
        ImageView photo;

        @BindView(R.id.article_item_title_tv)
        TextView title;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ArticleAdapter(ArticleClickListener listener) {
        this.mArticleListener = listener;
    }

    public void setItemList(List<ApiArticle> list) {
        this.apiArticleList = list;
        notifyDataSetChanged();
    }

    @NonNull
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ArticleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, int i) {
        holder.title.setText(apiArticleList.get(holder.getAdapterPosition()).getTitle());
        Picasso.with(holder.itemView.getContext()).load(apiArticleList.get(holder.getAdapterPosition()).getUrlToImage()).into(holder.photo);
        holder.itemView.setOnClickListener(v-> mArticleListener.onArticleClicked(apiArticleList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return this.apiArticleList.size();
    }
}
