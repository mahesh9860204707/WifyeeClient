package com.wifyee.greenfields.database;


public class DatabaseDB {

    public String Address="CREATE TABLE IF NOT EXISTS address(" +
            "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "   name TEXT" +
            ",  mobile_no TEXT " +
            ",  alternate_no TEXT " +
            ",  city TEXT " +
            ",  locality TEXT " +
            ",  flat_no TEXT " +
            ",  pincode TEXT " +
            ",  state TEXT " +
            ",  active_status INTEGER);";

    public String CartItem="CREATE TABLE IF NOT EXISTS cart_item(" +
            "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "   image_path TEXT" +
            ",  item_name TEXT " +
            ",  item_type TEXT " +
            ",  merchant_id TEXT " +
            ",  item_id TEXT " +
            ",  quantity TEXT " +
            ",  unit TEXT " +
            ",  price TEXT " +
            ",  discount TEXT " +
            ",  active_status INTEGER);";

    public String FoodCart="CREATE TABLE IF NOT EXISTS food_cart(" +
            "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "   image_path TEXT" +
            ",  item_name TEXT " +
            ",  item_id TEXT " +
            ",  item_description TEXT " +
            ",  quantity TEXT " +
            ",  price TEXT " +
            ",  discount TEXT " +
            ",  qty_half_full TEXT " +
            ",  category TEXT " +
            ",  active_status INTEGER);";

    public String drop_table="DROP TABLE food_cart";

    public String createTables(SQLController dbController)
    {
        String return_value="Pending";

        //return_value = dbController.fireQuery(drop_table);

        return_value = dbController.fireQuery(Address);
        return_value = dbController.fireQuery(FoodCart);
        return_value = dbController.fireQuery(CartItem);

        return return_value;
    }
}
