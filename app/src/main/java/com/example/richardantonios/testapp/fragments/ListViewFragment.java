package com.example.richardantonios.testapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.richardantonios.testapp.InfoActivity;
import com.example.richardantonios.testapp.MainActivity;
import com.example.richardantonios.testapp.R;
import com.example.richardantonios.testapp.adapters.ListViewAdapter;
import com.example.richardantonios.testapp.models.ItemModel;
import com.example.richardantonios.testapp.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewFragment extends BaseFragment {

    private ListView listView;
    private static ArrayList<ItemModel> items;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);

        listView = rootView.findViewById(R.id.listview);
        progressBar = rootView.findViewById(R.id.progressBar);

        if(getActivity() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("ListView Fragment");
        }

        progressBar.setVisibility(View.VISIBLE);
        new AsyncFetchData().execute();

        return rootView;
    }

    private class AsyncFetchData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            items = new ArrayList<>();
            return new HttpUtils().executePost("todos");
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    ItemModel model = new ItemModel();
                    model.setUserId(object.getInt("userId"));
                    model.setId(object.getInt("id"));
                    model.setTitle(object.getString("title"));
                    model.setCompleted(object.getBoolean("completed"));
                    items.add(model);
                    handleItems(items);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void handleItems(ArrayList<ItemModel> list)
    {
        ListViewAdapter adapter = new ListViewAdapter(list, getActivity().getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel model = (ItemModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity().getBaseContext(), InfoActivity.class);
                intent.putExtra("title", model.getTitle());
                intent.putExtra("id", model.getId());
                intent.putExtra("userid", model.getUserId());
                intent.putExtra("completed", model.isCompleted());
                startActivity(intent);
            }
        });
    }

}
