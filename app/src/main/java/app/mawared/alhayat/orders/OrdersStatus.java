package app.mawared.alhayat.orders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public  class OrdersStatus {

    public static final Map<Integer, String> STATUS =
            Collections.unmodifiableMap(new HashMap<Integer, String>() {{
                put(0, "جاري التجهيز");
                put(1, "قيد التنفيذ");
                put(2, "تمت الجدولة");
                put(3, "جاري التوصيل");
                put(4, "تم التسليم");
                put(5, "تم الإلغاء");
                put(6, "خطأ في الدفع");
                put(7, "إعادة الجدولة");
                put(8, "تم التوصيل");
                put(9, "تم الحذف");
                put(10, "معلق");
            }});

    public static final int[] greens={0,1,3,4,8};
    public static final int[] blues={2,7,10};


}
