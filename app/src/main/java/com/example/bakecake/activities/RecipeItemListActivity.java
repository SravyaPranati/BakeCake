package com.example.bakecake.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakecake.BakingService;
import com.example.bakecake.R;
import com.example.bakecake.others.RecipeItemDetailFragment;
import com.example.bakecake.model.Recipe;
import com.example.bakecake.model.Step;

import java.util.List;

/**
 * An activity representing a list of RecipeItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private TextView ingView;
    private Recipe recipe;
    private List<Step> step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeitem_list);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.recipeitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            recipe = getIntent().getParcelableExtra("recipe");
            ingView = findViewById(R.id.ing_data);
            setTitle(recipe.getName());
            String ingredientsTotal = "";
            ingredientsTotal = setIngredients(recipe);
            BakingService.myServiceMethod(this,ingredientsTotal);
            ingView.setText(ingredientsTotal);
        } else {

            mTwoPane = false;
            recipe = getIntent().getParcelableExtra("recipe");
            ingView = findViewById(R.id.ing_data);
            setTitle(recipe.getName());
            String ingredientsTotal = "";
            ingredientsTotal = setIngredients(recipe);
            BakingService.myServiceMethod(this,ingredientsTotal);
            ingView.setText(ingredientsTotal);
        }




        View recyclerView = findViewById(R.id.recipeitem_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private String setIngredients(Recipe recipes) {
        String ingredient, ingredientdetails = "";
        for (int i = 0; i < recipes.getIngredients().size(); i++) {
            if (recipes.getIngredients().get(i) != null) {
                ingredient = recipes.getIngredients().get(i).getIngredient() +
                        "\t\t" + recipes.getIngredients().get(i).getQuantity() +
                        recipes.getIngredients().get(i).getMeasure() + "\n";
                ingredientdetails += ingredient;

            }
        }
        return ingredientdetails;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recipe = getIntent().getParcelableExtra("recipe");
        step = recipe.getSteps();
        Log.i("Steps==",step.get(0).getVideoURL());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, step, mTwoPane));
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeItemListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(RecipeItemListActivity parent,
                                      List<Step> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipeitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                view.setOnClickListener(this);


            }

            @Override
            public void onClick(View view) {
                Step st = (Step) view.getTag();
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(RecipeItemDetailFragment.ARG_ITEM_ID, st);
                            RecipeItemDetailFragment fragment = new RecipeItemDetailFragment();

                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipeitem_detail_container, fragment)
                                    .commit();
                            fragment.setArguments(arguments);
                        } else {
                            Intent intent = new Intent(view.getContext(), RecipeItemDetailActivity.class);
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(RecipeItemDetailFragment.ARG_ITEM_ID, st);
                            intent.putExtras(arguments);
                            view.getContext().startActivity(intent);
                        }
                    }
            }
        }
    }

