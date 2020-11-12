package com.mrtecks.amrdukan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtecks.amrdukan.productsPOJO.Datum;
import com.mrtecks.amrdukan.productsPOJO.productsBean;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class product extends Fragment {

    RecyclerView grid;
    CategoryAdapter adapter;
    GridLayoutManager manager;
    String cid;
    String subid;
    ProgressBar progress;
    List<Datum> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product, container, false);

        list = new ArrayList<>();

        cid = getArguments().getString("cid");
        subid = getArguments().getString("subid");

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar7);

        adapter = new CategoryAdapter(getActivity(), list);
        manager = new GridLayoutManager(getActivity(), 2);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

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

        Call<productsBean> call = cr.getFoodProducts(subid);
        call.enqueue(new Callback<productsBean>() {
            @Override
            public void onResponse(Call<productsBean> call, Response<productsBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<productsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        Context context;
        List<Datum> list = new ArrayList<>();
        LayoutInflater inflater;


        public CategoryAdapter(Context context, List<Datum> list) {
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
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.product_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.title.setText(item.getName());
            holder.size.setText(item.getSize());
            holder.type.setText(item.getType());

            if (item.getType().equals("VEG")) {
                holder.type.setTextColor(Color.parseColor("#45A822"));
            } else {
                holder.type.setTextColor(Color.RED);
            }

            if (item.getStock().equals("In stock")) {
                holder.add.setVisibility(View.VISIBLE);
            } else {
                holder.add.setVisibility(View.GONE);
            }

            float mrp = Float.parseFloat(item.getPrice());
            float sel = Float.parseFloat(item.getDiscount());
            float dis = mrp - sel;

            if (dis > 0) {

                holder.selling.setText(Html.fromHtml("\u20B9 " + item.getDiscount()));
                holder.mrp.setText(Html.fromHtml("<strike>\u20B9 " + item.getPrice() + "</strike>"));
                holder.mrp.setVisibility(View.VISIBLE);
            } else {

                holder.selling.setText("\u20B9 " + item.getDiscount());
                holder.mrp.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context, SingleProduct.class);
                    intent.putExtra("cid", cid);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("pid", item.getId());
                    context.startActivity(intent);


                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.add_cart_dialog2);
                    dialog.show();

                    LinearLayout addon = dialog.findViewById(R.id.checkBox);
                    final List<String> addids = new ArrayList<>();
                    final EditText request = dialog.findViewById(R.id.editTextTextPersonName);
                    TextView addontitle = dialog.findViewById(R.id.textView13);
                    final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                    Button add = dialog.findViewById(R.id.button8);
                    final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);

                    stepperTouch.setMinValue(1);
                    stepperTouch.setMaxValue(99);
                    stepperTouch.setSideTapEnabled(true);
                    stepperTouch.setCount(1);

                    if (item.getAddons().size() > 0) {
                        addon.removeAllViews();

                        for (int i = 0; i < item.getAddons().size(); i++) {
                            View addonmodel = inflater.inflate(R.layout.addon_model, null);
                            CheckBox check = addonmodel.findViewById(R.id.check);
                            check.setText(item.getAddons().get(i).getTitle() + " (+ ₹" + item.getAddons().get(i).getPrice() + ")");

                            final int finalI = i;
                            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        addids.add(item.getAddons().get(finalI).getId());
                                    } else {
                                        addids.remove(item.getAddons().get(finalI).getId());
                                    }

                                }
                            });

                            addon.addView(addonmodel);
                        }

                        addon.setVisibility(View.VISIBLE);
                        addontitle.setVisibility(View.VISIBLE);
                    } else {
                        addon.setVisibility(View.GONE);
                        addontitle.setVisibility(View.GONE);
                    }

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String aons = TextUtils.join(",", addids);
                            final String req = request.getText().toString();

                            progressBar.setVisibility(View.VISIBLE);

                            Bean b = (Bean) context.getApplicationContext();


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

                            Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                            Log.d("pid", item.getId());
                            Log.d("quantity", String.valueOf(stepperTouch.getCount()));


                            Call<singleProductBean> call = cr.addFoodCart(
                                    SharePreferenceUtils.getInstance().getString("userId"),
                                    item.getId(),
                                    String.valueOf(stepperTouch.getCount()),
                                    item.getDiscount(),
                                    cid,
                                    aons,
                                    req
                            );

                            call.enqueue(new Callback<singleProductBean>() {
                                @Override
                                public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                    if (response.body().getStatus().equals("1")) {
                                        dialog.dismiss();
                                        ((Products) getActivity()).onResume();
                                    }

                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<singleProductBean> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });


                        }
                    });

                }
            });


        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title, size, type, selling, mrp;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView25);
                title = itemView.findViewById(R.id.textView37);
                size = itemView.findViewById(R.id.textView38);
                type = itemView.findViewById(R.id.textView50);
                selling = itemView.findViewById(R.id.textView44);
                mrp = itemView.findViewById(R.id.textView45);
                add = itemView.findViewById(R.id.imageButton);
            }
        }
    }

}
