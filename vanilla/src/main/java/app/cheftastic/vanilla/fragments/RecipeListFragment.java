package app.cheftastic.vanilla.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.cheftastic.R;
import app.cheftastic.vanilla.SQLiteHandler;
import app.cheftastic.vanilla.model.Recipe;

public class RecipeListFragment extends ListFragment {
    public static final String ARG_RECIPE_CATEGORY = "recipe_category";

    private RecipeListAdapter listAdapter;
    private RecipeListLoader recipeListLoader;
    private ProgressBar progressBar;
    private ListView recipeListView;
    private OnItemSelectedListener onItemSelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listAdapter = new RecipeListAdapter(getActivity(), R.layout.list_item_recipe, new ArrayList<Recipe>());
        setListAdapter(listAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressBar = (ProgressBar) getView().findViewById(R.id.fragment_recipe_list_progress_bar);
        recipeListView = getListView();

        if (recipeListLoader != null) {
            recipeListLoader = new RecipeListLoader(recipeListLoader.getProgress());
        } else {
            recipeListLoader = new RecipeListLoader();
        }

        recipeListLoader.execute((Void) null);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recipeListLoader != null) {
            recipeListLoader.cancel(true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onItemSelectedListener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnItemSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        onItemSelectedListener.onItemSelected(v.getId());
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(int id);
    }

    private class RecipeListAdapter extends ArrayAdapter<Recipe> {
        private final List<Recipe> recipes;
        ViewHolder viewHolder;
        private LayoutInflater inflater;

        public RecipeListAdapter(Context context, int resource, List<Recipe> objects) {
            super(context, resource, objects);
            this.recipes = objects;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_recipe, parent, false);
                viewHolder = new ViewHolder(convertView);
                if (convertView != null) {
                    convertView.setTag(viewHolder);
                }
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (convertView != null) convertView.setId(recipes.get(position).getId());
            viewHolder.name.setText(recipes.get(position).getName());
            viewHolder.description.setText(recipes.get(position).getDescription());
            viewHolder.verifiedIcon.setVisibility((recipes.get(position).getIsVerified()) ? ImageView.VISIBLE : ImageView.GONE);
            viewHolder.glutenFreeIcon.setVisibility((recipes.get(position).getIsGlutenFree()) ? ImageView.VISIBLE : ImageView.GONE);
            viewHolder.vegetarianIcon.setVisibility((recipes.get(position).getIsVegetarian()) ? ImageView.VISIBLE : ImageView.GONE);

            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView description;
            ImageView verifiedIcon;
            ImageView glutenFreeIcon;
            ImageView vegetarianIcon;

            public ViewHolder(View listItem) {
                name = (TextView) listItem.findViewById(R.id.list_item_recipe_name);
                description = (TextView) listItem.findViewById(R.id.list_item_recipe_description);
                verifiedIcon = (ImageView) listItem.findViewById(R.id.list_item_recipe_verified_icon);
                glutenFreeIcon = (ImageView) listItem.findViewById(R.id.list_item_recipe_gluten_free_icon);
                vegetarianIcon = (ImageView) listItem.findViewById(R.id.list_item_recipe_vegetarian_icon);
            }
        }
    }

    private class RecipeListLoader extends AsyncTask<Void, Integer, Void> {
        private int progress;
        private ArrayList<Recipe> recipes;

        public RecipeListLoader() {
            this(0);
        }

        public RecipeListLoader(int progress) {
            this.progress = progress;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bundle args = getArguments();

            if (args != null && args.containsKey(ARG_RECIPE_CATEGORY)) {
                int recipeCategoryId = args.getInt(ARG_RECIPE_CATEGORY);

                recipes = SQLiteHandler.retrieveListOfRecipes(recipeCategoryId);

                if (recipes != null && listAdapter != null) {
                    while (!isCancelled() && progress < recipes.size()) {
                        publishProgress(progress++);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            recipeListView.setVisibility(ListView.INVISIBLE);

            listAdapter.add(recipes.get(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(ProgressBar.GONE);
            recipeListView.setVisibility(ListView.VISIBLE);

            recipeListLoader = null;
        }

        public int getProgress() {
            return progress;
        }
    }
}
