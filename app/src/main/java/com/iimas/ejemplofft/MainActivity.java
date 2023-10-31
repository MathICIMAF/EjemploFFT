package com.iimas.ejemplofft;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    LineChart lineChart;
    int N = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.lineChart);

        EditText mEdit = findViewById(R.id.mEdit);
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int m = Integer.parseInt(charSequence.toString());
                    mostrarFFT(m);
                    lineChart.invalidate();
                }
                catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mostrarFFT(5);
    }

    private void mostrarFFT(int m1){
        DoubleFFT_1D fft = new DoubleFFT_1D(N);
        double[] data = funcionSeno(N, m1);
        double [] data1 = funcionCoseno(N, 100);
        for (int i = 0; i < data.length; i++)
            data[i]+=data1[i];
        double [] fft_data = new double[data.length*2];
        System.arraycopy(data, 0, fft_data, 0, data.length);
        fft.realForward(fft_data);
        mostrarGrafico(valorAbsFFT(fft_data));
    }

    private double[] parteRealFFT(double[] fft){
        double [] res = new double[fft.length/4];
        for (int i = 0; i < fft.length/4; i++){
            res[i] = fft[2*i];
        }

        return res;
    }

    double[] funcionSeno(int N, int M){
        double [] res = new double[N];
        for (int i = 0; i < N; i++)
            res[i] = Math.sin(2*Math.PI*i*M/N);
        return res;
    }

    double[] funcionCoseno(int N, int M){
        double [] res = new double[N];
        for (int i = 0; i < N; i++)
            res[i] = 0.25*Math.cos(2*Math.PI*i*M/N);
        return res;
    }



    private double[] parteImagFFT(double[] fft){
        double [] res = new double[fft.length/4];
        for (int i = 0; i < fft.length/4; i++){
            res[i] = fft[2*i+1];
        }

        return res;
    }

    private double[] valorAbsFFT(double[] fft){
        double [] res = new double[fft.length/4];
        for (int i = 0; i < fft.length/4; i++){
            double real = fft[2*i];
            double imag = fft[2*i+1];
            res[i] = Math.sqrt(real*real + imag*imag);
        }

        return res;
    }

    private void mostrarGrafico(double[] points) {

        lineChart.setBackgroundColor(Color.BLACK);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < points.length; i++){
            float y = (float) points[i];
            Entry entry = new Entry(i,y);
            entries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(entries,"");
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawCircles(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

    }
}