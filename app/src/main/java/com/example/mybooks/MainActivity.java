package com.example.mybooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER = 1;
    private static final String LOG_TAG = MainActivity.class.getName();

    private static EditText edittext;
    private ProgressBar loading;
    private ListView listview;
    private TextView empty;
    private BookAdapter adapter;
    private static final String link = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);


        edittext = (EditText) findViewById(R.id.search_book);
        edittext.setHint("Enter Book Name");


        Button button = (Button) findViewById(R.id.button);
        button.setText("Search");


        adapter = new BookAdapter(this, new ArrayList<Book>());
        listview = (ListView) findViewById(R.id.book_list);


        empty = (TextView) findViewById(R.id.empty_text);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG,"button clicked");
                listview.setAdapter(adapter);






                if (!edittext.getText().toString().matches("")) {
                    loading.setVisibility(View.VISIBLE);
                    Log.v(LOG_TAG,"before loader");
                    LoaderManager loadermanager = getLoaderManager();
                    Log.v(LOG_TAG,"initialize loader");

                    adapter.clear();
                    loadermanager.restartLoader(BOOK_LOADER, null, MainActivity.this);
                    Log.v(LOG_TAG,"after loader");
//                    background back = new background();
//                    back.execute(link + edittext.getText().toString().replaceAll(" ", "%20"));
                } else {
                    Toast.makeText(MainActivity.this, "Enter Book Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = adapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getUrl()));
                startActivity(intent);
            }
        });




    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"create loader");
        return new BookLoader(this, link + edittext.getText().toString().replaceAll(" ","%20"));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.v(LOG_TAG,"finish load");

        adapter.clear();


        empty.setText("No books");
        loading.setVisibility(View.GONE);
        if (isConnected() == false) {
            empty.setText("No Internet Connection");

        }

        if (books != null && !books.isEmpty()) {
        empty.setText(null);
            adapter.addAll(books);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
    //    private class background extends AsyncTask<String, Void, List<Book>> {
//
//        @Override
//        protected List<Book> doInBackground(String... strings) {
//
//
//            empty.setText(null);
//            ArrayList<Book> booklist = QueryUtils.extractBooks(strings[0]);
//            return booklist;
//        }
//
//        @Override
//        protected void onPostExecute(List<Book> books) {
//            adapter.clear();
//            empty.setText("No books");
//            loading.setVisibility(View.GONE);
//            if (isConnected() == false) {
//                empty.setText("No Internet Connection");
//
//            }
//            if (books != null && !books.isEmpty()) {
//
//                adapter.addAll(books);
//                empty.setText(null);
//            }
//        }
//    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}