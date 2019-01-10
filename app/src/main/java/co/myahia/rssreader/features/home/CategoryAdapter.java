package co.myahia.rssreader.features.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

public class CategoryAdapter extends Adapter<CategoryAdapter.CategoryViewHolder> {

    class CategoryViewHolder extends ViewHolder {
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
    }

    public int getItemCount() {
        return 0;
    }
}
