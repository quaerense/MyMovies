package org.quaerense.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.quaerense.mymovies.adapters.ReviewAdapter;
import org.quaerense.mymovies.adapters.TrailerAdapter;
import org.quaerense.mymovies.data.FavouriteMovie;
import org.quaerense.mymovies.data.MainViewModel;
import org.quaerense.mymovies.data.Movie;
import org.quaerense.mymovies.data.Review;
import org.quaerense.mymovies.data.Trailer;
import org.quaerense.mymovies.utils.JSONUtils;
import org.quaerense.mymovies.utils.NetworkUtils;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ScrollView scrollViewInfo;
    private ImageView imageViewBigPoster;
    private ImageView imageViewAddToFavourite;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private int id;
    private Movie movie;
    private FavouriteMovie favouriteMovie;

    private MainViewModel viewModel;

    private static String lang;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemMain) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else if (id == R.id.itemFavourite) {
            Intent favouriteIntent = new Intent(this, FavouriteActivity.class);
            startActivity(favouriteIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lang = Locale.getDefault().getLanguage();

        scrollViewInfo = findViewById(R.id.scrollViewInfo);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);

        imageViewAddToFavourite.setOnClickListener(this::onClickChangeFavourite);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }

        JSONObject jsonMovie = NetworkUtils.getJSONForMovie(id, lang);
        movie = JSONUtils.getMovieFromJSON(jsonMovie);

        if (movie != null) {
            Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.placeholder).into(imageViewBigPoster);
            textViewTitle.setText(movie.getTitle());
            textViewOriginalTitle.setText(movie.getOriginalTitle());
            textViewRating.setText(String.valueOf(movie.getVoteAverage()));
            textViewReleaseDate.setText(movie.getReleaseDate());
            textViewOverview.setText(movie.getOverview());

            setFavourite();

            recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
            recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
            trailerAdapter = new TrailerAdapter();
            trailerAdapter.setOnTrailerClickListener(url -> {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            });
            reviewAdapter = new ReviewAdapter();
            recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewTrailers.setAdapter(trailerAdapter);
            recyclerViewReviews.setAdapter(reviewAdapter);

            JSONObject jsonTrailers = NetworkUtils.getJSONForVideos(movie.getId(), lang);
            JSONObject jsonReviews = NetworkUtils.getJSONForReviews(movie.getId());
            List<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonTrailers);
            List<Review> reviews = JSONUtils.getReviewsFromJSON(jsonReviews);

            trailerAdapter.setTrailers(trailers);
            reviewAdapter.setReviews(reviews);

            scrollViewInfo.smoothScrollTo(0, 0);
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show();
        }

        setFavourite();
    }

    private void setFavourite() {
        favouriteMovie = viewModel.getFavouriteMovieById(id);

        if (favouriteMovie == null) {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to);
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove);
        }
    }
}