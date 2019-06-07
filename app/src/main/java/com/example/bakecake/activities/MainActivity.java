package com.example.bakecake.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.bakecake.R;
import com.example.bakecake.others.RecipeAdapter;
import com.example.bakecake.others.RecipeApi;
import com.example.bakecake.model.Recipe;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view_recipe)
    RecyclerView recycleView;
    RecipeAdapter recipeAdapter;
    int[] images ={R.mipmap.nutallapie,R.mipmap.brownie,R.mipmap.yellowcake,R.mipmap.cheesecake};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recipeAdapter = new RecipeAdapter(this,images);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        //List<Recipe> recipes = new ArrayList<>();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(RecipeApi.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RecipeApi recipeApi = retrofit.create(RecipeApi.class);
        Call<List<Recipe>> listCall = recipeApi.getRecipe();

        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    List<Recipe> recipes = response.body();
                        /*for(int i =0;i<recipes.size();i++)
                        {
                            Log.i("Items = ",recipes.get(i).getName());
                            Log.i("Items = ",recipes.get(i).getIngredients().toString());

                        }*/
                    recipeAdapter.setRecipes(recipes);
                    recycleView.setAdapter(recipeAdapter);


                }
                else{
                    Converter<ResponseBody,Recipe> converter = retrofit.responseBodyConverter(Recipe.class,new Annotation[0]);
                    Recipe er = null;
                    try {
                        er = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("Error",er.toString());

                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                Log.i("Msg=",t.getMessage());
                Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}

