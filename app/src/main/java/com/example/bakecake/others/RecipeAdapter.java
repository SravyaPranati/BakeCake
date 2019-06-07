package com.example.bakecake.others;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakecake.R;
import com.example.bakecake.activities.RecipeItemListActivity;
import com.example.bakecake.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context context;
    private List<Recipe> recipes;
    private int images[];

    public RecipeAdapter(Context context,int[] images) {
        this.images = images;
        this.context = context;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.recipeText.setText(recipes.get(i).getName());
        viewHolder.recipeImage.setImageResource(images[i]);
        /*Picasso.with(context)
                .load(Uri.parse(recipes.get(i).getImage())).placeholder(R.mipmap.no_image).fit().centerInside().into(viewHolder.recipeImage);*/

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView recipeImage;
        private TextView recipeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeText = itemView.findViewById(R.id.recipe_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RecipeItemListActivity.class);
            /*Bundle bundle = new Bundle();
            bundle.putParcelable("recipe",recipes.get(i));*/
            intent.putExtra("recipe", recipes.get(getAdapterPosition()));
            /*intent.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) recipes.get(i).getIngredients());
            intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) recipes.get(i).getSteps());*/
            view.getContext().startActivity(intent);
        }
    }
}