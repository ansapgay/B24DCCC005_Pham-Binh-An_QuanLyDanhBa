package com.example.contactmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private final ArrayList<Contact> contacts;
    private final LayoutInflater inflater;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_contact, parent, false);
            holder = new ViewHolder(
                    convertView.findViewById(R.id.tvAvatar),
                    convertView.findViewById(R.id.tvContactName),
                    convertView.findViewById(R.id.tvContactPhone),
                    convertView.findViewById(R.id.tvContactGroup),
                    convertView.findViewById(R.id.tvFavoriteStar));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = getItem(position);
        holder.avatar.setText(contact.getInitial());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        holder.group.setText(contact.getGroup());
        holder.favoriteStar.setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);
        return convertView;
    }

    private static class ViewHolder {
        final TextView avatar;
        final TextView name;
        final TextView phone;
        final TextView group;
        final TextView favoriteStar;

        ViewHolder(TextView avatar, TextView name, TextView phone, TextView group,
                   TextView favoriteStar) {
            this.avatar = avatar;
            this.name = name;
            this.phone = phone;
            this.group = group;
            this.favoriteStar = favoriteStar;
        }
    }
}
