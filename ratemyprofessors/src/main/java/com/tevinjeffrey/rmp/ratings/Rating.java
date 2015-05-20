package com.tevinjeffrey.rmp.ratings;

public class Rating {
    final String rating;
    final String description;

    public Rating(String rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return rating;
    }

    public static class Easiness extends Rating {
        public Easiness(String rating) {
            super(rating, "Easiness");
        }
    }


    public static class Helpfulness extends Rating {
        public Helpfulness(String rating) {
            super(rating, "Helpfulness");
        }
    }

    public static class Clarity extends Rating {
        public Clarity(String rating) {
            super(rating, "Clarity");
        }
    }

    public static class OverallQuality extends Rating {
        public OverallQuality(String rating) {
            super(rating, "Overall Quality");
        }
    }

    public static class AverageGrade extends Rating {
        public AverageGrade(String rating) {
            super(rating, "Average Grade");
        }
    }
}