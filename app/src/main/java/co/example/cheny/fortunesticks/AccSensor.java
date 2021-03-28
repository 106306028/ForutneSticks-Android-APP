package co.example.cheny.fortunesticks;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccSensor implements SensorEventListener {

    public interface SensorListener{
        void onNewShake();
    }

    private SensorListener listener;

    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private double mSpeed;                 //甩動力道數度
    private long mLastUpdateTime;           //觸發時間

    //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private static final int SPEED_SHRESHOLD = 3000;

    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;
    public AccSensor(Context context){

        //取得體感(Sensor)服務使用權限
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void start() {
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void setListener(SensorListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //當前觸發時間
        long mCurrentUpdateTime = System.currentTimeMillis();

        //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
        long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;

        //若觸發間隔時間< 70 則return;
        if (mTimeInterval < UPTATE_INTERVAL_TIME) return;

        mLastUpdateTime = mCurrentUpdateTime;

        //取得xyz體感(Sensor)偏移
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
        float mDeltaX = x - mLastX;
        float mDeltaY = y - mLastY;
        float mDeltaZ = z - mLastZ;

        mLastX = x;
        mLastY = y;
        mLastZ = z;

        //體感(Sensor)甩動力道速度公式
        mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;

        if(listener!=null && isShook()){
            listener.onNewShake();

        }

    }
    public boolean isShook(){
        boolean state = false;
        if(mSpeed >= SPEED_SHRESHOLD){
            state = true;
        }
        return state;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
