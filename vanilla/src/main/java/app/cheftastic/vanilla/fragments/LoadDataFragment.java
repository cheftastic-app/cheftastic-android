package app.cheftastic.vanilla.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import app.cheftastic.R;
import app.cheftastic.vanilla.App;
import app.cheftastic.vanilla.SQLiteHandler;
import app.cheftastic.vanilla.Settings;
import app.cheftastic.vanilla.WeeklyMenuActivity;
import app.cheftastic.vanilla.model.Recipe;

public class LoadDataFragment extends Fragment {
    public static final String STATE_UPDATING = "load_data_fragment_state_updating";
    public static final String STATE_DATA_ALREADY_UPDATED = "load_data_fragment_state_data_already_updated";
    public static final String STATE_RECIPES_ALREADY_COMPUTED = "load_data_fragment_state_recipes_already_computed";
    public static final String STATE_PROGRESS_BAR_1 = "load_data_fragment_state_progress_bar_1";
    public static final String STATE_PROGRESS_BAR_2 = "load_data_fragment_state_progress_bar_2";
    private static final String DATA_SEPARATOR = "###";
    private static final String TAG = "app.cheftastic.vanilla.fragments.LoadDataFragment";
    private final int PROGRESS_BAR_MAX = 1000;
    private DataUpdater dataUpdater;
    private Bundle savedState;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_load_data, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedState != null && savedInstanceState != null) {
            savedState.putAll(savedInstanceState);
        } else {
            savedState = savedInstanceState;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        progressBar = (ProgressBar) getView().findViewById(R.id.fragment_load_data_progress_bar);

        SQLiteHandler sqliteHandler = new SQLiteHandler(getActivity());

        if (savedState == null && dataUpdater == null) {
            progressBar.setVisibility(ProgressBar.GONE);

            dataUpdater = new DataUpdater(0, 0);
        } else {
            if (dataUpdater != null) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                dataUpdater = new DataUpdater(dataUpdater.getDataAlreadyUpdated(), dataUpdater.getRecipesAlreadyComputed());
            } else if (savedState.containsKey(STATE_UPDATING) && savedState.getBoolean(STATE_UPDATING)) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                progressBar.setMax(100);
                progressBar.setProgress((int) (savedState.getInt(STATE_PROGRESS_BAR_1) * ((double) 100 / PROGRESS_BAR_MAX)));
                progressBar.setSecondaryProgress((int) (savedState.getInt(STATE_PROGRESS_BAR_2) * ((double) 100 / PROGRESS_BAR_MAX)));

                dataUpdater = new DataUpdater(savedState.getInt(STATE_DATA_ALREADY_UPDATED), savedState.getInt(STATE_RECIPES_ALREADY_COMPUTED));
            }
        }

        SQLiteDatabase db = sqliteHandler.getWritableDatabase();
        dataUpdater.execute(db);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (dataUpdater != null) {
            dataUpdater.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dataUpdater != null) {
            outState.putBoolean(STATE_UPDATING, true);
            outState.putInt(STATE_DATA_ALREADY_UPDATED, dataUpdater.getDataAlreadyUpdated());
            outState.putInt(STATE_RECIPES_ALREADY_COMPUTED, dataUpdater.getRecipesAlreadyComputed());
            outState.putInt(STATE_PROGRESS_BAR_1, progressBar.getProgress());
            outState.putInt(STATE_PROGRESS_BAR_2, progressBar.getSecondaryProgress());
        } else {
            outState.putBoolean("updating", false);
        }
    }

    private int calculatePercentage(int total, int current, int scale) {
        if (total == 0) {
            return 0;
        }

        return (int) Math.round(((((double) current) / total) * scale));
    }

    private class DataUpdater extends AsyncTask<SQLiteDatabase, Integer, Void> {
        private int dataAlreadyUpdated;
        private int lastDataUpdated;
        private int lastLocalDataAvailable;
        private int lastRemoteDataAvailable;
        private int totalDataToUpdate;

        private int recipesAlreadyComputed;
        private int totalRecipesToCompute;

        public DataUpdater(int dataAlreadyUpdated, int recipesAlreadyComputed) {
            this.dataAlreadyUpdated = dataAlreadyUpdated;
            this.recipesAlreadyComputed = recipesAlreadyComputed;
        }

        @Override
        protected Void doInBackground(SQLiteDatabase... params) {
            updateData(params[0]);
            computeRecipes(params[0]);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress(values[1]);
            progressBar.setSecondaryProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(ProgressBar.GONE);

            Intent i = new Intent(getActivity(), WeeklyMenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().finish();
        }

        private void updateData(SQLiteDatabase db) {
            if (db != null) {
                lastDataUpdated = SQLiteHandler.getLastUpdatedData();

                BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.data)));
                String read;
                lastLocalDataAvailable = 0;
                try {
                    while ((read = br.readLine()) != null) {
                        lastLocalDataAvailable = Integer.parseInt(read.split(DATA_SEPARATOR)[0]);
                    }

                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error al leer archivo data.txt");
                }

                String[] remoteUrl = null;
                ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifi != null && (wifi.isConnected() || !Settings.getUseOnlyWifi())) {
                    String response = null;
                    try {
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet(App.WEB_SERVICE_URL + App.WEB_SERVICE_UPDATER + App.WEB_SERVICE_PARAMS_PREFIX + App.WEB_SERVICE_UPDATER_PARAM_LAST_UPDATED + getLastDataUpdated());
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        response = EntityUtils.toString(httpResponse.getEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response != null) {
                        remoteUrl = response.split(App.WEB_SERVICE_UPDATER_SEPARATOR);
                    }

                    lastRemoteDataAvailable = 0;
                    if (remoteUrl != null) {
                        try {
                            URL url = new URL(remoteUrl[remoteUrl.length - 1]);
                            br = new BufferedReader(new InputStreamReader(url.openStream()));

                            while ((read = br.readLine()) != null) {
                                lastRemoteDataAvailable = Integer.parseInt(read.split(DATA_SEPARATOR)[0]);
                            }

                            br.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (lastDataUpdated < lastLocalDataAvailable) {
                    totalDataToUpdate = lastLocalDataAvailable + lastRemoteDataAvailable - lastDataUpdated + dataAlreadyUpdated;
                    progressBar.setMax(PROGRESS_BAR_MAX);
                    progressBar.setProgress(0);
                    progressBar.setSecondaryProgress(calculatePercentage(totalDataToUpdate, dataAlreadyUpdated, progressBar.getMax()));

                    try {
                        br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.data)));
                        while (!isCancelled() && ((read = br.readLine()) != null)) {
                            String[] data = read.split(DATA_SEPARATOR);
                            if (Integer.parseInt(data[0]) > lastDataUpdated) {
                                db.beginTransaction();
                                try {
                                    db.execSQL(data[1]);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SQLiteHandler.TABLE_METADATA_COLUMN_INTEGER_VALUE, data[0]);
                                    db.update(SQLiteHandler.TABLE_METADATA, cv, SQLiteHandler.TABLE_METADATA_COLUMN_KEY + " = ?", new String[]{SQLiteHandler.TABLE_METADATA_COLUMN_LAST_UPDATED_VALUE});
                                    db.setTransactionSuccessful();
                                } catch (android.database.sqlite.SQLiteConstraintException e) {
                                    Log.w(TAG, "Se ha intentado volver a insertar un dato ya insertado, afortunadamente hemos salido del apuro");
                                } finally {
                                    publishProgress(calculatePercentage(totalDataToUpdate, ++dataAlreadyUpdated, progressBar.getMax()), 0);
                                    lastDataUpdated = (Integer.parseInt(data[0]));

                                    db.endTransaction();
                                }
                            }
                        }

                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error al leer archivo datatxt");
                    }
                }

                if (remoteUrl != null && lastDataUpdated < lastRemoteDataAvailable) {
                    totalDataToUpdate = lastLocalDataAvailable + lastRemoteDataAvailable - lastDataUpdated + dataAlreadyUpdated;
                    progressBar.setMax(PROGRESS_BAR_MAX);
                    progressBar.setProgress(0);
                    progressBar.setSecondaryProgress(calculatePercentage(totalDataToUpdate, dataAlreadyUpdated, progressBar.getMax()));

                    for (String url : remoteUrl) {
                        try {
                            br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                            while (!isCancelled() && ((read = br.readLine()) != null)) {
                                String[] data = read.split(DATA_SEPARATOR);
                                if (Integer.parseInt(data[0]) > lastDataUpdated) {
                                    db.beginTransaction();
                                    try {
                                        db.execSQL(data[1]);

                                        ContentValues cv = new ContentValues();
                                        cv.put(SQLiteHandler.TABLE_METADATA_COLUMN_INTEGER_VALUE, data[0]);
                                        db.update(SQLiteHandler.TABLE_METADATA, cv, SQLiteHandler.TABLE_METADATA_COLUMN_KEY + " = ?", new String[]{SQLiteHandler.TABLE_METADATA_COLUMN_LAST_UPDATED_VALUE});
                                        db.setTransactionSuccessful();
                                    } catch (android.database.sqlite.SQLiteConstraintException e) {
                                        Log.w(TAG, "Se ha intentado volver a insertar un dato ya insertado, afortunadamente hemos salido del apuro");
                                    } finally {
                                        publishProgress(calculatePercentage(totalDataToUpdate, ++dataAlreadyUpdated, progressBar.getMax()), 0);
                                        lastDataUpdated = (Integer.parseInt(data[0]));

                                        db.endTransaction();
                                    }
                                }
                            }

                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error al leer el archivo remoto " + url);
                        }
                    }
                }
            }
        }

        private void computeRecipes(SQLiteDatabase db) {
            if (db != null) {
                Cursor recipes = db.rawQuery(SQLiteHandler.QUERY_GET_RECIPES_WITHOUT_SCORING_COUNT, null);
                recipes.moveToFirst();
                int recipesToCompute = recipes.getInt(recipes.getColumnIndex(SQLiteHandler.SELECT_TABLE_RECIPE_COLUMN_NOT_YET_COMPUTED));

                if (recipesToCompute > 0) {
                    totalRecipesToCompute = recipesToCompute + recipesAlreadyComputed;

                    recipes = db.rawQuery(SQLiteHandler.QUERY_GET_RECIPES_WITHOUT_SCORING, null);

                    progressBar.setMax(PROGRESS_BAR_MAX);
                    progressBar.setProgress(calculatePercentage(totalRecipesToCompute, recipesAlreadyComputed, progressBar.getMax()));
                    progressBar.setSecondaryProgress(PROGRESS_BAR_MAX);

                    while (!isCancelled() && recipes.moveToNext()) {
                        Recipe recipe = Recipe.retrieveRecipe(recipes.getInt(recipes.getColumnIndex(SQLiteHandler.SELECT_TABLE_RECIPE_COLUMN_ID)));

                        recipe.processIngredients(db);

                        publishProgress(PROGRESS_BAR_MAX, calculatePercentage(totalRecipesToCompute, ++recipesAlreadyComputed, progressBar.getMax()));
                    }
                }
            }
        }

        public int getDataAlreadyUpdated() {
            return this.dataAlreadyUpdated;
        }

        public int getLastDataUpdated() {
            return this.lastDataUpdated;
        }

        public int getRecipesAlreadyComputed() {
            return this.recipesAlreadyComputed;
        }
    }
}