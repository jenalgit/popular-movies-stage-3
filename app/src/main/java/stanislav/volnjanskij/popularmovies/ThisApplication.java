package stanislav.volnjanskij.popularmovies;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.otto.Bus;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import stanislav.volnjanskij.popularmovies.db.DaoMaster;

import stanislav.volnjanskij.popularmovies.db.DaoSession;
import stanislav.volnjanskij.popularmovies.db.MoviesContentProvider;

/**
 * Created by Stas on 12.09.15.
 */
public class ThisApplication extends Application {

    static ThisApplication instance;
    static DaoSession daoSession;
    static Bus bus;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession=daoMaster.newSession();
        MoviesContentProvider.daoSession =daoSession;


//picasso tuning
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

        instance = this;
        bus = new Bus();
    }

    public static boolean isConectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public static DaoSession getDaoSession(){
        return daoSession;
    }

    public static Bus getEventBus() {
        return bus;
    }
}
