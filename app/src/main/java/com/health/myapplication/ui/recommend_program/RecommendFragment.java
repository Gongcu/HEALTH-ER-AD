package com.health.myapplication.ui.recommend_program;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.health.myapplication.R;
import com.health.myapplication.model.etc.ExerciseModel;
import com.health.myapplication.util.JsonParser;

import java.util.List;

public class RecommendFragment extends Fragment {
    private TextView textView;
    private RecyclerView recyclerView;
    private GuideListAdapter adapter;

    private List<ExerciseModel> list;

    private static final int BEGINNER=1;
    private static final int NOVICE=2;
    private static final int INTERMEDIATE=3;
    private static final int EXPERT=4;

    private final String[] novice = {"가슴, 등", "어깨, 하체"};
    private final String[] intermediate = {"가슴, 삼두", "등, 이두","하체, 어깨"};
    private final String[] expert = {"가슴, 삼두", "등, 이두","어깨, 복근","하체"};
    private String part;

    public RecommendFragment() {
    }


    public static RecommendFragment newInstance(int TYPE, int LEVEL) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putInt("type",TYPE);
        args.putInt("level", LEVEL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int TYPE=getArguments().getInt("type");
        int LEVEL=getArguments().getInt("level");

        switch (LEVEL) {
            case BEGINNER:
                list = JsonParser.INSTANCE.getRecommendProgramInfo(requireContext(),"무분할");
                part = "전신 운동";
                break;
            case NOVICE:
                list = JsonParser.INSTANCE.getRecommendProgramInfo(requireContext(),"초보자"+TYPE);
                part = novice[TYPE-1];
                break;
            case INTERMEDIATE:
                list = JsonParser.INSTANCE.getRecommendProgramInfo(requireContext(),"3분할"+TYPE);
                part = intermediate[TYPE-1];
                break;
            case EXPERT:
                list = JsonParser.INSTANCE.getRecommendProgramInfo(requireContext(),"4분할"+TYPE);
                part = expert[TYPE-1];
                break;
        }
        adapter = new GuideListAdapter(list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        textView = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recyclerView);

        textView.setText(part);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
