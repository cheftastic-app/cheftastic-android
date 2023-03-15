package app.cheftastic.vanilla.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.cheftastic.R;
import app.cheftastic.vanilla.SettingsActivity;
import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.RecipeSearchActivity;
import app.cheftastic.vanilla.AboutActivity;
import app.cheftastic.vanilla.WeeklyMenuActivity;

public class NavBarFragment extends Fragment {
    public static final int OPTION_WEEKLY_MENU = 1;
    public static final int OPTION_RECIPE_LIST = 2;
    public static final int OPTION_SETTINGS = 3;
    public static final int OPTION_ABOUT_INFO = 4;
    private static final String STATE_CURRENT_SELECTION = "nav_bar_fragment_state_current_selection";
    private ViewHolder viewHolder;
    private String activityName;
    private int currentSelectionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentSelectionId = savedInstanceState.getInt(STATE_CURRENT_SELECTION);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        viewHolder.navDrawerList = (ListView) inflater.inflate(R.layout.fragment_nav_bar, container, false);
        viewHolder.navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentSelectionId == view.getId()) {
                    viewHolder.drawer.closeDrawers();
                    return;
                }

                switch (view.getId()) {
                    case OPTION_WEEKLY_MENU:
                        startActivity(new Intent(getActivity().getBaseContext(), WeeklyMenuActivity.class));
                        break;

                    case OPTION_RECIPE_LIST:
                        startActivity(new Intent(getActivity().getBaseContext(), RecipeSearchActivity.class));
                        break;

                    case OPTION_SETTINGS:
                        startActivity(new Intent(getActivity().getBaseContext(), SettingsActivity.class));
                        break;

                    case OPTION_ABOUT_INFO:
                        startActivity(new Intent(getActivity().getBaseContext(), AboutActivity.class));
                        break;
                }
            }
        });

        NavDrawerListAdapter listAdapter = new NavDrawerListAdapter(getActivity(), R.layout.list_item_nav_bar, new ArrayList<ListItem>());
        viewHolder.navDrawerList.setAdapter(listAdapter);
        listAdapter.add(new ListItem(OPTION_WEEKLY_MENU, R.string.weekly_menu, getDayIcon()));
        listAdapter.add(new ListItem(OPTION_RECIPE_LIST, R.string.recipes, R.drawable.ic_recipes));
        listAdapter.add(new ListItem(OPTION_SETTINGS, R.string.settings, R.drawable.ic_settings));
        listAdapter.add(new ListItem(OPTION_ABOUT_INFO, R.string.information, R.drawable.ic_information));

        return viewHolder.navDrawerList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        activityName = activity.getTitle().toString();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_SELECTION, currentSelectionId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        viewHolder.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (viewHolder.drawer != null && isDrawerOpen()) {
            showActionBar(menu);
        } else if (viewHolder.drawer != null && !isDrawerOpen()) {
            showActivityContext(menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewHolder.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return true;
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, int currentSelectionId) {
        viewHolder.drawerContainer = getActivity().findViewById(fragmentId);
        viewHolder.drawer = drawerLayout;
        this.currentSelectionId = currentSelectionId;

        viewHolder.drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        viewHolder.drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                viewHolder.drawer,
                R.drawable.ic_drawer,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();
            }
        };

        viewHolder.drawer.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.drawerToggle.syncState();
            }
        });

        viewHolder.drawer.setDrawerListener(viewHolder.drawerToggle);
    }

    public boolean isDrawerOpen() {
        return viewHolder.drawer != null && viewHolder.drawer.isDrawerOpen(viewHolder.drawerContainer);
    }

    private void showActionBar(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }

    }

    private void showActivityContext(Menu menu) {
        getActionBar().setTitle(activityName);

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(true);
        }
    }

    private int getDayIcon() {
        int dayOfMonth = new CheftasticCalendar(0L).get(CheftasticCalendar.DAY_OF_MONTH);

        switch (dayOfMonth) {
            case 1:
                return R.drawable.ic_calendar_1;
            case 2:
                return R.drawable.ic_calendar_2;
            case 3:
                return R.drawable.ic_calendar_3;
            case 4:
                return R.drawable.ic_calendar_4;
            case 5:
                return R.drawable.ic_calendar_5;
            case 6:
                return R.drawable.ic_calendar_6;
            case 7:
                return R.drawable.ic_calendar_7;
            case 8:
                return R.drawable.ic_calendar_8;
            case 9:
                return R.drawable.ic_calendar_9;
            case 10:
                return R.drawable.ic_calendar_10;
            case 11:
                return R.drawable.ic_calendar_11;
            case 12:
                return R.drawable.ic_calendar_12;
            case 13:
                return R.drawable.ic_calendar_13;
            case 14:
                return R.drawable.ic_calendar_14;
            case 15:
                return R.drawable.ic_calendar_15;
            case 16:
                return R.drawable.ic_calendar_16;
            case 17:
                return R.drawable.ic_calendar_17;
            case 18:
                return R.drawable.ic_calendar_18;
            case 19:
                return R.drawable.ic_calendar_19;
            case 20:
                return R.drawable.ic_calendar_20;
            case 21:
                return R.drawable.ic_calendar_21;
            case 22:
                return R.drawable.ic_calendar_22;
            case 23:
                return R.drawable.ic_calendar_23;
            case 24:
                return R.drawable.ic_calendar_24;
            case 25:
                return R.drawable.ic_calendar_25;
            case 26:
                return R.drawable.ic_calendar_26;
            case 27:
                return R.drawable.ic_calendar_27;
            case 28:
                return R.drawable.ic_calendar_28;
            case 29:
                return R.drawable.ic_calendar_29;
            case 30:
                return R.drawable.ic_calendar_30;
            default:
                return R.drawable.ic_calendar_31;
        }
    }

    private class ViewHolder {
        ActionBarDrawerToggle drawerToggle;
        View drawerContainer;
        DrawerLayout drawer;
        ListView navDrawerList;
    }

    private class ListItem {
        int id;
        int textId;
        int iconId;

        ListItem(int id, int textId, int iconId) {
            this.id = id;
            this.textId = textId;
            this.iconId = iconId;
        }
    }

    private class NavDrawerListAdapter extends ArrayAdapter<ListItem> {
        private final Context context;
        private final List<ListItem> options;
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public NavDrawerListAdapter(Context context, int resource, List<ListItem> objects) {
            super(context, resource, objects);

            this.context = context;
            this.options = objects;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_nav_bar, parent, false);
                viewHolder = new ViewHolder(convertView);
                if (convertView != null) {
                    convertView.setTag(viewHolder);
                    convertView.setId(options.get(position).id);
                }
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.text.setText(context.getString(options.get(position).textId));
            viewHolder.icon.setImageResource(options.get(position).iconId);

            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView text;

            public ViewHolder(View listItem) {
                icon = (ImageView) listItem.findViewById(R.id.list_item_nav_bar_icon);
                text = (TextView) listItem.findViewById(R.id.list_item_nav_bar_text);
            }
        }
    }
}
