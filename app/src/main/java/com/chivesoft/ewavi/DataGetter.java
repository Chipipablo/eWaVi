package com.chivesoft.ewavi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class DataGetter {

    final static int NUM_CASH_CONVERTERS = 0;
    final static int NUM_CEX = 1;
    final static int NUM_EBAY = 2;
    final static int NUM_FNAC = 3;
    final static int NUM_MILANUNCIOS = 4;
    final static int NUM_NOLOTIRE = 5;
    final static int NUM_TODOCOLECCION = 6;
    final static int NUM_VIBBO = 7;
    final static int NUM_WALLAPOP = 8;

    final static int NUM_AMAZON = 9;

    private final String URL_WALLAPOP_ORDER_PRICE = "https://es.wallapop.com/rest/items?_p=0&dist=400&publishDate=any&order=salePrice-asc&kws=";
    private final String URL_WALLAPOP_ORDER_BESTMATCH = "https://es.wallapop.com/rest/items?_p=0&dist=400&publishDate=any&order=creationDate-des&kws=";
    private final String URL_WALLAPOP_PRICE_MIN = "&minPrice=";
    private final String URL_WALLAPOP_PRICE_MAX = "&maxPrice=";
    private final String URL_WALLAPOP_LOCATION_LAT = "&latitude=";
    private final String URL_WALLAPOP_LOCATION_LONG = "&longitude=";
    private final String URL_WALLAPOP_DISTANCE_REPLACE = "&dist=400";
    private final String URL_WALLAPOP_DISTANCE_REPLACE_FOR = "&dist=";

    //private final String URL_VIBBO_PRE_OLD = "https://www.vibbo.com/anuncios-toda-espana/";
    private final String URL_VIBBO_ORDER_PRICE = "https://api.vibbo.com/anuncios-toda-espana/?sort_by=2&od=2&q="; //http://api.vibbo.com/anuncios-barcelona/?sort_by=2&od=2&q=
    private final String URL_VIBBO_ORDER_BESTMATCH = "https://api.vibbo.com/anuncios-toda-espana/?sort_by=1&od=2&q=";
    //private final String URL_VIBBO_POST_OLD = ".htm?ca=0_s&od=2&sort_by=2&fPos=48&fOn=sb_searchtext";
    private final String URL_VIBBO_PRICE_MIN = "&ps=";
    private final String URL_VIBBO_PRICE_MAX = "&pe=";
    private final String URL_VIBBO_LOCATION_REPLACE = "/anuncios-toda-españa";
    private final String URL_VIBBO_LOCATION_REPLACE_FOR = "/anuncios-";

    private final String URL_EBAY_PRE_ORDER_PRICE = "https://www.ebay.es/sch/i.html?_from=R40&_sacat=0&_sop=15&_dcat=139971&rt=nc&_mPrRngCbx=1&LH_BIN=1&_nkw=";
    private final String URL_EBAY_PRE_ORDER_BESTMATCH = "https://www.ebay.es/sch/i.html?_from=R40&_sacat=0&_sop=12&_dcat=139971&rt=nc&_mPrRngCbx=1&LH_BIN=1&_nkw=";
    private final String URL_EBAY_PRICE_MIN = "&_udlo=";
    private final String URL_EBAY_PRICE_MAX = "&_udhi=";

    private final String URL_MILANUNCIOS_PRE = "https://www.milanuncios.com/anuncios/"; //https://www.milanuncios.com/anuncios-en-baleares/
    private final String URL_MILANUNCIOS_POST_ORDER_PRICE = ".htm?demanda=n&orden=baratos&fromSearch=1";
    private final String URL_MILANUNCIOS_POST_ORDER_BESTMATCH = ".htm?demanda=n&orden=baratos&fromSearch=1";
    private final String URL_MILANUNCIOS_PRICE_MIN = "&desde=";
    private final String URL_MILANUNCIOS_PRICE_MAX = "&hasta=";
    private final String URL_MILANUNCIOS_LOCATION_REPLACE = "/anuncios";
    private final String URL_MILANUNCIOS_LOCATION_REPLACE_FOR = "/anuncios-en-";
//    private final String URL_MILANUNCIOS_LOCATION_NO = "anuncios/";
//    private final String URL_MILANUNCIOS_LOCATION_YES = "anuncios-en-";
//    private final String URL_MILANUNCIOS_LOCATION_YES_POST = "/";

    private final String URL_CEX_ORDER_PRICE = "https://wss2.cex.es.webuy.io/v3/boxes?firstRecord=1&count=15&sortBy=sellprice&sortOrder=asc&inStock=1&q=";
    private final String URL_CEX_ORDER_BESTMATCH = "https://wss2.cex.es.webuy.io/v3/boxes?firstRecord=1&count=15&sortBy=relevance&sortOrder=desc&inStock=1&q=";
    private final String URL_CEX_PRICE_MIN = "&minPrice=";
    private final String URL_CEX_PRICE_MAX = "&maxPrice=";

    private final String URL_CASH_CONVERTERS_ORDER_PRICE = "https://www.cashconverters.es/es/es/search?sz=12&start=0&srule=price-low-to-high&q=";
    private final String URL_CASH_CONVERTERS_ORDER_BESTMATCH = "https://www.cashconverters.es/es/es/search?sz=12&start=0&srule=most-viewed&q=";
    private final String URL_CASH_CONVERTERS_PRICE_MIN = "&pmin=";
    private final String URL_CASH_CONVERTERS_PRICE_MAX = "&pmax=";

    private final String URL_NOLOTIRE_ORDER_PRICE = "https://www.nolotire.com/buscador?tiendasSeleccionadas=&orden=3&buscar=";
    private final String URL_NOLOTIRE_ORDER_BESTMATCH = "https://www.nolotire.com/buscador?tiendasSeleccionadas=&orden=0&buscar=";
    private final String URL_NOLOTIRE_PRICE_MIN = "&min=";
    private final String URL_NOLOTIRE_PRICE_MAX = "&max=";

    private final String URL_TODOCOLECCION_ORDER_PRICE = "https://www.todocoleccion.net/buscador?O=menos&bu=";
    private final String URL_TODOCOLECCION_ORDER_BESTMATCH = "https://www.todocoleccion.net/buscador?O=rl&bu=";
    private final String URL_TODOCOLECCION_PRICE_MIN = "&preciodesde=";
    private final String URL_TODOCOLECCION_PRICE_MAX = "&preciohasta=";
    private final String URL_TODOCOLECCION_LOCATION = "&pa=Espana&pr=";//&pa=España&pr=Barcelona

    private final String URL_FNAC_ORDER_PRICE = "https://www.fnac.es/SearchResult/ResultList.aspx?sft=1&sl&ssi=5&sso=1&Search=";
    private final String URL_FNAC_ORDER_BESTMATCH = "https://www.fnac.es/SearchResult/ResultList.aspx?SCat=0%211&sft=1&sa=0&Search=";
    private final String URL_FNAC_PRICE_MIN = "&SFilt=";
    private final String URL_FNAC_PRICE_MAX = "00_";
    private final String URL_FNAC_POST_PRICE_MAX = "00!9";

    private final String URL_AMAZON = "https://www.amazon.es/gp/search/sort=price-asc-rank&keywords=";
    private final String URL_AMAZON_PRICE_MIN = "&low-price=";
    private final String URL_AMAZON_PRICE_MAX = "&high-price=";

    private CustomAdapter adapter;
    private Context mContext;
    private ArrayList<ListDataModel> listDataModels;
    private ListView listView;
    private Comparator comparatorByMinPrice;
    private Comparator comparatorByMaxPrice;
    final static int ORDER_LIST_MIN_PRICE = 0;
    final static int ORDER_LIST_MAX_PRICE = 1;
    static int orderListSelected = 0;

    private final int NUM_STORES;
    static boolean[] isStoreEnabled;
    private static AtomicIntegerArray isStoreFinished1;
    private int[] aux;
    static int maxSizeList = 70;
    static boolean checkListEffects = true;
    final static int ORDER_BESTMATCH = 0;
    final static int ORDER_PRICE = 1;

    static int orderSelected = 0;

    private final Handler handler;
    private final int UPDATE_LIST = 3;
    private final int ADD_TO_LIST = 2;
    //    private static int cont = 0;
    private String[] ADS_LIST;

    private int sizeImage;
    private int sizeMarketImage;

    static boolean checkSearchOptionLocation = true;
    static boolean checkSearchOptionLocationOnly = false;
    //    static String locationCity = ""; // "-" separated
    static int locationCityNumber = -1;
    static Double locationLat = 0.0;
    static Double locationLong = 0.0;
    private static int locationDistance = 30;
    static boolean locationManuallyChanged = false;

    private String[] arrayMilanuncios;
    private String[] arrayTodocoleccion;


    public DataGetter(final Context mContext, final ListView listView) {
        arrayMilanuncios = mContext.getResources().getStringArray(R.array.provinces_milanuncios);
        arrayTodocoleccion = mContext.getResources().getStringArray(R.array.provinces_todocoleccion);
        this.mContext = mContext;
        this.listView = listView;
        NUM_STORES = mContext.getResources().getStringArray(R.array.store_items).length;
        ADS_LIST = mContext.getResources().getStringArray(R.array.ads_list);
        //Log.e("num",NUM_STORES+"");
        isStoreEnabled = new boolean[NUM_STORES];
        Arrays.fill(isStoreEnabled, true);
        aux = new int[NUM_STORES];
        Arrays.fill(aux, -1);
        isStoreFinished1 = new AtomicIntegerArray(aux.clone());
        listDataModels = new ArrayList<>();
        createComparatorByMinPrice();
        createComparatorByMaxPrice();
        handler = createHandler();

//        Log.e("DPI", "DPI: "+Main2Activity.DPI);
        sizeImage = mContext.getResources().getDimensionPixelSize(R.dimen.item_image_size);
        sizeMarketImage = mContext.getResources().getDimensionPixelSize(R.dimen.market_image_size);
//        Log.e("sizeMarketImage", "sizeMarketImage: "+sizeMarketImage);
    }

    private Handler createHandler() {
        Handler handler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int uid = 0;
                if (Build.VERSION.SDK_INT > 20) uid = msg.sendingUid;
                else uid = msg.arg2;
                if (msg.what == ADD_TO_LIST) {
                    for (int i = 0; i < isStoreFinished1.length(); i++) {
                        if (uid == i) {
                            if (msg.obj != null) addToList((ListDataModel) msg.obj);
                            isStoreFinished1.decrementAndGet(i);
//                            Log.e("add","add: "+isStoreFinished1.get(i));
//                            cont++;
                            break;
                        }
                    }
                } else if (msg.what == UPDATE_LIST) {
                    for (int i = 0; i < isStoreFinished1.length(); i++) {
                        if (uid == i) {
//                            Log.e("update","finished: "+i);
                            isStoreFinished1.set(i, 0);
                            break;
                        }
                    }
                }
                if (isStoreFinished1.get(uid) == 0 && (msg.what == ADD_TO_LIST || msg.what == UPDATE_LIST)) {
                    int notFinished = isStoreEnabled.length;
                    for (int i = 0; i < isStoreEnabled.length; i++) {
                        //Log.e("id","id: "+uid);
                        if (!isStoreEnabled[i] || (isStoreEnabled[i] && (isStoreFinished1.get(i) == 0))) {
                            notFinished--;
                        }
                    }
                    if (notFinished == 0) {
                        //Log.e("sizelist", "size: "+maxSizeList);
                        int size = listDataModels.size();
                        if (size == 0) {
                            Toast.makeText(mContext, "No se han encontrado resultados", Toast.LENGTH_LONG).show();
                            Main2Activity.stopProgressSpinner();
                        } else {
                            orderList();
                            if (size > maxSizeList)
                                listDataModels.subList(maxSizeList, size).clear();
                            size = listDataModels.size() / 10;
                            //Log.e("sizelist", "size: "+size);
                            for (int i = 0; i < size && i < ADS_LIST.length; i++) {
                                AdView adView = new AdView(mContext);
//                                adView.setAdSize(AdSize.SMART_BANNER);
                                adView.setAdSize(AdSize.FLUID);
                                if (BuildConfig.DEBUG) {
                                    adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
                                } else {
                                    adView.setAdUnitId(ADS_LIST[i]);
                                }
                                AdRequest adRequest = new AdRequest.Builder().build();
                                adView.loadAd(adRequest);
                                listDataModels.add(i * 10 + 8, new ListDataModel(adView));
                            }
                            listDataModels.trimToSize();
                            createList();
                        }
                        //Log.e("Finished_list","Finished list");
                        //Log.e("Finished_list",""+msg.what + "   "+uid);
                        //Log.e("sizes","size: "+cont+" list: "+listDataModels.size() + "   array: "+isStoreFinished1);
                    }
                }
                super.handleMessage(msg);
            }
        };
        return handler;
    }

    private void createComparatorByMinPrice() {
        comparatorByMinPrice = new Comparator<ListDataModel>() {
            @Override
            public int compare(ListDataModel l1, ListDataModel l2) {
                try {
                    if (l1 == null || l2 == null || l1.getPrice() == null || l2.getPrice() == null) {
                        Log.e("Comparator", "Null value");
                        return 0;
                    }
                    char[] c1 = l1.getPrice().toCharArray();
                    boolean b1 = false;
                    boolean b2 = false;
                    boolean decimalFound = false;
//                    String s1 = "";
                    StringBuilder s1 = new StringBuilder("");
                    for (char character : c1) {
                        if (Character.isDigit(character)) {
                            s1.append(character);
                            b1 = true;
                        } else if (character == '.') { //In ebay '.' is for "1.000,00"
                            //then do nothing (skip character)
                        } else if (character == ',') { //In ebay ',' is for decimals
                            if (decimalFound)
                                s1 = new StringBuilder(s1.toString().replaceAll("\\.", "")); //avoid multiple points of decimals
                            decimalFound = true;
                            //Add a point to transform it to double
                            s1.append('.');
                        } else if (b1) break;
                    }
                    char[] c2 = l2.getPrice().toCharArray();
//                    String s2 = "";
                    StringBuilder s2 = new StringBuilder("");
                    decimalFound = false;
                    for (char character : c2) {
                        if (Character.isDigit(character)) {
                            s2.append(character);
                            b2 = true;
                        } else if (character == '.') { //In ebay '.' is for "1.000,00"
                            //then do nothing (skip character)
                        } else if (character == ',') { //In ebay ',' is for decimals
                            if (decimalFound)
                                s2 = new StringBuilder(s2.toString().replaceAll("\\.", "")); //avoid multiple points of decimals
                            decimalFound = true;
                            //Add a point to transform it to double
                            s2.append('.');
                        } else if (b2) break; //Possible 2 prices, so keep first
                    }
                    //Log.e("double",s1);
                    //Log.e("double",s2);
                    if (b1 && b2)
                        return Double.compare(Double.parseDouble(s1.toString()), Double.parseDouble(s2.toString()));
                    else return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    /*Log.e("comparator", "error l1: " + l1);
                    Log.e("comparator", "error l2: " + l2);
                    Log.e("comparator", "error l1.getPrice: " + l1.getPrice());
                    Log.e("comparator", "error l2.getPrice: " + l2.getPrice());*/
                    return 0;
                }
            }
        };
    }

    private void createComparatorByMaxPrice() {
        comparatorByMaxPrice = new Comparator<ListDataModel>() {
            @Override
            public int compare(ListDataModel l1, ListDataModel l2) {
                try {
                    if (l1 == null || l2 == null || l1.getPrice() == null || l2.getPrice() == null) {
                        Log.e("Comparator", "Null value");
                        return 0;
                    }
                    char[] c1 = l1.getPrice().toCharArray();
                    boolean b1 = false;
                    boolean b2 = false;
                    boolean decimalFound = false;
//                    String s1 = "";
                    StringBuilder s1 = new StringBuilder("");
                    for (char character : c1) {
                        if (Character.isDigit(character)) {
                            s1.append(character);
                            b1 = true;
                        } else if (character == '.') { //In ebay '.' is for "1.000,00"
                            //then do nothing (skip character)
                        } else if (character == ',') { //In ebay ',' is for decimals
                            if (decimalFound)
                                s1 = new StringBuilder(s1.toString().replaceAll("\\.", "")); //avoid multiple points of decimals
                            decimalFound = true;
                            //Add a point to transform it to double
                            s1.append('.');
                        } else if (b1) break;
                    }
                    char[] c2 = l2.getPrice().toCharArray();
//                    String s2 = "";
                    StringBuilder s2 = new StringBuilder("");
                    decimalFound = false;
                    for (char character : c2) {
                        if (Character.isDigit(character)) {
                            s2.append(character);
                            b2 = true;
                        } else if (character == '.') { //In ebay '.' is for "1.000,00"
                            //then do nothing (skip character)
                        } else if (character == ',') { //In ebay ',' is for decimals
                            if (decimalFound)
                                s2 = new StringBuilder(s2.toString().replaceAll("\\.", "")); //avoid multiple points of decimals
                            decimalFound = true;
                            //Add a point to transform it to double
                            s2.append('.');
                        } else if (b2) break; //Possible 2 prices, so keep first
                    }
                    //Log.e("double",s1);
                    //Log.e("double",s2);
                    if (b1 && b2)
                        return Double.compare(Double.parseDouble(s2.toString()), Double.parseDouble(s1.toString()));
                    else return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    /*Log.e("comparator", "error l1: " + l1);
                    Log.e("comparator", "error l2: " + l2);
                    Log.e("comparator", "error l1.getPrice: " + l1.getPrice());
                    Log.e("comparator", "error l2.getPrice: " + l2.getPrice());*/
                    return 0;
                }
            }
        };
    }

    private void createList() {
        adapter = new CustomAdapter(listDataModels, mContext.getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListDataModel dataModel = listDataModels.get(position);
                if (dataModel.getUrl() != null && (dataModel.getUrl().startsWith("http://") || dataModel.getUrl().startsWith("https://"))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(dataModel.getUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Log.e("error URL_click", "" + dataModel.getUrl());
                    //Toast.makeText(mContext, "URL: "+dataModel.getUrl(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Main2Activity.stopProgressSpinner();
    }

    void removeList() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            listDataModels.clear();
            isStoreFinished1 = new AtomicIntegerArray(aux.clone());
        }
    }

    private void addToList(ListDataModel listDataModel) {
        listDataModels.add(listDataModel);
    }

    private void orderList() {
        try {
            if (orderListSelected == ORDER_LIST_MIN_PRICE)
                Collections.sort(listDataModels, comparatorByMinPrice);
            else if (orderListSelected == ORDER_LIST_MAX_PRICE)
                Collections.sort(listDataModels, comparatorByMaxPrice);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("order", "error order");
        }

    }

    void getEbay(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_EBAY, null);
            return;
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);

        String urlEbay = null;
        if (orderSelected == ORDER_BESTMATCH) urlEbay = URL_EBAY_PRE_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlEbay = URL_EBAY_PRE_ORDER_PRICE;
        try {
            urlEbay += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlEbay += URL_EBAY_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlEbay += URL_EBAY_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlEbay += txtSearch.replaceAll(" ", "+");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlEbay += URL_EBAY_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlEbay += URL_EBAY_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
        Log.i("url", urlEbay);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEbay,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("sresult");

                        if (items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Ebay", "Captcha encontrado");
                            } else {
                                Log.i("Ebay", "No hay elementos");
                            }
                            sendToHandler(UPDATE_LIST, NUM_EBAY, null);
                            return; //exit if no items
                        }

                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_ebay), (int) (sizeMarketImage * 1.8f), sizeMarketImage);
                        isStoreFinished1.set(NUM_EBAY, items.size());

                        //Log.e("response",response+"");
                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = "";
//                                try {
                                aux = item.getElementsByClass("vip").text();
                              /*  } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    aux = "-";
                                }*/
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 20) aux = aux.substring(0, 20);
                                final String title = aux;
                                //Log.e("price",desc+"precio: "+item.getElementsByClass("lvprice").html());
                                aux = "";
//                                try {
                                aux = item.getElementsByClass("bold").first().text();
                                if (aux.length() < 1) aux = "0€";
                                /*} catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll("EUR", "€").replaceAll("a", "-\n")
                                        .replaceAll(" ", "").replaceAll(",00", "");
                                try {
                                    aux = item.getElementsByClass("vip").attr("href");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    aux = "-";
                                }
                                final String url = aux;
                                try {
                                    aux = item.getElementsByTag("img").attr("src");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    aux = "";
                                }
                                final String imgItem = aux;
                                //final int num_marketplace = NUM_EBAY;
                                //Log.e("item",title+"");
                                //Log.e("price",price+"");
                                //Log.e("url",url+"");
                                //Log.e("imgItem",imgItem+"");

                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_EBAY, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                sendToHandler(ADD_TO_LIST, NUM_EBAY, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("/s-l225.jpg", "/s-l800.jpg")));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_EBAY, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_EBAY, null);
                            }
                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_EBAY, null);
                Log.e("Error Ebay", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.ebay));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getVibbo(String txtSearch, String txtPriceMin, String txtPriceMax) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlVibbo = null;
        if (orderSelected == ORDER_BESTMATCH) urlVibbo = URL_VIBBO_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlVibbo = URL_VIBBO_ORDER_PRICE;
        if (checkSearchOptionLocation && urlVibbo != null && locationCityNumber > -1) {
            try {
                urlVibbo += URLEncoder.encode(urlVibbo.replace(URL_VIBBO_LOCATION_REPLACE, URL_VIBBO_LOCATION_REPLACE_FOR + arrayMilanuncios[locationCityNumber]), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                urlVibbo = urlVibbo.replace(URL_VIBBO_LOCATION_REPLACE, URL_VIBBO_LOCATION_REPLACE_FOR + arrayMilanuncios[locationCityNumber]);
            }
        }
        try {
            urlVibbo += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlVibbo += URL_VIBBO_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlVibbo += URL_VIBBO_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlVibbo += txtSearch.replaceAll(" ", "+");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlVibbo += URL_VIBBO_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlVibbo += URL_VIBBO_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
//        Log.e("url", urlVibbo);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlVibbo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("flipper");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Vibbo", "Captcha encontrado");
                            } else Log.i("Vibbo", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_VIBBO, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_vibbo), sizeMarketImage, sizeMarketImage);
                        isStoreFinished1.set(NUM_VIBBO, items.size());

                        //Log.e("response",response+"");
                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String desc = item.getElementsByClass("subjectTitle").text();
                                if (desc.length() > 100) desc = desc.substring(0, 100) + "...";
                                final String description = desc;
                                if (desc.length() > 15) desc = desc.substring(0, 15);
                                final String title = desc;
                                String aux = "";
//                                try {
                                aux = item.getElementsByClass("subjectPrice").text();
                                if (aux == null || aux.length() < 1) aux = "0€";
                                /*} catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");

                                final String url = "https:" + item.getElementsByTag("a").attr("href");
//                                try {
                                aux = item.getElementsByClass("lazy").attr("title");
                                if (!aux.contains("images.vibbo.com"))
                                    aux = item.getElementsByClass("lazy").attr("src");

                              /*  } catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = "https:" + aux;
                                //final int num_marketplace = NUM_VIBBO;
                                //Log.e("item",title+"");
                                //Log.e("price",price+"");
                                //Log.e("url",url+"");
                                //Log.e("imgItem",imgItem+"");

                                if (imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_VIBBO, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                sendToHandler(ADD_TO_LIST, NUM_VIBBO, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("/c_238x178/", "/635x476/")));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_VIBBO, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_VIBBO, null);
                                Log.e("Vibbo", "Exception");
                            }

                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_VIBBO, null);
                Log.e("Error Vibbo", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.vibbo));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getWallapop(String txtSearch, String txtPriceMin, String txtPriceMax) {
        String urlWallapop = null;
        if (orderSelected == ORDER_BESTMATCH) urlWallapop = URL_WALLAPOP_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlWallapop = URL_WALLAPOP_ORDER_PRICE;
        try {
            urlWallapop += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlWallapop += URL_WALLAPOP_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlWallapop += URL_WALLAPOP_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlWallapop += txtSearch.replaceAll(" ", "%20");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlWallapop += URL_WALLAPOP_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlWallapop += URL_WALLAPOP_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
        if (checkSearchOptionLocation && locationLat != 0.0 && locationLong != 0.0) {
            urlWallapop = urlWallapop.replace(URL_WALLAPOP_DISTANCE_REPLACE, URL_WALLAPOP_DISTANCE_REPLACE_FOR + locationDistance);
//                    + URL_WALLAPOP_LOCATION_LAT + locationLat + URL_WALLAPOP_LOCATION_LONG + locationLong;
        }
//        Log.e("url", urlWallapop);

        final RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlWallapop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jArray = jsonObject.getJSONArray("items");

                            if (jArray == null || jArray.length() == 0) {
                                Log.i("Wallapop", "No se han encontrado resultados");
                                sendToHandler(UPDATE_LIST, NUM_WALLAPOP, null);
                                return;
                            }
                            final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_wallapop), sizeMarketImage, sizeMarketImage);
                            isStoreFinished1.set(NUM_WALLAPOP, jArray.length());

                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    final String title = oneObject.getString("title");
                                    String desc = oneObject.getString("description");
                                    if (desc.length() > 100) {
                                        desc = desc.substring(0, 100) + "...";
                                    }
                                    final String description = desc;
                                    String aux = "";
//                                    try {
                                    aux = oneObject.getString("price");
                                    if (aux == null || aux.length() < 1) aux = "0€";
                                    /*} catch (Exception e) {
                                        aux = "0€";
                                    }*/
                                    final String price = aux.replaceAll(" ", "").replaceAll(",00", "");
//                                    try {
                                    aux = oneObject.getJSONObject("mainImage").getString("smallURL");
                                   /* } catch (Exception e) {
                                        aux = "";
                                    }*/
                                    final String imgItem = aux;
//                                    final String url1 = oneObject.getString("url");
                                    final String url = "https://es.wallapop.com/item/" + oneObject.getString("url");

                                    if (imgItem == null || imgItem.length() < 5) {
                                        sendToHandler(ADD_TO_LIST, NUM_WALLAPOP, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                        continue;
                                    }

                                    ImageRequest imageRequest = new ImageRequest(
                                            imgItem, // Image URL
                                            new Response.Listener<Bitmap>() { // Bitmap listener
                                                @Override
                                                public void onResponse(Bitmap response) {
                                                    sendToHandler(ADD_TO_LIST, NUM_WALLAPOP, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("=W320", "=W640")));
                                                }
                                            },
                                            sizeImage, // Image width
                                            sizeImage, // Image height
                                            ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                            Bitmap.Config.RGB_565, //Image decode configuration
                                            new Response.ErrorListener() { // Error listener
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    error.printStackTrace();
                                                    sendToHandler(ADD_TO_LIST, NUM_WALLAPOP, null);
                                                }
                                            }
                                    );

                                    // Add ImageRequest to the RequestQueue
                                    queue.add(imageRequest);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sendToHandler(ADD_TO_LIST, NUM_WALLAPOP, null);
                                    //Toast.makeText(mContext, "Error de JSON2 "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }//end for
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Error de JSON", Toast.LENGTH_SHORT).show();
                            Log.i("Wallapop", "JSON Error. No results");
                            sendToHandler(UPDATE_LIST, NUM_WALLAPOP, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_WALLAPOP, null);
                Log.e("Error Wallapop", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.wallapop));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if (locationLat != 0.0 && locationLong != 0.0) {
                    params.put("Cookie", "searchLat=" + locationLat + "; searchLng=" + locationLong);
                }
//                params.put("Cookie", "searchLat=39.46868; searchLng=-0.37691");
//                conn.setRequestProperty("Cookie", "searchLat=39.46868; searchLng=-0.37691");

                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getMilanuncios(String txtSearch, String txtPriceMin, String txtPriceMax) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);

        // Request a string response from the provided URL.

        String urlMilanuncios = null;
        if (orderSelected == ORDER_BESTMATCH) urlMilanuncios = URL_MILANUNCIOS_POST_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlMilanuncios = URL_MILANUNCIOS_POST_ORDER_PRICE;
        if (checkSearchOptionLocation && urlMilanuncios != null && locationCityNumber > -1) {
            urlMilanuncios = urlMilanuncios.replace(URL_MILANUNCIOS_LOCATION_REPLACE, URL_MILANUNCIOS_LOCATION_REPLACE_FOR + arrayMilanuncios[locationCityNumber]);
        }
        txtSearch = txtSearch.replaceAll(" ", "-");
        try {
            urlMilanuncios = URL_MILANUNCIOS_PRE + URLEncoder.encode(txtSearch, "utf-8") + urlMilanuncios;
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlMilanuncios += URL_MILANUNCIOS_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlMilanuncios += URL_MILANUNCIOS_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlMilanuncios = URL_MILANUNCIOS_PRE + txtSearch.replaceAll(" ", "-") + urlMilanuncios;
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlMilanuncios += URL_MILANUNCIOS_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlMilanuncios += URL_MILANUNCIOS_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
//        Log.e("url", urlMilanuncios);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlMilanuncios,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("aditem");

                        if (items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Milanuncios", "Captcha encontrado");
                            } else {
                                Log.i("Milanuncios", "No hay elementos");
                            }
                            sendToHandler(UPDATE_LIST, NUM_MILANUNCIOS, null);
                            return;
                        }

                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_milanuncios), sizeMarketImage, sizeMarketImage);

                        isStoreFinished1.set(NUM_MILANUNCIOS, items.size());
                        //Log.e("response",response+"");
                        //Log.e("body",document.body()+"");
//                        Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String desc = item.getElementsByClass("tx").text();
                                if (desc.length() > 100) desc = desc.substring(0, 100) + "...";
                                final String description = desc;
                                desc = item.getElementsByClass("aditem-detail-title").text();
                                if (desc.length() > 30) desc = desc.substring(0, 30) + "...";
                                final String title = desc;
                                String aux = "";
//                                try {
                                aux = item.getElementsByClass("aditem-price").text();
                                if (aux == null || aux.length() < 1) aux = "0€";
                                /*} catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");
                                final String url = "https://www.milanuncios.com" + item.getElementsByTag("a").attr("href");
//                                try {
                                aux = item.getElementsByClass("ef").attr("src");
                                /*} catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = aux;
                                //Log.e("item",title+"");
                                //Log.e("item",description+"");
                                //Log.e("price",price+"");
//                                Log.e("url",url+"");
//                                Log.e("imgItem","url: "+imgItem+"");

                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_MILANUNCIOS, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                sendToHandler(ADD_TO_LIST, NUM_MILANUNCIOS, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("/fp/", "/fg/")));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
//                                                error.printStackTrace();
                                                Log.e("Error Milanuncios", "Catch1");
                                                sendToHandler(ADD_TO_LIST, NUM_MILANUNCIOS, null);
                                            }
                                        }
                                );
                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Error Milanuncios", "Catch2");
                                sendToHandler(ADD_TO_LIST, NUM_MILANUNCIOS, null);
                            }
                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_MILANUNCIOS, null);
                Log.e("Error Milanuncios", "Error Milanuncios");
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.milanuncios));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getCeX(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_CEX, null);
            return;
        }
        String urlCex = null;
        if (orderSelected == ORDER_BESTMATCH) urlCex = URL_CEX_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlCex = URL_CEX_ORDER_PRICE;
        try {
            urlCex += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlCex += URL_CEX_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlCex += URL_CEX_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlCex += txtSearch.replaceAll(" ", "%20");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlCex += URL_CEX_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlCex += URL_CEX_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
        //Log.e("url", urlCex);

        final RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCex,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jArray = jsonObject.getJSONObject("response").getJSONObject("data").optJSONArray("boxes");

                            if (jArray == null || jArray.length() == 0) {
                                Log.e("Cex", "No se han encontrado resultados");
                                sendToHandler(UPDATE_LIST, NUM_CEX, null);
                                return;
                            }
                            final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_cex), (int) (sizeMarketImage * 1.4), sizeMarketImage);
                            isStoreFinished1.set(NUM_CEX, jArray.length());
                            //Log.e("Cex", ""+jArray.length());
                            //Log.e("Cex", ""+jArray.toString());
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    String aux = oneObject.getString("boxName");
                                    if (aux.length() > 100) {
                                        aux = aux.substring(0, 100) + "...";
                                    }
                                    final String description = aux;
                                    if (aux.length() > 15) {
                                        aux = aux.substring(0, 15);
                                    }
                                    final String title = aux;
//                                    try {
                                    aux = oneObject.getString("sellPrice") + "€";
                                    if (aux.length() < 1) aux = "0€";
                                    /*} catch (Exception e) {
                                        aux = "0€";
                                    }*/
                                    final String price = aux.replace(",", "").replace(".", ",");
//                                    try {
                                    aux = oneObject.getJSONObject("imageUrls").getString("medium");
                                    /*} catch (Exception e) {
                                        aux = "";
                                    }*/
                                    final String imgItem = aux;
//                                    Log.e("imgItem",imgItem);
                                    final String url = "https://es.webuy.com/product-detail/?id=" + oneObject.getString("boxId");

                                    if (imgItem == null || imgItem.length() < 5) {
                                        sendToHandler(ADD_TO_LIST, NUM_CEX, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                        continue;
                                    }

                                    ImageRequest imageRequest = new ImageRequest(
                                            imgItem, // Image URL
                                            new Response.Listener<Bitmap>() { // Bitmap listener
                                                @Override
                                                public void onResponse(Bitmap response) {
                                                    sendToHandler(ADD_TO_LIST, NUM_CEX, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("_m.", "_l.")));
                                                }
                                            },
                                            sizeImage, // Image width
                                            sizeImage, // Image height
                                            ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                            Bitmap.Config.RGB_565, //Image decode configuration
                                            new Response.ErrorListener() { // Error listener
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    error.printStackTrace();
                                                    sendToHandler(ADD_TO_LIST, NUM_CEX, null);
                                                }
                                            }
                                    );

                                    // Add ImageRequest to the RequestQueue
                                    queue.add(imageRequest);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sendToHandler(ADD_TO_LIST, NUM_CEX, null);
                                    //Toast.makeText(mContext, "Error de JSON2 "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }//end for
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Error de JSON", Toast.LENGTH_SHORT).show();
                            Log.i("Cex", "Error JSON, No results");
                            sendToHandler(UPDATE_LIST, NUM_CEX, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_CEX, null);
                Log.e("Error Cex", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.cex));
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getCashConverters(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_CASH_CONVERTERS, null);
            return;
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlCashConverters = null;
        if (orderSelected == ORDER_BESTMATCH)
            urlCashConverters = URL_CASH_CONVERTERS_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlCashConverters = URL_CASH_CONVERTERS_ORDER_PRICE;
        try {
            urlCashConverters += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlCashConverters += URL_CASH_CONVERTERS_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlCashConverters += URL_CASH_CONVERTERS_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlCashConverters += txtSearch.replaceAll(" ", "%20");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlCashConverters += URL_CASH_CONVERTERS_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlCashConverters += URL_CASH_CONVERTERS_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }

        //Log.e("url", urlCashConverters);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCashConverters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response.length()+"");
                        //Log.e("response",response.substring(0,500)+"");
                        //Log.e("response2",response.substring(501,1000)+"");
                        //Log.e("response3",response.substring(1001,response.length())+"");
                        //if (response.length()> 1)return;
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("box-product");
                        //Log.e("size",items.size()+"");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error CashConverters", "Captcha encontrado");
                            } else Log.i("CashConverters", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_CASH_CONVERTERS, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_cash_converters), (int) (sizeMarketImage * 1.4f), sizeMarketImage);
                        isStoreFinished1.set(NUM_CASH_CONVERTERS, items.size());

                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = item.getElementsByClass("info").text();
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 15) aux = aux.substring(0, 15);
                                final String title = aux;

//                                try {
                                aux = item.getElementsByClass("product-sales-price").text();
                                if (aux == null || aux.length() < 1) aux = "0€";
                                /*} catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");

                                final String url = "https://www.cashconverters.es" + item.getElementsByTag("a").attr("href");
//                                try {
                                aux = item.getElementsByClass("product-image").first().getElementsByTag("img").attr("src");
                                /*} catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = aux;
                                //Log.e("item","price: "+title);
                                //Log.e("price","price: "+price);
                                //Log.e("url","url: "+url);
                                //Log.e("imgItem","img: "+imgItem);

                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_CASH_CONVERTERS, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                String imgUrl;
                                                int pos = imgItem.lastIndexOf(".jpg");
                                                if (pos > 0) imgUrl = imgItem.substring(0, pos + 4);
                                                else imgUrl = imgItem;
                                                sendToHandler(ADD_TO_LIST, NUM_CASH_CONVERTERS, new ListDataModel(response, title, description, price, marketplace, url, imgUrl));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_CASH_CONVERTERS, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_CASH_CONVERTERS, null);
                                Log.e("CashConverters", "Exception");
                            }

                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_CASH_CONVERTERS, null);
                Log.e("Error CashConverters", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.cash_converters));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getNolotire(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_NOLOTIRE, null);
            return;
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlNolotire = null;
        if (orderSelected == ORDER_BESTMATCH) urlNolotire = URL_NOLOTIRE_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlNolotire = URL_NOLOTIRE_ORDER_PRICE;
        try {
            urlNolotire += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlNolotire += URL_NOLOTIRE_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlNolotire += URL_NOLOTIRE_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlNolotire += txtSearch.replaceAll(" ", "%20");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlNolotire += URL_NOLOTIRE_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlNolotire += URL_NOLOTIRE_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }

        //Log.e("url", urlNolotire);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlNolotire,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response.length()+"");
                        //Log.e("response",response.substring(0,500)+"");
                        //Log.e("response2",response.substring(501,1000)+"");
                        //Log.e("response3",response.substring(1001,response.length())+"");
                        //if (response.length()> 1)return;
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("producto");
                        //Log.e("size",items.size()+"");
                        //Log.e("items",items.html()+"");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Nolotire", "Captcha encontrado");
                            } else Log.i("Nolotire", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_NOLOTIRE, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_nolotire), sizeMarketImage, sizeMarketImage);
                        isStoreFinished1.set(NUM_NOLOTIRE, items.size());

                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = item.getElementsByClass("nombre").text();
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 15) aux = aux.substring(0, 15);
                                final String title = aux;

//                                try {
                                aux = item.getElementsByClass("precio-texto").first().text() + ","
                                        + item.getElementsByClass("precio-texto-mini").first().text();
                                if (aux == null || aux.length() < 1) aux = "0€";
                                /*} catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");

                                final String url = item.getElementsByTag("a").attr("href");
//                                try {
                                aux = item.getElementsByClass("thumbnail").first().getElementsByTag("img").attr("src");
                                /*} catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = aux;
                                /*Log.e("item","title: "+title);
                                Log.e("price","price: "+price);
                                Log.e("url","url: "+url);
                                Log.e("imgItem","img: "+imgItem);
                                */
                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_NOLOTIRE, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                sendToHandler(ADD_TO_LIST, NUM_NOLOTIRE, new ListDataModel(response, title, description, price, marketplace, url, imgItem.replace("/low/", "/high/")));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_NOLOTIRE, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_NOLOTIRE, null);
                                Log.e("Nolotire", "Exception");
                            }

                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_NOLOTIRE, null);
                Log.e("Error Nolotire", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.nolotire));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getTodoColeccion(String txtSearch, String txtPriceMin, String txtPriceMax) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlTodoColeccion = null;
        if (orderSelected == ORDER_BESTMATCH) urlTodoColeccion = URL_TODOCOLECCION_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlTodoColeccion = URL_TODOCOLECCION_ORDER_PRICE;
        try {
            urlTodoColeccion += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlTodoColeccion += URL_TODOCOLECCION_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlTodoColeccion += URL_TODOCOLECCION_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlTodoColeccion += txtSearch.replaceAll(" ", "%20");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlTodoColeccion += URL_TODOCOLECCION_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlTodoColeccion += URL_TODOCOLECCION_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
        if (checkSearchOptionLocation && locationCityNumber > -1) {
            try {
                urlTodoColeccion += URL_TODOCOLECCION_LOCATION + URLEncoder.encode(arrayTodocoleccion[locationCityNumber], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                urlTodoColeccion += URL_TODOCOLECCION_LOCATION + arrayTodocoleccion[locationCityNumber];
            }
        }
//        Log.e("url", urlTodoColeccion);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlTodoColeccion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response.length()+"");
                        //Log.e("response",response.substring(0,response.length())+"");
                        //Log.e("response2",response.substring(501,1000)+"");
                        //Log.e("response3",response.substring(1001,response.length())+"");
                        //if (response.length()> 1)return;
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("lote-item");
                        //Log.e("size",items.size()+"");
                        //Log.e("items",items.html()+"");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error TodoColeccion", "Captcha encontrado");
                            } else Log.i("TodoColeccion", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_TODOCOLECCION, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_todocoleccion), sizeMarketImage, sizeMarketImage);
                        isStoreFinished1.set(NUM_TODOCOLECCION, items.size());

                        //Log.e("body",document.body()+"");
                        //Log.e("size", items.size() + "");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = item.getElementsByClass("lote-titulo-enlace").first().attr("title");
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 15) aux = aux.substring(0, 15);
                                final String title = aux;

//                                try {
                                aux = item.getElementsByClass("text-nowrap").first().text();
                                if (aux == null || aux.length() < 1) aux = "0€";
                               /* } catch (Exception e) {
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");

                                final String url = "https://www.todocoleccion.net" + item.getElementsByTag("a").attr("href");
//                                try {
                                aux = item.getElementsByClass("lote-con-foto").first().getElementsByTag("img").attr("src");
                               /* } catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = aux;
                                //Log.e("item","title: "+title);
                                //Log.e("price","price: "+price);
                                //Log.e("url","url: "+url);
                                //Log.e("imgItem","img: "+imgItem);

                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_TODOCOLECCION, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                String imgUrl;
                                                int pos = imgItem.lastIndexOf(".jpg");
                                                if (pos > 0) imgUrl = imgItem.substring(0, pos + 4);
                                                else imgUrl = imgItem;
                                                sendToHandler(ADD_TO_LIST, NUM_TODOCOLECCION, new ListDataModel(response, title, description, price, marketplace, url, imgUrl));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_TODOCOLECCION, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_TODOCOLECCION, null);
                                Log.e("TodoColeccion", "Exception");
                            }

                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_TODOCOLECCION, null);
                Log.e("Error TodoColeccion", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.todocoleccion));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getFnac(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_FNAC, null);
            return;
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlFnac = null;
        if (orderSelected == ORDER_BESTMATCH) urlFnac = URL_FNAC_ORDER_BESTMATCH;
        else if (orderSelected == ORDER_PRICE) urlFnac = URL_FNAC_ORDER_PRICE;
        try {
            urlFnac += URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlFnac += URL_FNAC_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            else if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlFnac += URL_FNAC_PRICE_MIN + "0";
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlFnac += URL_FNAC_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8")
                        + URL_FNAC_POST_PRICE_MAX;
            else if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlFnac += URL_FNAC_PRICE_MAX + "9999900" + URL_FNAC_POST_PRICE_MAX;
        } catch (UnsupportedEncodingException e) {
            urlFnac += txtSearch.replace(" ", "+");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlFnac += URL_FNAC_PRICE_MIN + txtPriceMin;
            else if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlFnac += URL_FNAC_PRICE_MIN + "0";
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlFnac += URL_FNAC_PRICE_MAX + txtPriceMax + URL_FNAC_POST_PRICE_MAX;
            else if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlFnac += URL_FNAC_PRICE_MAX + "9999900" + URL_FNAC_POST_PRICE_MAX;
            e.printStackTrace();
        }
        //Log.e("url", urlFnac);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFnac,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response.length()+"");
                        //Log.e("response",response.substring(0,response.length())+"");
                        //Log.e("response3",response.substring(1001,response.length())+"");
                        //if (response.length()> 1)return;
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("Article-itemGroup");
//                        Log.e("size", items.size() + "");
                        //Log.e("items",items.html()+"");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Fnac", "Captcha encontrado");
                            } else Log.i("Fnac", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_FNAC, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_fnac), sizeMarketImage, sizeMarketImage);
                        isStoreFinished1.set(NUM_FNAC, items.size());

                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = item.getElementsByClass("Article-desc").first().text();
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 15) aux = aux.substring(0, 15);
                                final String title = aux;
//                                Log.e("item1", title + "");
//                                try {
                                aux = item.getElementsByClass("userPrice").last().text();
                                if (aux == null || aux.length() < 1) aux = "0€";

                             /*   } catch (Exception e) {
                                    e.printStackTrace();
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");
//                                Log.e("item1", price + "");
                                final String url = item.getElementsByTag("a").first().attr("href");
//                                try {
                                aux = item.getElementsByClass("Article-itemVisual").first().getElementsByTag("img").attr("data-lazyimage");
                                /*} catch (Exception e) {
                                    e.printStackTrace();
                                    aux = "";
                                }*/
                                final String imgItem = aux;
//                                Log.e("item","title: "+title);
//                                Log.e("price","price: "+price);
//                                Log.e("url","url: "+url);
//                                Log.e("imgItem","img: "+imgItem);

                                if (imgItem == null || imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_FNAC, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                int posExt = imgItem.length() - 4;
                                                String urlImg = imgItem.substring(posExt);
                                                if ((urlImg.equals(".jpg"))) {
                                                    String size = imgItem.substring(71, 77);
                                                    if (size.startsWith("/"))
                                                        urlImg = imgItem.replaceAll(size, "/1507-");
                                                } else if (urlImg.equals(".gif"))
                                                    urlImg = imgItem.replace("/Grandes110_110/", "/ZoomPE/").replace(".gif", ".jpg");
                                                else urlImg = imgItem;
                                                sendToHandler(ADD_TO_LIST, NUM_FNAC, new ListDataModel(response, title, description, price, marketplace, url, urlImg));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_FNAC, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_FNAC, null);
                                Log.e("Fnac", "Exception");
                            }
                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_FNAC, null);
                Log.e("Error Fnac", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.fnac));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void getAmazon(String txtSearch, String txtPriceMin, String txtPriceMax) {
        if (checkSearchOptionLocation && checkSearchOptionLocationOnly) {
            sendToHandler(UPDATE_LIST, NUM_AMAZON, null);
            return;
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlAmazon = null;
        try {
            urlAmazon = URL_AMAZON + URLEncoder.encode(txtSearch, "utf-8");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlAmazon += URL_AMAZON_PRICE_MIN + URLEncoder.encode(txtPriceMin, "utf-8");
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlAmazon += URL_AMAZON_PRICE_MAX + URLEncoder.encode(txtPriceMax, "utf-8");
        } catch (UnsupportedEncodingException e) {
            urlAmazon = URL_AMAZON + txtSearch.replaceAll(" ", "+");
            if (txtPriceMin != null && !txtPriceMin.equals(""))
                urlAmazon += URL_AMAZON_PRICE_MIN + txtPriceMin;
            if (txtPriceMax != null && !txtPriceMax.equals(""))
                urlAmazon += URL_AMAZON_PRICE_MAX + txtPriceMax;
            e.printStackTrace();
        }
        urlAmazon = "https://www.amazon.es/s/ref=nb_sb_noss_1?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Daps&field-keywords=iphone";
//        Log.e("url", urlAmazon);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAmazon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("response",response.length()+"");
                        //Log.e("response",response.substring(0,500)+"");
                        //Log.e("response2",response.substring(501,1000)+"");
                        //Log.e("response3",response.substring(1001,response.length())+"");
                        //if (response.length()> 1)return;
                        Document document = Jsoup.parse(response);
                        final Elements items = document.body().getElementsByClass("sx-table-detail");
                        //Log.e("size",document.body()+"");
                        //Log.e("size",items.size()+"");
                        Log.e("s2", response.substring(response.indexOf("Apple iPhone X") - 500));
                        //Log.e("items",items.html()+"");
                        if (items == null || items.size() == 0) {
                            if (response.contains("captcha")) {
                                Log.e("Error Amazon", "Captcha encontrado");
                            } else Log.i("Amazon", "No hay elementos");
                            sendToHandler(UPDATE_LIST, NUM_AMAZON, null);
                            return;
                        }
                        final Bitmap marketplace = getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_amazon), sizeMarketImage, sizeMarketImage);
                        isStoreFinished1.set(NUM_AMAZON, items.size());
                        //Log.e("body",document.body()+"");
                        //Log.e("size",items.size()+"");
                        for (final Element item : items) {
                            //Log.e("item",item.html()+"");
                            //Log.e("key",items.size()+"");
                            try {
                                String aux = item.getElementsByClass("sx-title").first().text();
                                //Log.e("aux",aux+"");
                                if (aux.length() > 100) aux = aux.substring(0, 100) + "...";
                                final String description = aux;
                                if (aux.length() > 15) aux = aux.substring(0, 15);
                                final String title = aux;
                                aux = "0€";
//                                try {
                                int index = 0;
                                int index2;
                                double prc = Double.MAX_VALUE;
                                while (index > -1) {
                                    index = item.html().indexOf("EUR ", index);
                                    index2 = item.html().indexOf("</", index);
                                    if (index < 0) break;
                                    String aux2 = item.html().substring(index + 4, index2).replace(".", "");
                                    if (prc > Double.parseDouble(aux2.replace(",", "."))) {
                                        aux = aux2 + "€";
                                        prc = Double.parseDouble(aux2.replace(",", "."));
                                    }
                                    index = index2;
                                }
                                if (aux.length() < 1) aux = "0€";
                          /*      } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("error price", "error price");
                                    aux = "0€";
                                }*/
                                final String price = aux.replaceAll(" ", "").replaceAll(",00", "");

                                final String url = item.getElementsByTag("a").attr("href");
//                                try {
                                index = item.html().indexOf("data-search-image-load data-search-image-source-set=") + "data-search-image-load data-search-image-source-set=".length();
                                index2 = item.html().indexOf(" ", index);
                                Log.e("index", "index: " + index);
                                Log.e("index2", "index2: " + index2);
                                aux = item.html().substring(index, index2);

                                //aux = item.getElementsByTag("img").first().attr("src");
                                /*} catch (Exception e) {
                                    aux = "";
                                }*/
                                final String imgItem = aux;
                                Log.e("item", "title: " + title);
                                Log.e("price", "price: " + price);
                                Log.e("url", "url: " + url);
                                Log.e("imgItem", "img: " + imgItem);

                                if (imgItem.length() < 5) {
                                    sendToHandler(ADD_TO_LIST, NUM_AMAZON, new ListDataModel(marketplace, title, description, price, marketplace, url, null));
                                    continue;
                                }

                                ImageRequest imageRequest = new ImageRequest(
                                        imgItem, // Image URL
                                        new Response.Listener<Bitmap>() { // Bitmap listener
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                sendToHandler(ADD_TO_LIST, NUM_AMAZON, new ListDataModel(response, title, description, price, marketplace, url, imgItem));
                                            }
                                        },
                                        sizeImage, // Image width
                                        sizeImage, // Image height
                                        ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                                        Bitmap.Config.RGB_565, //Image decode configuration
                                        new Response.ErrorListener() { // Error listener
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                sendToHandler(ADD_TO_LIST, NUM_AMAZON, null);
                                            }
                                        }
                                );

                                // Add ImageRequest to the RequestQueue
                                queue.add(imageRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendToHandler(ADD_TO_LIST, NUM_AMAZON, null);
                                Log.e("Amazon", "Exception");
                            }

                        } //end for
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendToHandler(UPDATE_LIST, NUM_AMAZON, null);
                Log.e("Error Amazon", "" + error.getLocalizedMessage());
                error.printStackTrace();
                ToastError(mContext.getResources().getString(R.string.amazon));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sendToHandler(int what, int sendingUid, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        if (Build.VERSION.SDK_INT > 20) msg.sendingUid = sendingUid;
        else msg.arg2 = sendingUid;
        handler.sendMessage(msg);
    }

    private void ToastError(String store) {
        Toast.makeText(mContext, mContext.getResources().getString(R.string.search_error) + " " +
                store, Toast.LENGTH_SHORT).show();
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
