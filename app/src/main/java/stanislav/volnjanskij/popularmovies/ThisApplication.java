package stanislav.volnjanskij.popularmovies;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import stanislav.volnjanskij.popularmovies.db.DaoMaster;

import stanislav.volnjanskij.popularmovies.db.DaoSession;
import stanislav.volnjanskij.popularmovies.db.MoviesContentProvider;

/**
 * Created by Stas on 12.09.15.
 */
public class ThisApplication extends Application {

    static ThisApplication instance;
    static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession=daoMaster.newSession();
        MoviesContentProvider.daoSession =daoSession;


        instance = this;
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
}
