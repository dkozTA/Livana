package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.EditText;

public class WishlistFragment extends Fragment implements WishlistFolderAdapter.OnFolderClickListener {
    private RecyclerView recyclerView;
    private PostAdapter allHousesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        // Setup folders RecyclerView
        RecyclerView foldersRecycler = view.findViewById(R.id.wishlist_recycler);
        foldersRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        WishlistFolderAdapter folderAdapter = new WishlistFolderAdapter(
                WishlistManager.getInstance().getFolders(),
                this
        );
        foldersRecycler.setAdapter(folderAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_folder);
        fab.setOnClickListener(v -> showCreateFolderDialog());

        return view;
    }

    @Override
    public void onFolderClick(WishlistFolder folder) {
        // Replace current fragment with FolderDetailFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, FolderDetailFragment.newInstance(folder))
                .addToBackStack(null)
                .commit();
    }

    private void showCreateFolderDialog() {
        EditText input = new EditText(getContext());
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Create New Folder")
                .setView(input)
                .setPositiveButton("Create", (dialog, which) -> {
                    String folderName = input.getText().toString();
                    if (!folderName.isEmpty()) {
                        WishlistManager.getInstance().createFolder(folderName);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}