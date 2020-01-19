package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.prikkie.Api.recipe_api.PrikkieApi.PrikkieRecipeApi;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static com.example.prikkie.App.hideKeyboardFrom;

public class RecipeFragment extends Fragment {
    private static RecipeFragment m_fragment;
    public static RecipeFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new RecipeFragment();
        }
        return m_fragment;
    }
    private RecipeFragment(){}

    private Button searchButton;
    private TextInputEditText searchQuery;
    private TextInputEditText include;
    private TextInputEditText exclude;
    private RecyclerView m_recipeListView;
    private int currentPage;
    public int lastPage;
    public LinearLayoutManager layoutManager;
    private boolean m_loadingMore;

    private RecipeListAdapter m_adapter;
    private ArrayList<Recipe> recipes;
    private ProgressBar m_loader;
    private View m_view;

    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_recipe, viewGroup, false);

        searchButton = m_view.findViewById(R.id.recipeSubmitButton);
        searchQuery = m_view.findViewById(R.id.recipeSearch);
        include = m_view.findViewById(R.id.includedIngredients);
        exclude = m_view.findViewById(R.id.excludeIngredient);
        m_recipeListView = m_view.findViewById(R.id.recipeList);
        m_loader = m_view.findViewById(R.id.progressBarRecipesFragment);

        buildRecyclerView();

        Button search = (Button) m_view.findViewById(R.id.recipeSubmitButton);

        expandableView = m_view.findViewById(R.id.expandableView);
        arrowBtn = m_view.findViewById(R.id.arrowBtn);
        cardView = m_view.findViewById(R.id.cardView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_loader.setVisibility(View.VISIBLE);
                currentPage = 1;
                showRecipe();
                hideKeyboardFrom(getContext(), m_view);
            }
        });
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                } else {
                    hideExpandableView();
                }
            }
        });

        if(m_adapter.getRecipes() == null) {
            m_loader.setVisibility(View.VISIBLE);
            search.performClick();
        }
        return m_view;
    }

    private void hideExpandableView(){
        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
        expandableView.setVisibility(View.GONE);
        arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
    }

    public void showRecipe(){
        m_loadingMore = true;
        //Get the recipe keywords
        String keywords = searchQuery.getText().toString();
        //Get the prefered ingredients
        String ingredients = include.getText().toString();
        //Get excluded ingredients
        String excludes = exclude.getText().toString();

        RecipeThread thread = new RecipeThread();
        thread.name = keywords;
        thread.includes = ingredients;
        thread.excludes = excludes;
        thread.page = currentPage;
        Thread t = new Thread(thread);
        t.start();
    }

    private void loadMoreRecipes(){
        if(currentPage < lastPage){
            currentPage++;
            showRecipe();
        }
    }

    //Builds the recylerView and sets up the adapter
    public void buildRecyclerView(){
        m_recipeListView.setHasFixedSize(true);
        m_recipeListView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!m_loadingMore) {
                    if ((visibleItemCount + firstVisibleItemPosition) >=
                            totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreRecipes();
                    }
                }
            }
        });
        layoutManager = new LinearLayoutManager((MainActivity) getContext());

        if(m_adapter == null) {
            m_adapter = new RecipeListAdapter(recipes, getActivity());
        }

        m_recipeListView.setLayoutManager(layoutManager);
        m_recipeListView.setAdapter(m_adapter);

        m_adapter.setOnItemClickListener(new RecipeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Go to recipe page
            }
        });
    }

    class RecipeThread implements Runnable{
        public String name;
        public String includes;
        public String excludes;
        public int page;

        private ArrayList<Recipe> recipes;
        private PrikkieRecipeApi api = new PrikkieRecipeApi();


        @Override
        public void run() {
            recipes = getRecipesFromApi();
            if(recipes == null){
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_adapter.setRecipes(recipes, currentPage);
                    m_loader.setVisibility(View.INVISIBLE);
                }
            });
            m_loadingMore = false;
        }

        private ArrayList<Recipe> getRecipesFromApi(){
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            recipes = api.getRecipeFromApi(name, includes, excludes, page);


            return recipes;
        }
    }
}