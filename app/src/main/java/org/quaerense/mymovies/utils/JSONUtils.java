package org.quaerense.mymovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quaerense.mymovies.data.Movie;
import org.quaerense.mymovies.data.Review;
import org.quaerense.mymovies.data.Trailer;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {
    private static final String KEY_RESULTS = "results";

    //All information about movie
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    //For reviews
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    //For trailers
    private static final String KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static List<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        List<Movie> movies = new ArrayList<>();

        if (jsonObject == null) {
            return movies;
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(i);
                int id = jsonMovie.getInt(KEY_ID);
                int voteCount = jsonMovie.getInt(KEY_VOTE_COUNT);
                String title = jsonMovie.getString(KEY_TITLE);
                String originalTitle = jsonMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = jsonMovie.getString(KEY_OVERVIEW);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH);
                String backdropPAth = jsonMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage = jsonMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = jsonMovie.getString(KEY_RELEASE_DATE);

                Movie movie = new Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPAth, voteAverage, releaseDate);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static Movie getMovieFromJSON(JSONObject jsonObject) {
        Movie movie = null;

        if (jsonObject == null) {
            return null;
        }

        try {
            int id = jsonObject.getInt(KEY_ID);
            int voteCount = jsonObject.getInt(KEY_VOTE_COUNT);
            String title = jsonObject.getString(KEY_TITLE);
            String originalTitle = jsonObject.getString(KEY_ORIGINAL_TITLE);
            String overview = jsonObject.getString(KEY_OVERVIEW);
            String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + jsonObject.getString(KEY_POSTER_PATH);
            String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + jsonObject.getString(KEY_POSTER_PATH);
            String backdropPAth = jsonObject.getString(KEY_BACKDROP_PATH);
            double voteAverage = jsonObject.getDouble(KEY_VOTE_AVERAGE);
            String releaseDate = jsonObject.getString(KEY_RELEASE_DATE);

            movie = new Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPAth, voteAverage, releaseDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }

    public static List<Review> getReviewsFromJSON(JSONObject jsonObject) {
        List<Review> reviews = new ArrayList<>();

        if (jsonObject == null) {
            return reviews;
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReview = jsonArray.getJSONObject(i);
                String author = jsonReview.getString(KEY_AUTHOR);
                String content = jsonReview.getString(KEY_CONTENT);

                Review review = new Review(author, content);
                reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static List<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        List<Trailer> trailers = new ArrayList<>();

        if (jsonObject == null) {
            return trailers;
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTrailer = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + jsonTrailer.getString(KEY_OF_VIDEO);
                String name = jsonTrailer.getString(KEY_NAME);

                Trailer trailer = new Trailer(key, name);
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }
}
