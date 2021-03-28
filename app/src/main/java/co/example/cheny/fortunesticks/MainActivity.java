package co.example.cheny.fortunesticks;

import android.app.Service;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private AccSensor accSensor;
    private String[] Lucky = {"大吉","中吉","小吉","吉","末吉","凶","大凶"};
    private String[] Content = {"物極必反，小心樂極生悲!","吉星高照，走路記得盯著地上!","心懷善念，就會有好事發生!","樸實無華，願日日歲月靜好","五年運轉，二十翻盤!","時乖運舛，小心踩到狗大便!","否極泰來，危機就是轉機!"};
    private int[] stick = {R.drawable.dachi, R.drawable.zongchi, R.drawable.xiaochi, R.drawable.chi, R.drawable.mochi, R.drawable.xoung, R.drawable.daxoung};
    public SQLiteManager mSQL = null;

    private ImageView stickImage;
    private ImageView temple;
    private TextView horoscope;
    private TextView quotation;
    private TextView shake;
    private TextView shake2;
    private Button button ;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horoscope = (TextView) findViewById(R.id.horoscope);
        quotation = (TextView) findViewById(R.id.content);
        shake = (TextView) findViewById(R.id.shake);
        shake2 = (TextView) findViewById(R.id.shake2);
        stickImage = (ImageView) findViewById(R.id.stick);
        temple = findViewById(R.id.temple);
        button = (Button) findViewById(R.id.button);
        mp = MediaPlayer.create(this.getBaseContext(), R.raw.bell2);
        //設定傳感器
        setAccSensor();

        //寫入資料
        mSQL = new SQLiteManager(this);
        for (int i = 0; i < Lucky.length; i++) {
            mSQL.insert(Lucky[i], Content[i], stick[i]);
        }
        mSQL.close();

        //設置事件傾聽者
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horoscope.setText(getString(R.string.horoscope));
                quotation.setText(getString(R.string.instruction));
                accSensor.start();
                button.setVisibility(View.INVISIBLE);
                shake.setVisibility(View.VISIBLE);
                shake2.setVisibility(View.VISIBLE);
                stickImage.setVisibility(View.INVISIBLE);
                temple.setVisibility(View.VISIBLE);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        accSensor.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accSensor.stop();
    }

    private void setAccSensor(){
        accSensor = new AccSensor(this);
        AccSensor.SensorListener sl = new AccSensor.SensorListener() {

            @Override
            public void onNewShake(){
                getStick();
                setVibrate(500);
            }
        };
        accSensor.setListener(sl);
    }

    private void getStick(){
        //隨機抽籤
        int stickNumber = (int)(Math.random()*7);
        SQLiteManager mSQLiteDB = new SQLiteManager(MainActivity.this);
        //取得資料庫的指標
        Cursor mCursor = mSQLiteDB.select();
        //將指標滑動到第一筆，取第一筆資料
        mCursor.moveToPosition(stickNumber);
        String lucky = mCursor.getString(mCursor.getColumnIndex(mSQLiteDB.LUCKY));
        String content = mCursor.getString(mCursor.getColumnIndex(mSQLiteDB.CONTENT));
        int stickResource = mCursor.getInt(mCursor.getColumnIndex(mSQLiteDB.STICK));
        horoscope.setText(lucky);
        quotation.setText(content);
        stickImage = (ImageView) findViewById(R.id.stick);
        stickImage.setVisibility(View.VISIBLE);
        stickImage.setImageResource(stickResource);
        temple.setVisibility(View.INVISIBLE);
        accSensor.stop();
        button.setVisibility(View.VISIBLE);
        shake.setVisibility(View.INVISIBLE);
        shake2.setVisibility(View.INVISIBLE);
        mp.start();

    }

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }


}
