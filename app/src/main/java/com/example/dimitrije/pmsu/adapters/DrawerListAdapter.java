package com.example.dimitrije.pmsu.adapters;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dimitrije.pmsu.R;

import java.util.ArrayList;
import com.example.dimitrije.pmsu.model.NavItem;

/**
 * Created by Dimitrije on 4/17/2018.
 */

public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_post_list, null);
        }else{
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.titlePost);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.iconPost);

        titleView.setText(mNavItems.get(position).getmTitle());
        subtitleView.setText(mNavItems.get(position).getmSubtitle());
        iconView.setImageResource(mNavItems.get(position).getmIcon());
        return view;
    }

}
