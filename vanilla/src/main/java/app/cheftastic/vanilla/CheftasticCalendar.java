package app.cheftastic.vanilla;

import android.content.res.Resources;

import java.util.GregorianCalendar;

import app.cheftastic.R;

public class CheftasticCalendar extends GregorianCalendar {

    private int relativeWeek;

    public CheftasticCalendar() {
        this(0);
    }

    public CheftasticCalendar(int relativeWeek) {
        previousWeek(relativeWeek);
        this.relativeWeek = relativeWeek;
    }

    public CheftasticCalendar(long selectedDayId) {
        setDayId(selectedDayId);
        relativeWeek = calculateRelativeWeek();
    }

    public static int weeksBetween(CheftasticCalendar first, CheftasticCalendar second) {
        return second.getRelativeWeek() - first.getRelativeWeek();
    }

    public static int weeksBetween(long first, long second) {
        return weeksBetween(new CheftasticCalendar(first), new CheftasticCalendar(second));
    }

    public static int daysBetween(CheftasticCalendar first, CheftasticCalendar second) {
        int sign = -1;
        if (first.getDayId() < second.getDayId()) {
            sign = 1;
        }

        return sign * daysBetweenAbs(first, second);
    }

    @SuppressWarnings("MagicConstant")
    public static int daysBetweenAbs(CheftasticCalendar first, CheftasticCalendar second) {
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.set(first.get(YEAR), first.get(MONTH), first.get(DAY_OF_MONTH));
        calendar2.set(second.get(YEAR), second.get(MONTH), second.get(DAY_OF_MONTH));

        if (first.getDayId() < second.getDayId()) {
            GregorianCalendar t = calendar1;
            calendar1 = calendar2;
            calendar2 = t;
        }

        int daysBetween = 0;
        while (calendar1.get(YEAR) != calendar2.get(YEAR)) {
            daysBetween += calendar1.get(DAY_OF_YEAR);
            calendar1.set(calendar1.get(YEAR) - 1, DECEMBER, 31);
        }
        daysBetween += calendar1.get(DAY_OF_YEAR) - calendar2.get(DAY_OF_YEAR);

        return daysBetween;
    }

    public static boolean isValidDayId(long id) {
        if (id > 0 && Long.toString(id).length() == 8) {
            int day = (int) id % 100;
            id = (long) Math.floor(id / 100.0);
            int month = (int) id % 100;
            int year = (int) Math.floor(id / 100.0);

            if (day >= 1 && day <= 31 && month >= 1 && month <= 12) {
                switch (month) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        return true;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        return day <= 30;
                    case 2:
                        if (new GregorianCalendar().isLeapYear(year)) {
                            return day <= 29;
                        } else {
                            return day <= 28;
                        }
                }
            }
        }

        return false;
    }

    public void nextWeek() {
        previousWeek(relativeWeek + 1);
    }

    public void previousWeek(int difference) {
        GregorianCalendar c = new GregorianCalendar();
        c.set(WEEK_OF_YEAR, c.get(WEEK_OF_YEAR) + difference);
        setTime(c.getTime());

        relativeWeek += difference;
    }

    public int getRelativeWeek() {
        return this.relativeWeek;
    }

    public void previousDay() {
        int selectedWeek = get(WEEK_OF_YEAR);
        set(DAY_OF_YEAR, get(DAY_OF_YEAR) - 1);

        if (selectedWeek != get(WEEK_OF_YEAR)) {
            relativeWeek--;
        }
    }

    public void nextDay() {
        int selectedWeek = get(WEEK_OF_YEAR);
        set(DAY_OF_YEAR, get(DAY_OF_YEAR) + 1);

        if (selectedWeek != get(WEEK_OF_YEAR)) {
            relativeWeek++;
        }
    }

    @SuppressWarnings("MagicConstant")
    public boolean setDayId(long dayId) {
        int day = (int) dayId % 100;
        dayId = (long) Math.floor(dayId / 100.0);
        int month = (int) dayId % 100;
        int year = (int) Math.floor(dayId / 100.0);

        if (day != 0 && month != 0 && year != 0) {
            set(year, (month - 1), day);
            relativeWeek = calculateRelativeWeek();
            return true;
        }

        return false;
    }

    public void setRelativeDay(int relativeDay) {
        set(DAY_OF_YEAR, get(DAY_OF_YEAR) + relativeDay);
        relativeWeek = calculateRelativeWeek();
    }

    public int getDayOfMonth() {
        return get(DAY_OF_MONTH);
    }

    public int getDayOfMonth(int dayOfWeek) {
        int tDayOfWeek = get(DAY_OF_WEEK);

        set(DAY_OF_WEEK, dayOfWeek);
        int dayOfMonth = get(DAY_OF_MONTH);

        set(DAY_OF_WEEK, tDayOfWeek);

        return dayOfMonth;
    }

    public long getDayId() {
        return (((get(YEAR) * 100) + (get(MONTH) + 1)) * 100) + get(DAY_OF_MONTH);
    }

    public long getDayId(int relativeDay) {
        CheftasticCalendar c = new CheftasticCalendar(this.getDayId());
        c.setRelativeDay(relativeDay);
        return c.getDayId();
    }

    public long getDayOfWeekId(int dayOfWeek) {
        int tDayOfWeek = get(DAY_OF_WEEK);

        set(DAY_OF_WEEK, dayOfWeek);
        long id = getDayId();

        set(DAY_OF_WEEK, tDayOfWeek);

        return id;
    }

    public String getDayOfWeekName() {
        Resources r = App.getContext().getResources();
        switch (get(DAY_OF_WEEK)) {
            case MONDAY:
                return r.getStringArray(R.array.days_of_week)[0];
            case TUESDAY:
                return r.getStringArray(R.array.days_of_week)[1];
            case WEDNESDAY:
                return r.getStringArray(R.array.days_of_week)[2];
            case THURSDAY:
                return r.getStringArray(R.array.days_of_week)[3];
            case FRIDAY:
                return r.getStringArray(R.array.days_of_week)[4];
            case SATURDAY:
                return r.getStringArray(R.array.days_of_week)[5];
            case SUNDAY:
                return r.getStringArray(R.array.days_of_week)[6];
            default:
                return "";
        }
    }

    public String getDayOfWeekName(int relativeDay) {
        set(DAY_OF_YEAR, get(DAY_OF_YEAR) + relativeDay);
        String name = getDayOfWeekName();
        set(DAY_OF_YEAR, get(DAY_OF_YEAR) - relativeDay);
        return name;
    }

    public int getDayOfWeek() {
        switch (get(DAY_OF_WEEK)) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 7;
        }

        return -1;
    }

    public String getMonthNameAbbreviation() {
        Resources r = App.getContext().getResources();
        if (r != null) {
            switch (get(MONTH)) {
                case JANUARY:
                    return r.getStringArray(R.array.month_abbreviations)[0];
                case FEBRUARY:
                    return r.getStringArray(R.array.month_abbreviations)[1];
                case MARCH:
                    return r.getStringArray(R.array.month_abbreviations)[2];
                case APRIL:
                    return r.getStringArray(R.array.month_abbreviations)[3];
                case MAY:
                    return r.getStringArray(R.array.month_abbreviations)[4];
                case JUNE:
                    return r.getStringArray(R.array.month_abbreviations)[5];
                case JULY:
                    return r.getStringArray(R.array.month_abbreviations)[6];
                case AUGUST:
                    return r.getStringArray(R.array.month_abbreviations)[7];
                case SEPTEMBER:
                    return r.getStringArray(R.array.month_abbreviations)[8];
                case OCTOBER:
                    return r.getStringArray(R.array.month_abbreviations)[9];
                case NOVEMBER:
                    return r.getStringArray(R.array.month_abbreviations)[10];
                case DECEMBER:
                    return r.getStringArray(R.array.month_abbreviations)[11];
            }
        }

        return "";
    }

    public String getMonthNameAbbreviation(int dayOfWeek) {
        int tDayOfWeek = get(DAY_OF_WEEK);

        set(DAY_OF_WEEK, dayOfWeek);
        String month = getMonthNameAbbreviation();

        set(DAY_OF_WEEK, tDayOfWeek);

        return month;
    }

    @SuppressWarnings("MagicConstant")
    private int calculateRelativeWeek() {
        CheftasticCalendar currentWeek = new CheftasticCalendar();
        CheftasticCalendar newWeek = new CheftasticCalendar();
        newWeek.set(get(YEAR), get(MONTH), get(DAY_OF_MONTH));

        boolean anterior = (newWeek.getDayId() < currentWeek.getDayId());

        if (anterior) {
            currentWeek.set(DAY_OF_MONTH, currentWeek.getDayOfMonth(SUNDAY));
        } else {
            currentWeek.set(DAY_OF_MONTH, currentWeek.getDayOfMonth(MONDAY));
        }

        return (daysBetween(currentWeek, newWeek) / 7);
    }
}