/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.whatsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.upc.whatsapp.R;
import edu.upc.whatsapp._GlobalState;
import edu.upc.whatsapp.e_MessagesActivity;
import entity.UserInfo;

import java.util.List;

/**
 * @author upcnet
 */
public class MyAdapter_users extends BaseAdapter implements AdapterView.OnItemClickListener {

    _GlobalState globalState;
    Context context;
    public List<UserInfo> users;

    public MyAdapter_users(Context context, List<UserInfo> users, _GlobalState globalState) {
        this.globalState = globalState;
        this.context = context;
        this.users = users;
    }

    public int getCount() {
        return users.size();
    }

    public Object getItem(int arg0) {
        return users.get(arg0);
    }

    public long getItemId(int arg0) {
        return users.get(arg0).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.row_twotextviews, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.row_twotextviews_name))
                .setText(((UserInfo) getItem(position)).getName());
        ((TextView) convertView.findViewById(R.id.row_twotextviews_surname))
                .setText(((UserInfo) getItem(position)).getSurname());

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        globalState.user_to_talk_to = (UserInfo) getItem(position);
        Intent intent = new Intent(context, e_MessagesActivity.class);
        intent.putExtra("user_id", globalState.user_to_talk_to.getId());

        // Add the FLAG_ACTIVITY_NEW_TASK flag if the context is not an Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

    }
}
