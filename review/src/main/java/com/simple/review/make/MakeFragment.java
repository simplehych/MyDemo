package com.simple.review.make;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simple.review.R;

/**
 * @author hych
 * @date 2018/8/3 10:49
 */
public class MakeFragment extends Fragment {

    private String toastTip = "toast in MyFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void methodWithGlobalVariable() {
        Toast.makeText(getContext(), toastTip, Toast.LENGTH_SHORT).show();
    }

    public void methodWithLocalVariable() {
        String logMessage = "log in MyFragment";
        logMessage = logMessage.toLowerCase();
        System.out.println(logMessage);
    }
}
