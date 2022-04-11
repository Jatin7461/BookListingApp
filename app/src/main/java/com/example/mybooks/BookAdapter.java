package com.example.mybooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book = getItem(position);
        View listview = convertView;
        if (listview == null) {
            listview = LayoutInflater.from(getContext()).inflate(R.layout.bookview, parent, false);
        }

        TextView title = (TextView) listview.findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView author = (TextView) listview.findViewById(R.id.author);
        author.setText(book.getAuthor());

        ImageView image = (ImageView) listview.findViewById(R.id.bookimage);
        image.setImageDrawable(book.getImage());

        return listview;
    }
}
