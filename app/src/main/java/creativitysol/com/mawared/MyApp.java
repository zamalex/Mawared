package creativitysol.com.mawared;


import io.paperdb.Paper;

public class MyApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Paper.init(this);

    }
}
