package com.simple.practice.PieView;

/**
 * Created by hych on 2017/5/4 10:03
 */

public class PieData {

    //用户关心的数据
    private String name;        //名字
    private float value;        //数值
    private float percentage;   //百分比

    //非用户关心的数据
    private int clor = 0;       //颜色
    private float angle = 0;    //角度

    public PieData(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getClor() {
        return clor;
    }

    public void setClor(int clor) {
        this.clor = clor;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "PieData{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", percentage=" + percentage +
                ", clor=" + clor +
                ", angle=" + angle +
                '}';
    }
}
