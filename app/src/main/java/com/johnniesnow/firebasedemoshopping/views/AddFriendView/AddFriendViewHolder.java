package com.johnniesnow.firebasedemoshopping.views.AddFriendView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.entities.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_user_userName)
    TextView userEmail;

    @BindView(R.id.list_user_itemView)
    public ImageView userItemView;

    public AddFriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void populate(User user){
        itemView.setTag(user);
        userEmail.setText(user.getEmail());
    }
}
