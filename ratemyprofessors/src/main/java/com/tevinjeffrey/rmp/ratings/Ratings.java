package com.tevinjeffrey.rmp.ratings;

public class Ratings {
		final private Rating.Easiness easiness;
		final private Rating.Helpfulness helpfulness;
		final private Rating.Clarity clarity;
		final private Rating.OverallQuality overallQuality;
		final private Rating.AverageGrade averageGrade;


		public Ratings(Rating.Easiness easiness, Rating.Helpfulness helpfulness,
			Rating.Clarity clarity, Rating.OverallQuality overallQuality, Rating.AverageGrade averageGrade) {
			this.easiness = easiness;
			this.helpfulness = helpfulness;
			this.clarity = clarity;
			this.overallQuality = overallQuality;
			this.averageGrade = averageGrade;
		}

		public Rating getEasiness() {
			return easiness;
		}
		public Rating getHelpfulness() {
			return helpfulness;
		}
		public Rating getClarity() {
			return clarity;
		}
		public Rating getAverageGrade() {
			return averageGrade;
		}
		public Rating getOverallQuality() {
			return overallQuality;
		}

    @Override
    public String toString() {
        return  "   Easiness: " + easiness + "\n" +
                "   Helpfulness: " + helpfulness + "\n" +
                "   Clarity: " + clarity + "\n" +
                "   OverallQuality: " + overallQuality + "\n" +
                "   AverageGrade: " + averageGrade;
    }
}
