package com.mrtecks.amrdukan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Cart extends Fragment {

    CartAdapter adapter;

    GridLayoutManager manager;

    RecyclerView grid;
    MainActivity mainActivity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart, container, false);
        mainActivity = (MainActivity) getActivity();
        grid = view.findViewById(R.id.grid);

        adapter = new CartAdapter(mainActivity);

        manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);




        return view;
    }



    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        Context context;
        LayoutInflater inflater;

        CartAdapter(Context context) {
            this.context = context;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.prod_list_model4, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {


        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ImageButton delete;

            Button add, remove;
            TextView quantity, title, brand, price;


            ViewHolder(@NonNull View itemView) {
                super(itemView);


                //buy.setSideTapEnabled(true);

                //name = itemView.findViewById(R.id.name);


            }
        }
    }

}
