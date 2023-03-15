package app.cheftastic.vanilla.widgets.menu;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.SQLiteHandler;
import app.cheftastic.vanilla.model.DailyMenu;
import app.cheftastic.vanilla.model.MenuRecipe;
import app.cheftastic.vanilla.model.Recipe;

public class MenuWidget extends ViewPager {

    private final int PREVIOUS_PAGE = 0;
    private final int CURRENT_PAGE = 1;
    private final int NEXT_PAGE = 2;

    private final int NUM_PAGES = 3;

    private final int MOVE_TO_PREVIOUS = -1;
    private final int MOVE_TO_NEXT = 1;
    private final int STALE = 0;

    private int movement = STALE;

    private int selectedPage = CURRENT_PAGE;
    private boolean canMove = true;

    private ViewMenu[] viewMenus;
    private Queue<Long> loadQueue;
    private CheftasticCalendar calendar;
    private Handler handler;
    private MenuLoader menuLoader;

    public MenuWidget(Context context) {
        this(context, null);
    }

    public MenuWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuWidget(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        calendar = new CheftasticCalendar();

        viewMenus = new ViewMenu[NUM_PAGES];
        loadQueue = new LinkedList<Long>();
        viewMenus[PREVIOUS_PAGE] = new ViewMenu(getContext());
        viewMenus[CURRENT_PAGE] = new ViewMenu(getContext());
        viewMenus[NEXT_PAGE] = new ViewMenu(getContext());

        this.setAdapter(new WidgetMenuPagerAdapter());

        setCurrentItem(CURRENT_PAGE);


        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (movement == STALE) {
                        if (selectedPage == PREVIOUS_PAGE) {
                            calendar.previousDay();

                            if (handler != null) {
                                handler.onMenuWidgetSelectedDayChange(calendar.getDayId());
                            }

                            viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1), viewMenus[CURRENT_PAGE].getMenu());
                            viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId(), viewMenus[PREVIOUS_PAGE].getMenu());
                            setCurrentItem(CURRENT_PAGE, false);
                            viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1));

                            loadQueue.add(calendar.getDayId(-1));

                            launchLoaderOnBackground();

                        } else if (selectedPage == NEXT_PAGE) {
                            calendar.nextDay();

                            if (handler != null) {
                                handler.onMenuWidgetSelectedDayChange(calendar.getDayId());
                            }

                            viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1), viewMenus[CURRENT_PAGE].getMenu());
                            viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId(), viewMenus[NEXT_PAGE].getMenu());
                            setCurrentItem(CURRENT_PAGE, false);
                            viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1));

                            loadQueue.add(calendar.getDayId(+1));

                            launchLoaderOnBackground();
                        }
                    } else {
                        if (movement == MOVE_TO_PREVIOUS) {
                            viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1));

                            loadQueue.add(calendar.getDayId(+1));

                            launchLoaderOnBackground();
                        } else if (movement == MOVE_TO_NEXT) {
                            viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1));

                            loadQueue.add(calendar.getDayId(-1));

                            launchLoaderOnBackground();
                        }

                        movement = STALE;
                    }
                }
            }
        });

        viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1));
        viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId());
        viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1));

        loadQueue.add(calendar.getDayId());
        loadQueue.add(calendar.getDayId(+1));
        loadQueue.add(calendar.getDayId(-1));

        launchLoaderOnBackground();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return canMove && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return canMove && super.onInterceptTouchEvent(event);

    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setSelectedDayId(long dayId) {
        if (dayId > calendar.getDayId()) {
            calendar.setDayId(dayId);
            movement = MOVE_TO_NEXT;

            viewMenus[PREVIOUS_PAGE].setDayId(dayId, viewMenus[CURRENT_PAGE].getMenu());

            viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1));

            setCurrentItem(PREVIOUS_PAGE, false);

            viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId());

            setCurrentItem(CURRENT_PAGE);

            loadQueue.clear();
            loadQueue.add(calendar.getDayId());
            loadQueue.add(calendar.getDayId(+1));

            launchLoaderOnBackground();

        } else if (dayId < calendar.getDayId()) {
            calendar.setDayId(dayId);
            movement = MOVE_TO_PREVIOUS;

            viewMenus[NEXT_PAGE].setDayId(dayId, viewMenus[CURRENT_PAGE].getMenu());

            viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1));

            setCurrentItem(NEXT_PAGE, false);

            viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId());

            setCurrentItem(CURRENT_PAGE);

            loadQueue.clear();
            loadQueue.add(calendar.getDayId());
            loadQueue.add(calendar.getDayId(-1));

            launchLoaderOnBackground();
        }
    }

    public void restoreSavedState(long dayId) {
        calendar.setDayId(dayId);

        viewMenus[PREVIOUS_PAGE].setDayId(calendar.getDayId(-1));
        viewMenus[CURRENT_PAGE].setDayId(calendar.getDayId());
        viewMenus[NEXT_PAGE].setDayId(calendar.getDayId(+1));

        loadQueue.clear();
        loadQueue.add(dayId);
        loadQueue.add(calendar.getDayId(-1));
        loadQueue.add(calendar.getDayId(+1));

        this.movement = STALE;

        launchLoaderOnBackground();
    }

    public void addRecipe(long dayId, int mealId, int recipeId) {
        int page = (int) (dayId - calendar.getDayId()) + CURRENT_PAGE;

        if (page < PREVIOUS_PAGE || page > NEXT_PAGE) {
            SQLiteHandler.storeMenuRecipe(new MenuRecipe(dayId, mealId, Recipe.retrieveRecipe(recipeId)));
        } else {
            viewMenus[page].getMenu().addRecipeAndSave(Recipe.retrieveRecipe(recipeId), mealId);
            viewMenus[page].updateView();
        }
    }

    private void launchLoaderOnBackground() {
        if (menuLoader == null) {
            menuLoader = new MenuLoader();
            menuLoader.execute((Void) null);
        }
    }

    public void onMenuLoaded(long dayId, DailyMenu menu) {
        int page = CURRENT_PAGE + CheftasticCalendar.daysBetween(calendar, new CheftasticCalendar(dayId));

        if (page < NUM_PAGES && page >= 0) {
            viewMenus[page].setDayId(dayId, menu);
        }
    }

    public interface Handler {

        public void onMenuWidgetSelectedDayChange(long selectedDayId);
    }

    private class WidgetMenuPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            viewMenus[position].setDayName(calendar.getDayOfWeekName((position - CURRENT_PAGE)));
            container.addView(viewMenus[position]);
            return viewMenus[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return viewMenus[position].getDayName();
        }
    }

    private class MenuLoader extends AsyncTask<Void, DailyMenu, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!loadQueue.isEmpty()) {
                try {
                    long dayId = loadQueue.poll();
                    DailyMenu menu = new DailyMenu(dayId);

                    publishProgress(menu);
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(DailyMenu... values) {
            super.onProgressUpdate(values);
            onMenuLoaded(values[0].getDayId(), values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            menuLoader = null;
        }
    }
}
