package com.tevinjeffrey.rutgersct.rutgersapi.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings({"MagicNumber", "ValueOfIncrementOrDecrementUsed"})
public class SemesterUtils {

    private final Calendar calendar;
    private final int UPPER_LIMIT;
    private int LOWER_LIMIT = 2011;

    public SemesterUtils(Calendar calendar) {
        this.calendar = calendar;
        UPPER_LIMIT = Integer.parseInt(resolveCurrentYear(calendar)) + 1;
        LOWER_LIMIT = UPPER_LIMIT - 4;
    }

    public String[] getListOfSeasons() {
        return new String[]{Season.WINTER.getName(), Season.SPRING.getName(), Season.SUMMER.getName(), Season.FALL.getName()};
    }

    public List<String> getListOfYears() {
        return addYear(new ArrayList<String>(), UPPER_LIMIT);
    }

    private List<String> addYear(List<String> list, int limit) {
        int lowerLimit = LOWER_LIMIT;
        if (lowerLimit != limit) {
            list.add(String.valueOf(limit));
            addYear(list, --limit);
        }
        return list;
    }

    private String resolveCurrentYear(Calendar c) {
        return String.valueOf(c.get(Calendar.YEAR));
    }

    public Semester resolveCurrentSemester() {
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String currentYear = resolveCurrentYear(calendar);

        //Dec 15 <-> Jan 15 = Winter
        if ((month == 11 && dayOfMonth > 15) ||
                //The first month of the year is 0 for some fucking reason.
                (month == 0 && dayOfMonth < 15)) {
            return new Semester(Season.WINTER, month != 0 ? nextYear(currentYear) : currentYear);

        }
        //Jan 15 <-> May 20 = Spring
        else if ((month >= 0 && dayOfMonth >= 15) ||
                (month <= 4 && dayOfMonth < 20)) {
            return new Semester(Season.SPRING, currentYear);
        }
        //May 20 <-> August 20 = Summer
        else if ((month >= 4 && dayOfMonth >= 20) ||
                (month <= 7 && dayOfMonth < 20)) {
            return new Semester(Season.SUMMER, currentYear);
        }
        //August 20 <-> December 15 = Fall
        else if ((month >= 7 && dayOfMonth >= 20) ||
                (month <= 11 && dayOfMonth <= 15)) {
            return new Semester(Season.FALL, currentYear);
        } else {
            throw new IllegalStateException("could not resolveCurrentSemester");
        }
    }

    private String nextYear(String year) {
        int i = Integer.valueOf(year);
        return String.valueOf(++i);
    }

    private Semester[] resolveSemesterChoices(Calendar c) {
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String currentYear = resolveCurrentYear(c);

        //Oct 1 <-> Jan 15
        if ((month >= 9 && dayOfMonth >= 1) ||
                //The first day of the month is 0 for some fucking reason.
                (month == 0 && dayOfMonth < 15)) {
            return new Semester[]{new Semester(Season.WINTER, month != 0 ? nextYear(currentYear) : currentYear)
                    , new Semester(Season.SPRING, month != 0 ? nextYear(currentYear) : currentYear)};

        }
        //Jan 15 <-> March 15
        else if ((month == 0 && dayOfMonth >= 15) ||
                (month <= 2 && dayOfMonth < 15)) {
            return new Semester[]{new Semester(Season.SPRING, currentYear)
                    , new Semester(Season.SUMMER, currentYear)};
        }
        //March 15 <-> September 1
        else if ((month >= 2 && dayOfMonth >= 15) ||
                (month <= 9 && dayOfMonth <= 31)) {
            return new Semester[]{new Semester(Season.SUMMER, currentYear)
                    , new Semester(Season.FALL, currentYear)};
        } else {
            throw new IllegalStateException("could not resolveSemesterChoices");
        }
    }

    public Semester resolveSecondarySemester() {
        return resolveSemesterChoices(calendar)[1];
    }

    public String getSecondarySemester() {
        return resolveSecondarySemester().toString();
    }

    public Semester resolvePrimarySemester() {
        return resolveSemesterChoices(calendar)[0];
    }

    public String getPrimarySemester() {
        return resolvePrimarySemester().toString();
    }


    @Override
    public String toString() {
        return "SemesterUtils{" +
                "UPPER_LIMIT=" + UPPER_LIMIT +
                ", LOWER_LIMIT=" + LOWER_LIMIT +
                ", calendar=" + calendar +
                '}';
    }

    public enum Season {
        WINTER(0, "Winter"),
        SPRING(1, "Spring"),
        SUMMER(7, "Summer"),
        FALL(9, "Fall");

        private final int code;
        private final String name;

        Season(int code, String simpleName) {
            this.code = code;
            this.name = simpleName;
        }

        int getCode() {
            return this.code;
        }

        int getMappedValue() {
            if (this == WINTER) {
                return 0;
            } else if (this == SPRING) {
                return 1;
            } else if (this == SUMMER) {
                return 2;
            } else if (this == FALL) {
                return 3;
            } else {
                return -1;
            }
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "Season{" +
                    "code=" + code +
                    ", name='" + name + '\'' +
                    '}';
        }

    }

    public static class Semester implements Parcelable {

        Season mSeason;
        final String mYear;

        public Semester(Season season, String year) {
            this.mSeason = season;
            this.mYear = year;
        }

        public Semester(String season, String year) {
            this.mYear = year;
            switch (season) {
                case "Winter":
                    this.mSeason = Season.WINTER;
                    break;
                case "Spring":
                    this.mSeason = Season.SPRING;
                    break;
                case "Summer":
                    this.mSeason = Season.SUMMER;
                    break;
                case "Fall":
                    this.mSeason = Season.FALL;
                    break;
                default:
                    throw new IllegalArgumentException("error while getting Season enum");

            }
        }

        public Semester(String yearAndSeason) {
            this(yearAndSeason.split(" ")[0], yearAndSeason.split(" ")[1]);
        }

        public Season getSeason() {
            return mSeason;
        }

        public String getYear() {
            return mYear;
        }

        @Override
        public String toString() {
            return mSeason.getName() + " " + mYear;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mSeason == null ? -1 : this.mSeason.ordinal());
            dest.writeString(this.mYear);
        }

        protected Semester(Parcel in) {
            int tmpMSeason = in.readInt();
            this.mSeason = tmpMSeason == -1 ? null : Season.values()[tmpMSeason];
            this.mYear = in.readString();
        }

        public static final Parcelable.Creator<Semester> CREATOR = new Parcelable.Creator<Semester>() {
            public Semester createFromParcel(Parcel source) {
                return new Semester(source);
            }

            public Semester[] newArray(int size) {
                return new Semester[size];
            }
        };
    }
}