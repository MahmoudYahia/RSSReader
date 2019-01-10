package co.myahia.rssreader.features.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.myahia.rssreader.R;
import co.myahia.rssreader.data.remote.model.NewsProvider;

/**
 * Created by M.YAHIA on 09/01/2019.
 */
public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourceViewHolder> {
    private List<NewsProvider> newsProviderList;
    private List<Integer> selectedPositions;

    public SourcesAdapter() {
        newsProviderList = new ArrayList<>();
        selectedPositions = new ArrayList<>();
    }

    public void setNewsProviderList(List<NewsProvider> newsProviderList) {
        this.newsProviderList = newsProviderList;
        this.selectedPositions.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.source_item_layout, viewGroup, false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int i) {
        holder.checkBox.setChecked(selectedPositions.contains(holder.getAdapterPosition()));
        holder.checkBox.setText(newsProviderList.get(holder.getAdapterPosition()).getName());
        holder.urlTv.setText(newsProviderList.get(holder.getAdapterPosition()).getUrl());
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (!selectedPositions.contains(holder.getAdapterPosition()))
                    selectedPositions.add(holder.getAdapterPosition());
            } else {
                selectedPositions.remove(Integer.valueOf(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsProviderList.size();
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.source_item_checkbox)
        CheckBox checkBox;
        @BindView(R.id.source_item_url_tv)
        TextView urlTv;

        public SourceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<NewsProvider> getSelectedPositions() {
        ArrayList<NewsProvider> list = new ArrayList<>();

        for (Integer pos : selectedPositions) {
            list.add(newsProviderList.get(pos));
        }
        return list;
    }
}
