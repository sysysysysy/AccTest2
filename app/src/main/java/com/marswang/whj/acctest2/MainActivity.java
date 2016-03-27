package com.marswang.whj.acctest2;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private MyMediaPlayer mMyMediaPlayer = new MyMediaPlayer();
    private SensorManager mSensorManager;
    private static final String WAIT_SHOW = "点击开始测试按钮采集数据";
    private float daftG = 0;
    private long[] time = new long[1024];
    private static final float PI = 3.1415926f;


    //设置采集点数
    private float[] date = new float[1024];
    private float[] outputDate = new float[1024];
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTvNoContent(WAIT_SHOW);
    }

    public void beginClick(View view) {
        // date = null;
        setTvNoContent("已经开始采集数据，请稍后。。。");
        // mMyMediaPlayer.paly(getApplicationContext(),R.raw.start);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);//
        i = 0;
    }
    //    public void readClick(View view){
    //        setTvNoContent(WAIT_SHOW);
    //        for (int i=0;i<date.length;i++){
    //            System.out.println("第"+(i+1)+"组"+date[i]);}
    //    }

    private void setTvNoContent(String string) {
        TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText(string);
    }

    public void dataShowClick(View view) {
        Intent intent = new Intent(MainActivity.this, DateList.class);
        Bundle bundle = new Bundle();
        bundle.putFloatArray(DateList.DATA_LIST, date);
        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
        setTvNoContent(WAIT_SHOW);
    }

    public void writeSDCard(View view) throws IOException {
        byte[] c = {0x0d, 0x0a};
        File dir = Environment.getExternalStorageDirectory();
        File newFile = new File(dir, "data.txt");
        FileOutputStream fos = new FileOutputStream(newFile, false);
        //        OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
        for (int i = 0; i < date.length; i++) {
            String temp = (i + 1) + " " + date[i] + " ";
            fos.write(temp.getBytes());
            fos.write(c);


        }

        fos.close();

    }

    public void writeSDCard2(View view) throws IOException {
        byte[] c = {0x0d, 0x0a};
        outputDate = new float[date.length];
        FFT fft = new FFT();
        float[] dataTemp = new float[date.length];
        //此处添加dataTemp复制data数组中值，防止在排序后data数组值混乱，导致fft操作后重绘时域波形，发生错误。
        System.arraycopy(date, 0, dataTemp, 0, dataTemp.length);
        outputDate = fft.i2Sort(dataTemp, 10);
        outputDate = fft.myFFT(outputDate, 10);
        File dir = Environment.getExternalStorageDirectory();
        File newFile = new File(dir, "data2.txt");
        FileOutputStream fos = new FileOutputStream(newFile, false);
        //        OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
        for (int i = 0; i < outputDate.length; i++) {
            String temp = (i + 1) + " " + outputDate[i] + " ";
            fos.write(temp.getBytes());
            fos.write(c);


        }

        fos.close();


    }

    public float getAverange(float[] date) {

        float sum = 0;
        float avg = 0;
        for (float aDate : date) {
            sum += aDate;
        }
        avg = sum / date.length;
        daftG = avg;
        return avg;
    }

    public void getStaticAvg(View view) {
        double avg = getAverange(date);
        Toast.makeText(this, "静止状态采样均值为" + avg, Toast.LENGTH_SHORT).show();
    }

    public void drawCharClick(View view) {
        Intent intent = new Intent(MainActivity.this, LineChartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putFloatArray(LineChartActivity.DATA_DRAWCHART, date);
        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
        setTvNoContent(WAIT_SHOW);
    }

    public void drawCharClick2(View view) {
        Intent intent = new Intent(MainActivity.this, LineChartActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putFloatArray(LineChartActivity2.DATA_DRAWCHART, outputDate);
        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
        setTvNoContent(WAIT_SHOW);
    }

    public void test(View view) {
        for (int i = 0; i < date.length; i++) {
            float tempY = (float)(Math.sin(2 * PI *15*i * 0.01) ) ;//(Math.sin(2 * PI * 15 * i * 0.01) + 2 * Math.sin(2 * PI * 40 * i * 0.01));
            date[i] = tempY;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (i < date.length) {
            float x = event.values[2];
            date[i] = x - daftG;
            time[i] = System.currentTimeMillis();
            i++;
        } else {
            long timeTotle = time[time.length - 1] - time[0];
            System.out.println(timeTotle + "共用时间");
            setTvNoContent("采集完毕");
            mMyMediaPlayer.paly(getApplication(), R.raw.over);
            mSensorManager.unregisterListener(this);


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
