package ModelClass;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wolfsoft5 on 18/9/18.
 */

public class MarketPlaceModelClass {

    String varCoupanName,varSellerTitle,varPrice;
    Integer imgBuy;


    public Integer getImgBuy() {
        return imgBuy;
    }

    public void setImgBuy(Integer imgBuy) {
        this.imgBuy = imgBuy;
    }

    public String getVarCoupanName() {
        return varCoupanName;
    }

    public void setVarCoupanName(String varCoupanName) {
        this.varCoupanName = varCoupanName;
    }

    public String getVarSellerTitle() {
        return varSellerTitle;
    }

    public void setVarSellerTitle(String varSellerTitle) {
        this.varSellerTitle = varSellerTitle;
    }

    public String getVarPrice() {
        return varPrice;
    }

    public void setVarPrice(String varPrice) {
        this.varPrice = varPrice;
    }

    public MarketPlaceModelClass(String CoupanName, String SellerTitle, String price, Integer Buy) {
        this.varCoupanName = CoupanName;
        this.varSellerTitle = SellerTitle;
        this.varPrice = price;
        this.imgBuy = Buy;
    }



}
