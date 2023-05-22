package com.fashionstore.fashion.fragment;



import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fashionstore.fashion.R;
import com.fashionstore.fashion.activity.ChangePasswordActivity;
import com.fashionstore.fashion.activity.EditProfileActivity;
import com.fashionstore.fashion.activity.SignInActivity;
import com.fashionstore.fashion.prefs.DataStoreManager;
import com.fashionstore.fashion.utils.GlobalFuntion;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

   private View view;
   private TextView vt_edit, tv_change_password, usernameTextView, tv_log_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_account, container, false);

        vt_edit = view.findViewById(R.id.vt_edit);
        tv_change_password = view.findViewById(R.id.tv_change_password);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        usernameTextView.setText(DataStoreManager.getUser().getEmail());
        tv_log_out = view.findViewById(R.id.tv_log_out);

        tv_log_out.setOnClickListener(v-> onClickSignOut());

        vt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        FirebaseAuth.getInstance().signOut();
        DataStoreManager.setUser(null);
        GlobalFuntion.startActivity(getActivity(), SignInActivity.class);
        getActivity().finishAffinity();
    }

}