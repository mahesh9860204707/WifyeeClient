package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class NearOfferList extends JSONObject implements Serializable {
            public String itemId;
            public String itemName;
            public String itemPrice;
            public String discountType;
            public String discount;
            public String mobicash_commission;
            public String commission_type;
            public String quentity;
            public String status;
            public String createdDate;
            public String updateddDate;
            public List<OfferImageObj_ResponseList> mOfferImageObj_responseLists;

}
