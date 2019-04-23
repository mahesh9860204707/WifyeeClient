package com.wifyee.greenfields.SharedPrefence;

/**
 * Created by user on 12/10/2017.
 */

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;
        import com.google.gson.Gson;
        import com.wifyee.greenfields.foodorder.MerchantActivity;
        import com.wifyee.greenfields.foodorder.SharedPrefenceList;


        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
public class SharedPreference {

    public Context context;
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "SharedPrefenceList_Favorite";

    public SharedPreference() {
        super();
    }
    public SharedPreference(Context ctx) {
        this.context=ctx;
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<SharedPrefenceList> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void clearCart(Context context){
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();

        Intent i = new Intent(context, MerchantActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void clearCache(Context context){
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();
    }



    public void addFavorite(Context context, ArrayList<SharedPrefenceList> product) {
        List<SharedPrefenceList> favorites = getFavorites(context);
//        if (product == null)
       saveFavorites(context, product);
    }
    public void addFavoriteItem(Context context, SharedPrefenceList product) {
        List<SharedPrefenceList> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<SharedPrefenceList>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

   public void removeFavorite(Context context, List<SharedPrefenceList> product) {
       List<SharedPrefenceList> favorites = product;

       if (favorites != null) {
         favorites.clear();
           saveFavorites(context, favorites);
       }
   }

    public void updateFavoriteItem(Context context, SharedPrefenceList product) {
        List<SharedPrefenceList> favorites = getFavorites(context);
        if (favorites != null)
            for(int k=0;k<favorites.size();k++)
            {
                if(favorites.get(k).name.equalsIgnoreCase(product.name))
                {

                 /*   favorites.get(k).price=product.price;
                    favorites.get(k).quantiy=product.quantiy;
                   *//* favorites.remove(k);
                    favorites.add(product);*/
                    favorites.remove(k);
                    favorites.add(k,product);
                    break;
                }
            }

        saveFavorites(context, favorites);
    }
    public void removeFavoriteItem(Context context, SharedPrefenceList product) {
        ArrayList<SharedPrefenceList> favorites = getFavorites(context);
        if (favorites != null) {
           // favorites.remove(product);
            for(int k=0;k<favorites.size();k++)
            {
                if(favorites.get(k).name.equalsIgnoreCase(product.name))
                {
                    favorites.remove(k);
                    break;
                }
            }
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<SharedPrefenceList> getFavorites(Context context) {
        SharedPreferences settings;
        List<SharedPrefenceList> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            SharedPrefenceList[] favoriteItems = gson.fromJson(jsonFavorites,
                    SharedPrefenceList[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<SharedPrefenceList>(favorites);
        } else
            return null;

        return (ArrayList<SharedPrefenceList>) favorites;
    }
}
