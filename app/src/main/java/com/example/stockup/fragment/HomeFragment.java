package com.example.stockup.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.stockup.R;
import com.example.stockup.adapter.RecyclerAdapter;
import com.example.stockup.model.Habit;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    final int RECYCLER_SPAN_COUNT = 2;
    RecyclerView mRecyclerView;
    ArrayList<Habit> mHabits = new ArrayList<Habit>(Arrays.asList(new Habit("Habit_1",23), new Habit("Habit_2", 63)));

    String TAG = getClass().toString();
    int FLIP_INTERVAL = 1000 * 3; //In milliseconds
    ViewFlipper mOfferFlipper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_auth,container,false);

        /*mRecyclerView = v.findViewById(R.id.home_recycler);
        mRecyclerView.setAdapter(new RecyclerAdapter(mHabits));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),RECYCLER_SPAN_COUNT));*/

        return v;
    }


}
