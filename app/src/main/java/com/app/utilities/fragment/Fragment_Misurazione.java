package com.app.utilities.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.app.utilities.R;
import com.app.utilities.utility.CustomAdapter;
import com.app.utilities.utility.RowItem;
import com.app.utilities.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Misurazione extends Fragment {
    private final Utils utils = new Utils();
    String[] orientazioneTextsView;
    TypedArray orientazioneImagesView;
    CustomAdapter adapter;
    List<RowItem> rowItems;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__sorte, container, false);
        rowItems = new ArrayList<>();
        orientazioneImagesView = getResources().obtainTypedArray(R.array.orientazione_images);
        orientazioneTextsView = getResources().getStringArray(R.array.orientazione_strings);
        for (int i = 0; i < orientazioneTextsView.length; i++) {
            RowItem item = new RowItem(orientazioneTextsView[i],
                    orientazioneImagesView.getResourceId(i, -1));
            rowItems.add(item);
        }
        listView = view.findViewById(R.id.sorteListView);
        adapter = new CustomAdapter(requireActivity(), rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position == 0) {
                utils.goToARMeasureActivity(requireActivity());
            } else if (position == 1) {
                utils.goToLivellaActivity(requireActivity());
            } else if (position == 2) {
                utils.goToBussolaActivity(requireActivity());
            } else if (position == 3) {
                utils.goToInclinometroActivity(requireActivity());
            }
        });
        return view;
    }
}