package stanislav.volnjanskij.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import stanislav.volnjanskij.popularmovies.api.MovieModel;
import stanislav.volnjanskij.popularmovies.ui.movie_details.DetailsActivity;
import stanislav.volnjanskij.popularmovies.ui.movie_details.DetailsFragment;
import stanislav.volnjanskij.popularmovies.ui.movies_list.MoviesListFragment;
import stanislav.volnjanskij.popularmovies.ui.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity implements MoviesListFragment.Callback {
    static final String DETAILS = "details";


    @Nullable
    @Bind(R.id.container)
    FrameLayout container;

    boolean twoPane;
    private DetailsFragment detailsFragment;
    private String currentOrder;
    private MovieModel movie;
    private MoviesListFragment movieListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        movieListFragment = (MoviesListFragment) getFragmentManager().findFragmentById(R.id.movie_list);
        movieListFragment.setCallback(this);
        if (container != null) {
            twoPane = true;
        }
        if (savedInstanceState != null && twoPane) {
            detailsFragment = new DetailsFragment();
            movie = savedInstanceState.getParcelable("movie");
            Bundle params = new Bundle();
            params.putParcelable("movie", movie);
            detailsFragment.setArguments(params);
            getFragmentManager().beginTransaction().replace(R.id.container, detailsFragment, DETAILS).commit();

        }
        if (!ThisApplication.isConectedToInternet()) {
            getSupportActionBar().setTitle(R.string.favorites);
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int listType = prefs.getInt(MoviesListFragment.LIST_TYPE, MoviesListFragment.POPULAR);
            switch (listType) {
                case MoviesListFragment.FAVORITES:
                    getSupportActionBar().setTitle(R.string.favorites);
                    break;
                case MoviesListFragment.POPULAR:
                    getSupportActionBar().setTitle(R.string.popular_movies);
                    break;
                case MoviesListFragment.TOP_RATED:
                    getSupportActionBar().setTitle(R.string.top_rated_movies);
                    break;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (!ThisApplication.isConectedToInternet()) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
            return true;
        }
        switch (id) {
            case R.id.action_favorites:
                getSupportActionBar().setTitle(R.string.favorites);
                editor.putInt(MoviesListFragment.LIST_TYPE, MoviesListFragment.FAVORITES);
                break;
            case R.id.action_popular:
                getSupportActionBar().setTitle(R.string.popular_movies);
                editor.putInt(MoviesListFragment.LIST_TYPE, MoviesListFragment.POPULAR);
                break;
            case R.id.action_top_rated:
                getSupportActionBar().setTitle(R.string.top_rated_movies);
                editor.putInt(MoviesListFragment.LIST_TYPE, MoviesListFragment.TOP_RATED);
                break;

        }
        editor.commit();
        movieListFragment.reload();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String order = prefs.getString("sort_order", "");
        if (currentOrder == null) currentOrder = order;
        // remove details if sort order changed
        if (twoPane && !currentOrder.equals(order)) {
            if (detailsFragment == null) {
                detailsFragment = (DetailsFragment) getFragmentManager().findFragmentByTag("DETAILS");
            }
            if (detailsFragment != null) {
                getFragmentManager().beginTransaction().remove(detailsFragment).commit();
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", movie);
    }

    @Override
    public void movieSelected(MovieModel movie) {
        this.movie = movie;
        if (!twoPane) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        } else {
            detailsFragment = new DetailsFragment();
            Bundle params = new Bundle();
            params.putParcelable("movie", movie);
            detailsFragment.setArguments(params);
            getFragmentManager().beginTransaction().replace(R.id.container, detailsFragment, DETAILS).commit();

        }
    }
}
