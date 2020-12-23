package Adapter;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.List;

        import ModelClass.MarketPlaceModelClass;

import com.example.myfirstapp.MarketPlace;
import com.example.myfirstapp.R;


public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.MyViewHolder> {

    Context context;


    private List<MarketPlaceModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        //TextView title,icon_type,percentage,price,value;
        //ImageView icon,arrow;

        TextView varCoupanName,varSellerTitle,varPrice;
        ImageView imgBuy;


        public MyViewHolder(View view) {
            super(view);

            varCoupanName = (TextView) view.findViewById(R.id.couponName);
            varSellerTitle = (TextView) view.findViewById(R.id.seller);
            varPrice = (TextView) view.findViewById(R.id.price);
            imgBuy = (ImageView) view.findViewById(R.id.buynow);



        }

    }


    public MarketPlaceAdapter(Context context, List<MarketPlaceModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MarketPlaceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market_place, parent, false);


        return new MarketPlaceAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MarketPlaceModelClass lists = OfferList.get(position);
        holder.varCoupanName.setText(lists.getVarCoupanName());
        holder.varSellerTitle.setText(lists.getVarSellerTitle());
        holder.varPrice.setText(lists.getVarPrice());
        holder.imgBuy.setImageResource(lists.getImgBuy());
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}



