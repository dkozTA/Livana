package com.example.myapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.myapplication.R;
import com.example.myapplication.ui.misc.Post;
import com.example.myapplication.ui.misc.WishlistManager;

public class DialogUtils {

    public static void showCreateWishlistDialog(Context context, Post post, Runnable onComplete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_wishlist, null);
        builder.setView(dialogView);

        EditText inputName = dialogView.findViewById(R.id.editWishlistName);
        Button btnCreate = dialogView.findViewById(R.id.btnCreate);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);
        TextView btnClear = dialogView.findViewById(R.id.btnClear);
        TextView counter = dialogView.findViewById(R.id.textCounter);

        AlertDialog dialog = builder.create();
        dialog.show();

        inputName.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                counter.setText(s.length() + "/50 characters");
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnClear.setOnClickListener(v -> inputName.setText(""));

        btnCreate.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();

            if (name.isEmpty()) {
                inputName.setError("Please enter a name");
                return;
            }

            boolean exists = WishlistManager.getInstance().getFolders()
                    .stream()
                    .anyMatch(folder -> folder.getName().equalsIgnoreCase(name));

            if (exists) {
                inputName.setError("This folder already exists");
                return;
            }

            WishlistManager.getInstance().createFolder(name);
            WishlistManager.getInstance().getFolders()
                    .get(WishlistManager.getInstance().getFolders().size() - 1)
                    .addPost(post);

            dialog.dismiss();

            if (onComplete != null) {
                onComplete.run();
            }
        });
    }
}
