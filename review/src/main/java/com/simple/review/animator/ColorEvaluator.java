package com.simple.review.animator;

import android.animation.TypeEvaluator;

/**
 * @author hych
 * @date 2018/8/10 08:32
 */
public class ColorEvaluator implements TypeEvaluator<String> {

    private int mCurrentRed = -1;
    private int mCurrentGreen = -1;
    private int mCurrentBlue = -1;

    @Override
    public String evaluate(float fraction, String startValue, String endValue) {
        int startRed = Integer.parseInt(startValue.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startValue.substring(3, 5), 16);
        int startBlue = Integer.parseInt(startValue.substring(5, 7), 16);
        int endRed = Integer.parseInt(endValue.substring(1, 3), 16);
        int endGreen = Integer.parseInt(endValue.substring(3, 5), 16);
        int endBlue = Integer.parseInt(endValue.substring(5, 7), 16);

        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }

        int redDiff = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff + greenDiff + blueDiff;
        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
        } else if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, redDiff, fraction);
        } else if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff, redDiff + greenDiff, fraction);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(mCurrentRed);
        stringBuilder.append(mCurrentGreen);
        stringBuilder.append(mCurrentBlue);
        return stringBuilder.toString();
    }


    private int getCurrentColor(int startColor, int endColor,
                                int colorDiff, int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - (fraction * colorDiff - offset));
            if (currentColor < endColor) {
                currentColor = endColor;
            }
        } else {
            currentColor = (int) (startColor + (startColor + (fraction * colorDiff - offset)));
            if (currentColor > endColor) {
                currentColor = endColor;
            }
        }
        return currentColor;

    }

    private String getHexString(int value) {
        StringBuilder stringBuilder = new StringBuilder();
        String hexString = Integer.toHexString(value);
        stringBuilder.append(hexString);
        if (hexString.length() == 1) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

}
