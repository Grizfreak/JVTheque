package com.jv.theque.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jv.theque.favoritesImplementation.FavoriteSearch;
import com.jv.theque.MainActivity;
import com.jv.theque.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView displayList;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        displayList = view.findViewById(R.id.displayList);
        displayList.setOnItemClickListener((adapterView, view12, i, l) -> {
            FavoriteSearch a = MainActivity.userData.getUserFavorites().getList().get(i);
            a.changeFragment((AppCompatActivity) view12.getContext());
        });
        displayList.setOnItemLongClickListener((parent, view1, position, id) -> {
            MainActivity.userData.getUserFavorites().getList().remove(position);
            ArrayAdapter<FavoriteSearch> arrayAdapter = new ArrayAdapter<FavoriteSearch>(view1.getContext(), android.R.layout.simple_list_item_1, MainActivity.userData.getUserFavorites().getList());
            displayList.setAdapter(arrayAdapter);
            Toast.makeText(view1.getContext(),"Le favori a été supprimé",Toast.LENGTH_SHORT).show();
            return false;
        });
        ArrayAdapter<FavoriteSearch> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, MainActivity.userData.getUserFavorites().getList());
        displayList.setAdapter(arrayAdapter);
        return view;
    }

}