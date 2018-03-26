package com.a1.chm.myapplication.dao;


import android.content.Context;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.EncryptedDatabase;

import java.util.List;

import static com.google.common.net.HttpHeaders.UPGRADE;


public class DBManager {
    private static DBManager instance;
    private static Context mContext;

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private StudentMsgBeanDao mStudentMsgBeanDao;
    private static final String TAG = "DBManager";

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context,
                    "test.db", null);
            daoMaster = new DaoMaster(helper.getEncryptedWritableDb("1234"));
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private DBManager() {
    }

    public static void init(Context context) {
        mContext = context;
        instance = new DBManager();
        // 数据库对象
        DaoSession daoSession = getDaoSession(mContext);
        instance.setMatterBeanDao(daoSession.getStudentMsgBeanDao());


    }

    private void setMatterBeanDao(StudentMsgBeanDao studentMsgBeanDao) {
        this.mStudentMsgBeanDao = studentMsgBeanDao;
    }

    public StudentMsgBeanDao getMatterBeanDao() {
        return mStudentMsgBeanDao;
    }

    public static DBManager getInstance() {
        return instance;
    }

    public void insert() {
        StudentMsgBeanDao msgBeanDao = daoSession.getStudentMsgBeanDao();
        StudentMsgBean studentMsgBean = new StudentMsgBean();
        studentMsgBean.setName("zone");
        studentMsgBean.setStudentNum("123456");
        msgBeanDao.insert(studentMsgBean);
    }

    public void delete() {
        StudentMsgBeanDao dao = daoSession.getStudentMsgBeanDao();
        List<StudentMsgBean> list = dao.queryBuilder()
                .build().list();
        for (int i = 0; i < list.size(); i++) {
            Log.d("zoneLog", "studentNumber: " + list.get(i).getStudentNum());
            Log.d("zoneLog", "name: " + list.get(i).getName());
            if (i == 0) {
                dao.deleteByKey(list.get(0).getId());//通过 Id 来删除数据
                //                        msgBeanDao.delete(list.get(0));//通过传入实体类的实例来删除数据
            }

        }
    }

    public void update() {
        StudentMsgBeanDao dao = daoSession.getStudentMsgBeanDao();
        List<StudentMsgBean> list = dao.queryBuilder()
                .build().list();
        for (int i = 0; i < list.size(); i++) {
            Log.d("zoneLog", "studentNumber: " + list.get(i).getStudentNum());
            Log.d("zoneLog", "name: " + list.get(i).getName());
            if (i == 0) {
                list.get(0).setName("zone==========>");
                dao.update(list.get(0));
            }
        }
    }

    public List<StudentMsgBean> query() {
        StudentMsgBeanDao dao = daoSession.getStudentMsgBeanDao();
        List<StudentMsgBean> list = dao.queryBuilder()
                .offset(1)//偏移量，相当于 SQL 语句中的 skip
                .limit(3)//只获取结果集的前 3 个数据
                .orderAsc(StudentMsgBeanDao.Properties.StudentNum)//通过 StudentNum 这个属性进行正序排序
                .where(StudentMsgBeanDao.Properties.Name.eq("zone"))//数据筛选，只获取 Name = "zone" 的数据。
                .build()
                .list();
        return list;
    }

    //2表联查
    public void queryTwo() {
        StudentMsgBeanDao dao = daoSession.getStudentMsgBeanDao();
        ScoreBeanDao scoreBeanDao = daoSession.getScoreBeanDao();
        //              存入一个数据
        StudentMsgBean studentMsgBean = new StudentMsgBean();
        studentMsgBean.setName("zone");
        studentMsgBean.setStudentNum("123456");
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setEnglishScore("120");
        scoreBean.setMathScore("1000");
        scoreBeanDao.insert(scoreBean);
        ScoreBean scoreBean1 = scoreBeanDao.queryBuilder().unique();
        if (scoreBean1 != null) {
            studentMsgBean.setScoreId(scoreBean1.getId());
            studentMsgBean.setMScoreBean(scoreBean);
            dao.insert(studentMsgBean);
        }
        //                查询数据
        List<StudentMsgBean> list = dao.queryBuilder().list();
        for (int i = 0; i < list.size(); i++) {
            Log.d("zoneLog", "studentNumber: " + list.get(i).getStudentNum());
            Log.d("zoneLog", "name: " + list.get(i).getName());
            Log.d("zoneLog", "english: " + list.get(i).getMScoreBean().getEnglishScore());
            Log.d("zoneLog", "math: " + list.get(i).getMScoreBean().getMathScore());
        }
    }

    public void queryMore() {
        AuthorDao authorDao = daoSession.getAuthorDao();
        PostDao postDao = daoSession.getPostDao();

        Author author = new Author();//存贮一个作者
        author.setName("zone");
        author.setSex("boy");
        authorDao.insert(author);
        Author authorByQuery = authorDao.queryBuilder().where(AuthorDao.Properties.Name.eq("zone"), AuthorDao.Properties.Sex.eq("boy")).unique();

        Post firstPost = new Post();//写一篇文章
        firstPost.setAuthorId(authorByQuery.getId());
        firstPost.setContent("第一篇文章！");
        Post secondPost = new Post();//写一篇文章
        secondPost.setAuthorId(authorByQuery.getId());
        secondPost.setContent("第二篇文章！");
        postDao.insertInTx(firstPost, secondPost);//存储文章

        Author authorResult = authorDao.queryBuilder().where(AuthorDao.Properties.Name.eq("zone"), AuthorDao.Properties.Sex.eq("boy")).unique();//查询存储的结果
        Log.d(TAG, authorResult.getName());
        Log.d(TAG, authorResult.getSex());
        for (int i = 0; i < authorResult.getPosts().size(); i++) {
            Log.d(TAG, authorResult.getPosts().get(i).getContent());
        }
    }


    /*
     未成功记录插入数据库
     */
    //    public void insertMatter(MatterBean matterBean) {
    //        MatterBeanDao matterBeanDao = daoSession.getMatterBeanDao();
    //        QueryBuilder<MatterBean> qb = matterBeanDao.queryBuilder();
    //        if (matterBean.getProblem() != null) {
    //            long count = qb.where(qb.and(MatterBeanDao.Properties.Id.eq(matterBean.getId()),
    //                    MatterBeanDao.Properties.Problem.eq(matterBean.getProblem())))
    //                    .count();
    //            if (count == 0) {
    //                matterBeanDao.insertOrReplace(matterBean);
    //            }
    //        }
    //    }
    //
    //
    //    /*
    //     未成功记录插入数据库
    //     */
    //    public void insertMatters(List<MatterBean> list) {
    //
    //        if (list != null) {
    //            Iterator<MatterBean> it = list.iterator();
    //            MatterBeanDao matterBeanDao = daoSession.getMatterBeanDao();
    //            QueryBuilder<MatterBean> qb = matterBeanDao.queryBuilder();
    //            while (it.hasNext()) {
    //                MatterBean matterBean = it.next();
    //                if (matterBean.getProblem() != null) {
    //                    long count = qb.where(qb.and(MatterBeanDao.Properties.Id.eq(matterBean.getId()),
    //                            MatterBeanDao.Properties.Problem.eq(matterBean.getProblem())))
    //                            .count();
    //                    if (count == 0) {
    //                        matterBeanDao.insertOrReplace(matterBean);
    //                    }
    //                }
    //            }
    //        }
    //    }
    //
    //    /**
    //     * 获取未报事成功的记录
    //     *
    //     * @return
    //     */
    //    public List<MatterBean> getAllMatters() {
    //        MatterBeanDao matterBeanDao = daoSession.getMatterBeanDao();
    //        QueryBuilder<MatterBean> qb = matterBeanDao.queryBuilder();
    //        //List<Tune> list = qb.list();
    //        List<MatterBean> list = qb.list();
    //        return list;
    //    }
    //
    //
    //    public void deleteAllMatter() {
    //        MatterBeanDao matterBeanDao = daoSession.getMatterBeanDao();
    //        QueryBuilder<MatterBean> qb = matterBeanDao.queryBuilder();
    //        matterBeanDao.deleteAll();
    //    }
    //
    //    /*
    //    报事描述相同就认为时同一条报事
    //     */
    //    public void deleteMatter(String problem, int id) {
    //        MatterBeanDao matterBeanDao = daoSession.getMatterBeanDao();
    //        QueryBuilder<MatterBean> qb = matterBeanDao.queryBuilder();
    //        if (problem != null) {
    //            List<MatterBean> list = qb.where(qb.and(MatterBeanDao.Properties.Id.eq(id),
    //                    MatterBeanDao.Properties.Problem.eq(problem))).list();
    //            if (list != null && list.size() > 0) {
    //                for (MatterBean matterBean : list) {
    //                    matterBeanDao.delete(matterBean);
    //                }
    //            }
    //        }
    //    }

    public static class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
        //    public static class MySQLiteOpenHelper extends SQLiteOpenHelper {

        private final Context context;
        private final String name;
        private final int version = DaoMaster.SCHEMA_VERSION;

        private boolean loadSQLCipherNativeLibs = true;

        public MySQLiteOpenHelper(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
            this.context=context;
            this.name=name;
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {

//            EncryptedMigrationHelper.migrate((EncryptedDatabase) db,AreaDao.class, PeopleDao.class, ProductDao.class);
            if(oldVersion!=newVersion) {
                MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },AuthorDao.class,PostDao.class,StudentMsgBeanDao.class,ScoreBeanDao.class,TestDao.class
                ,Test1Dao.class);
                Log.e(UPGRADE,"upgrade run success");
            }else
                Log.w(TAG, "onUpgrade: it was new" );

        }

        @Override
        public Database getEncryptedWritableDb(String password) {
            MyEncryptedHelper encryptedHelper = new MyEncryptedHelper(context,name,version,loadSQLCipherNativeLibs);
            return encryptedHelper.wrap(encryptedHelper.getReadableDatabase(password));
        }

        private class MyEncryptedHelper extends net.sqlcipher.database.SQLiteOpenHelper {
            public MyEncryptedHelper(Context context, String name, int version, boolean loadLibs) {
                super(context, name, null, version);
                if (loadLibs) {
                    net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
                }
            }

            @Override
            public void onCreate(net.sqlcipher.database.SQLiteDatabase db) {
                MySQLiteOpenHelper.this.onCreate(wrap(db));
            }

            @Override
            public void onUpgrade(net.sqlcipher.database.SQLiteDatabase db, int oldVersion, int newVersion) {
                MySQLiteOpenHelper.this.onUpgrade(wrap(db), oldVersion, newVersion);
            }

            @Override
            public void onOpen(net.sqlcipher.database.SQLiteDatabase db) {
                MySQLiteOpenHelper.this.onOpen(wrap(db));
            }

            protected Database wrap(net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
                return new EncryptedDatabase(sqLiteDatabase);
            }
        }
    }


}
