package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapters.PostAdapter;
import com.example.myapplication.ui.misc.WishlistFolder;

public class FolderDetailFragment extends Fragment {
    private WishlistFolder folder;

    public static FolderDetailFragment newInstance(WishlistFolder folder) {
        FolderDetailFragment fragment = new FolderDetailFragment();
        fragment.folder = folder;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder_detail, container, false);

        ImageButton backButton = view.findViewById(R.id.btnBack);
        backButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        ImageButton closeButton = view.findViewById(R.id.btnClose);
        closeButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        TextView folderNameText = view.findViewById(R.id.folder_name);
        folderNameText.setText(folder.getName());

        RecyclerView recyclerView = view.findViewById(R.id.houses_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        PostAdapter adapter = new PostAdapter(getContext(), folder.getPosts(), true);
        recyclerView.setAdapter(adapter);

        return view;
    }
}