package com.tang.commodityadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tang.commodityadmin.R;

/*
 *文件名: UserFragment
 *创建者: 醉意丶千层梦
 *创建时间:2021/12/18 16:43
 *描述: 这是一个示例
 */
public class UserFragment extends Fragment {
    public UserFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
}
