package com.mrtecks.amrdukan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Orders extends Fragment {

    ProgressBar progress;
    RecyclerView grid;
    OrdersAdapter adapter;
    GridLayoutManager manager;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_orders , container , false);
        mainActivity = (MainActivity)getActivity();

        progress = view.findViewById(R.id.progressBar3);
        grid = view.findViewById(R.id.grid);



        adapter = new OrdersAdapter(mainActivity);

        manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        return view;
    }




    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        Context context;
        LayoutInflater inflater;

        OrdersAdapter(Context context) {
            this.context = context;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.order_list_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {



           /* viewHolder.txn.setText("#" + item.getTxn());
            viewHolder.date.setText(item.getCreated());
            viewHolder.status.setText(item.getStatus());
            viewHolder.name.setText(item.getName());
            viewHolder.address.setText(item.getAddress());
            viewHolder.pay.setText(item.getPay_mode());
            viewHolder.slot.setText(item.getSlot());
            viewHolder.amount.setText("\u20B9 " + item.getAmount());

            viewHolder.deldate.setText(item.getDelivery_date());*/

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* Intent intent = new Intent(context , OrderDetails.class);
                    intent.putExtra("oid" , item.getId());
                    startActivity(intent);*/

                }
            });


        }

        @Override
        public int getItemCount() {
            return 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txn , date , status , name , address , amount , pay , slot , deldate;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                /*txn = itemView.findViewById(R.id.textView27);
                date = itemView.findViewById(R.id.textView28);
                status = itemView.findViewById(R.id.textView35);
                name = itemView.findViewById(R.id.textView32);
                address = itemView.findViewById(R.id.textView34);
                amount = itemView.findViewById(R.id.textView30);
                pay = itemView.findViewById(R.id.textView40);
                slot = itemView.findViewById(R.id.textView62);
                deldate = itemView.findViewById(R.id.textView42);*/


            }
        }
    }

}
