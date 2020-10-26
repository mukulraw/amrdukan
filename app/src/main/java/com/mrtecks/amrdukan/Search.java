package com.mrtecks.amrdukan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtecks.amrdukan.searchPOJO.Datum;
import com.mrtecks.amrdukan.searchPOJO.searchBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Search extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    EditText query;
    List<Datum> list;
    SearchAdapter adapter;
    GridLayoutManager manager;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar5);
        query = view.findViewById(R.id.editText4);


        adapter = new SearchAdapter(mainActivity, list);
        manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {


                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) mainActivity.getApplicationContext();

                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.level(HttpLoggingInterceptor.Level.HEADERS);
                    logging.level(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .client(client)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<searchBean> call = cr.search(s.toString());
                    call.enqueue(new Callback<searchBean>() {
                        @Override
                        public void onResponse(Call<searchBean> call, Response<searchBean> response) {


                            if (response.body().getStatus().equals("1")) {
                                adapter.setData(response.body().getData());
                            }

                            progress.setVisibility(View.GONE);


                        }

                        @Override
                        public void onFailure(Call<searchBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {

                    adapter.setData(new ArrayList<Datum>());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public SearchAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);


            holder.title.setText(item.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleProduct2.class);
                    intent.putExtra("id", item.getPid());
                    intent.putExtra("title", item.getName());
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                title = itemView.findViewById(R.id.textView37);


            }
        }
    }

}
