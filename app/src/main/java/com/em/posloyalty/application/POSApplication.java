package com.em.posloyalty.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by USER on 5/13/2015.
 */
public class POSApplication extends Application
{
    public DaoSession daoSession;


    @Override
    public void onCreate()
    {
        super.onCreate();
        setupDatabase();
        initImageLoader();
    }

    private void setupDatabase()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "posloyaltydb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public DaoSession getDaoSession()
    {
        if (daoSession != null)
        {
            return daoSession;
        } else
        {
            setupDatabase();
            return daoSession;
        }
    }


    public void initImageLoader()
    {

        // Catche
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Initialize ImageLoader with configuration.
        if (ImageLoader.getInstance().isInited() == false)
        {
            ImageLoader.getInstance().init(config);
        }
    }
}
