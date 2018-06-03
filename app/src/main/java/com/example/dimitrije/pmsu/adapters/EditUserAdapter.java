package com.example.dimitrije.pmsu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.R;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dimitrije on 6/2/2018.
 */

public class EditUserAdapter extends ArrayAdapter<User> {

    private User user;
    private UserService userService;

    public EditUserAdapter(Context context, List<User> users){
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        User user = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_list, viewGroup, false);
        }

        TextView nameView = convertView.findViewById(R.id.titlePostList);
        TextView usernameView = convertView.findViewById(R.id.subTitleList);

        nameView.setText(user.getName());
        usernameView.setText(user.getUsername());

        return convertView;
    }
}
