import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class rmp {
	public static void main(String []args) {

	}

	public class Professor {
		final private String name;
		final private String university; 
		final private String department;
		final private Ratings ratings;

		public Professor(String name, String university, String department, Rating ratings) {
			this.name = name;
			this.university = university;
			this.department = department;
			this.ratings = ratings;
		}

	}
	
	public class Ratings {
		final private Easiness easiness;
		final private Helpfulness helpfulness;
		final private Clarity clarity;
		final private OverallQuality overallQuality;
		final private AverageGrade averageGrade;


		public Ratings(Easiness easiness, Helpfulness helpfulness, 
			Clarity clarity, OverallQuality overallQuality, AverageGrade averageGrade) {
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

	}

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
	}

	public class Easiness extends Rating {
		public Easiness(String rating) {
			super(rating, "Easiness");
		}
	}


	public class Helpfulness extends Rating {
		public Helpfulness(String rating) {
			super(rating, "Helpfulness");
		}
	}

	public class Clarity extends Rating {
		public Clarity(String rating) {
			super(rating, "Clarity");
		}
	}

	public class OverallQuality extends Rating {
		public OverallQuality(String rating) {
			super(rating, "Overall Quality");
		}
	}

	public class AverageGrade extends Rating {
		public AverageGrade(String rating) {
			super(rating, "Average Grade");
		}
	}

}