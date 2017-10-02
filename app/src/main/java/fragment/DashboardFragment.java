package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.trikarya.growth.Draft;
import android.trikarya.growth.ProfilUser;
import android.trikarya.growth.R;
import android.trikarya.growth.About;
import android.trikarya.growth.History;
import android.trikarya.growth.MainActivity;
import android.trikarya.growth.NearbyOutletActivity;
import android.trikarya.growth.OutletList;
import android.trikarya.growth.SplashScreen;
import android.trikarya.growth.SubmitVisit;
import android.trikarya.growth.TakePhoto;
import android.trikarya.growth.VisitPlan;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.BannerAdapter;
import master.Banner;
import master.City;
import master.Competitor;
import master.DatabaseHandler;
import master.Distributor;
import master.Outlet;
import master.Produk;
import master.ServerRequest;
import master.Tipe;
import master.TipePhoto;
import master.User;
import master.VisitPlanDb;
import network.ConnectionHandler;
import network.JsonCallback;
import network.UserNetwork;
import utility.ImageHelper;

/**
 * Created by Hendry on 9/19/2017.
 */
//
public class DashboardFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    LinearLayout menu_outlet, menu_visit_plan, menu_submit_visit, menu_take_photo,
            menu_history, menu_refresh, menu_nearby, menu_about, menu_logout, menu_draft, menu_profile;

    TextView warning, namaUser, kd_user, areaUser, kotaUser;
    ServerRequest serverRequest;
    DatabaseHandler databaseHandler;
    User user;
    UserNetwork userNetwork;
    ProgressBar progressBar;
    ImageHelper imageHelper;
    BannerAdapter bannerAdapter;
    ViewPager viewPager;
    List<Bitmap> bitmaps;
    List<Banner> banners;
    View ind1, ind2, ind3, aktif;
    int width;

    List<Outlet> outletList;
    List<City> cityList;
    List<Distributor> distributorList;
    List<VisitPlanDb> visitPlanDbs;
    List<Tipe> tipeList;
    List<Produk> produkList;
    List<Competitor> competitors;
    List<TipePhoto> tipePhotos;

    private ArticleFragment.OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        databaseHandler = new DatabaseHandler(getContext());
        imageHelper = new ImageHelper(getContext());
        userNetwork = new UserNetwork(getContext());

        viewPager = (ViewPager) view.findViewById(R.id.slider);
        namaUser = (TextView) view.findViewById(R.id.user_name);
        namaUser.setText(databaseHandler.getUser().getNama());
        kd_user = (TextView) view.findViewById(R.id.user_code);
        kd_user.setText(databaseHandler.getUser().getNIK());
        warning = (TextView) view.findViewById(R.id.message);
        areaUser = (TextView) view.findViewById(R.id.area_code);
        areaUser.setText(databaseHandler.getUser().getArea_code());
        kotaUser = (TextView) view.findViewById(R.id.area_name);

        ind1 = view.findViewById(R.id.ind1);
        ind2 = view.findViewById(R.id.ind2);
        ind3 = view.findViewById(R.id.ind3);
        bitmaps = ((MainActivity)getActivity()).getBitmaps();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        menu_outlet = (LinearLayout) view.findViewById(R.id.menu_outlet);
        menu_outlet.setOnClickListener(this);
        menu_visit_plan = (LinearLayout) view.findViewById(R.id.menu_visitplan);
        menu_visit_plan.setOnClickListener(this);
        menu_submit_visit = (LinearLayout) view.findViewById(R.id.menu_submit);
        menu_submit_visit.setOnClickListener(this);
        menu_nearby = (LinearLayout) view.findViewById(R.id.menu_nearby_outlet);
        menu_nearby.setOnClickListener(this);
        menu_about = (LinearLayout) view.findViewById(R.id.menu_about);
        menu_about.setOnClickListener(this);
        menu_take_photo = (LinearLayout) view.findViewById(R.id.menu_take_photo);
        menu_take_photo.setOnClickListener(this);
        menu_history = (LinearLayout) view.findViewById(R.id.menu_history);
        menu_history.setOnClickListener(this);
        menu_draft = (LinearLayout) view.findViewById(R.id.menu_draft);
        menu_draft.setOnClickListener(this);
        menu_profile = (LinearLayout) view.findViewById(R.id.menu_profile);
        menu_profile.setOnClickListener(this);
        menu_refresh = (LinearLayout) view.findViewById(R.id.menu_refresh);
        menu_refresh.setOnClickListener(this);
        menu_logout = (LinearLayout) view.findViewById(R.id.menu_logout);
        menu_logout.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        ((TextView)view.findViewById(R.id.tahun)).setText(" "+String.valueOf(cal.get(Calendar.YEAR))+" ");

        if(bitmaps.size() == 0 ){
            banners = databaseHandler.getAllBanner();
            if(banners.size() == 0 || (cal.getTimeInMillis() - Double.parseDouble(banners.get(0).getTanggal()))>= 24*60*60*1000){
                userNetwork.getBanner(new JsonCallback() {
                    @Override
                    public void Done(JSONObject jsonObject, String message) {
                        if (message.equals(ConnectionHandler.response_message_success) && jsonObject != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(ConnectionHandler.response_data));
                                databaseHandler.deleteAllBanner();
                                Log.d("response banner >>> ", String.valueOf(jsonArray));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString("path_image"), Base64.DEFAULT);
                                    Log.d("decoded", String.valueOf(decodedString));
                                    Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                                    bitmaps.add(bitmap);
                                    databaseHandler.createBanner(
                                            new Banner(0, jsonArray.getJSONObject(i).getString("path_image"), String.valueOf(cal.getTimeInMillis())));
                                }
                                progressBar.setVisibility(View.GONE);
                                if (bitmaps.size() >= 1) {
                                    instanceBanner();
                                    ((MainActivity)getActivity()).setBitmaps(bitmaps);
                                } else {
                                    warning.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                progressBar.setVisibility(View.GONE);
                                warning.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            else {
                for(int i = 0; i < banners.size(); i++) {
                    byte[] decodedString = Base64.decode(banners.get(i).getImage(), Base64.DEFAULT);
                    Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                    bitmaps.add(bitmap);
                }
                if(bitmaps.size() >= 1) {
                    instanceBanner();
                    ((MainActivity)getActivity()).setBitmaps(bitmaps);
                }
            }
        }
        else if(bitmaps.size() > 0){
            instanceBanner();
        }
        return view;
    }

    private void instanceBanner(){
        ind1.setVisibility(View.VISIBLE);
        if(bitmaps.size() >= 2) {
            ind2.setVisibility(View.VISIBLE);
            if(bitmaps.size() >= 3)
                ind3.setVisibility(View.VISIBLE);
        }
        ind1.setActivated(true);
        aktif = ind1;
        bannerAdapter = new BannerAdapter(getContext(), bitmaps);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(bannerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (aktif != null)
                    aktif.setActivated(false);
                switch (position) {
                    case 0:
                        ind1.setActivated(true);
                        aktif = ind1;
                        break;
                    case 1:
                        ind2.setActivated(true);
                        aktif = ind2;
                        break;
                    default:
                        ind3.setActivated(true);
                        aktif = ind3;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_outlet:
                getActivity().startActivity(new Intent(getContext(), OutletList.class));
                getActivity().finish();
                break;
            case R.id.menu_visitplan:
                getActivity().startActivity(new Intent(getContext(), VisitPlan.class));
                getActivity().finish();
                break;
            case R.id.menu_submit:
                getActivity().startActivity(new Intent(getContext(), SubmitVisit.class));
                getActivity().finish();
                break;
            case R.id.menu_nearby_outlet:
                getActivity().startActivity(new Intent(getContext(), NearbyOutletActivity.class));
                getActivity().finish();
                break;
            case R.id.menu_take_photo:
                getActivity().startActivity(new Intent(getContext(), TakePhoto.class));
                getActivity().finish();
                break;
            case R.id.menu_about:
                getActivity().startActivity(new Intent(getContext(), About.class));
                getActivity().finish();
                break;
            case R.id.menu_history:
                getActivity().startActivity(new Intent(getContext(), History.class));
                getActivity().finish();
                break;
            case R.id.menu_draft:
                getActivity().startActivity(new Intent(getContext(), Draft.class));
                getActivity().finish();
                break;
            case R.id.menu_profile:
                getActivity().startActivity(new Intent(getContext(), ProfilUser.class));
                getActivity().finish();
                break;
            case R.id.menu_refresh:
                outletList = new ArrayList<Outlet>();
                cityList = new ArrayList<City>();
                distributorList = new ArrayList<Distributor>();
                visitPlanDbs = new ArrayList<VisitPlanDb>();
                tipeList = new ArrayList<Tipe>();
                produkList = new ArrayList<Produk>();
                competitors = new ArrayList<Competitor>();
                tipePhotos = new ArrayList<TipePhoto>();
                JsonCallback jsonCallback = new JsonCallback() {
                    @Override
                    public void Done(JSONObject jsonObject, String message)
                    {
                        if (message.equals(ConnectionHandler.response_message_success) && jsonObject != null) {
                            JSONObject response = null;
                            try {
                                response = new JSONObject(jsonObject.getString("data"));
                                JSONArray jsonArray;
                                if (response.has("kota")) {
                                    jsonArray = response.getJSONArray("kota");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            cityList.add(new City(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_area"), jsonResponse.getString("nm_kota")));
                                        }
                                    }
                                }
                                if (response.has("competitor")) {
                                    jsonArray = response.getJSONArray("competitor");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            competitors.add(new Competitor(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_competitor"),
                                                    jsonResponse.getString("alamat")));
                                        }
                                    }
                                }
                                if (response.has("outlet")) {
                                    jsonArray = response.getJSONArray("outlet");
                                    Outlet outlet;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            outlet = new Outlet(jsonResponse.getInt("kd_outlet"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getInt("kd_user"),
                                                    jsonResponse.getInt("kd_dist"), jsonResponse.getString("nm_outlet"),
                                                    jsonResponse.getString("almt_outlet"), jsonResponse.getInt("kd_tipe"),
                                                    jsonResponse.getString("rank_outlet"), jsonResponse.getString("kodepos"),
                                                    jsonResponse.getString("reg_status"), jsonResponse.getString("latitude"),
                                                    jsonResponse.getString("longitude"));
                                            outlet.setStatus_area(jsonResponse.getInt("status_area"));
                                            outletList.add(outlet);
                                        }
                                    }
                                }
                                if (response.has("distributor")) {
                                    jsonArray = response.getJSONArray("distributor");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            distributorList.add(new Distributor(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("kd_dist"), jsonResponse.getString("kd_tipe"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_dist"),
                                                    jsonResponse.getString("almt_dist"), jsonResponse.getString("telp_dist")));
                                        }
                                    }
                                }
                                if (response.has("tipe")) {
                                    jsonArray = response.getJSONArray("tipe");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            tipeList.add(new Tipe(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("nm_tipe")));
                                        }
                                    }
                                }
                                if (response.has("visitplan")) {
                                    jsonArray = response.getJSONArray("visitplan");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            visitPlanDbs.add(new VisitPlanDb(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                                                    jsonResponse.getString("date_create_visit"), jsonResponse.getInt("approve_visit"),
                                                    jsonResponse.getInt("status_visit"), jsonResponse.getString("date_visiting"),
                                                    jsonResponse.getString("skip_order_reason"), jsonResponse.getString("skip_reason")));
                                        }
                                    }
                                }
                                if (response.has("produk")) {
                                    jsonArray = response.getJSONArray("produk");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            produkList.add(new Produk(jsonResponse.getInt("id"), jsonResponse.getString("kd_produk"),
                                                    jsonResponse.getString("nm_produk")));
                                        }
                                    }
                                }
                                if (response.has("tipe_photo")) {
                                    jsonArray = response.getJSONArray("tipe_photo");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            tipePhotos.add(new TipePhoto(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("nama_tipe")));
                                        }
                                    }
                                }
                                databaseHandler.deleteAll();
                                insertCityToDB(cityList);
                                insertDistributorToDB(distributorList);
                                insertOutletToDB(outletList);
                                insertTipeToDB(tipeList);
                                insertVisitPlanToDB(visitPlanDbs);
                                insertProdukToDB(produkList);
                                insertCompetitor(competitors);
                                insertTipePhoto(tipePhotos);
                                User user = databaseHandler.getUser();
                                user.setStatus(1);
                                databaseHandler.updateUser(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), this, true);
                    }
                };
                userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), jsonCallback, true);
                break;
            case R.id.menu_logout:
                databaseHandler.deleteAll();
                databaseHandler.deleteUser();
                getActivity().finish();
                startActivity(new Intent(getContext(), SplashScreen.class));
                break;
        }
    }

    private void insertCompetitor(List<Competitor> competitors) {
        int count = competitors.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createCompetitor(competitors.get(i));
    }

    private void insertVisitPlanToDB(List<VisitPlanDb> visitPlanDbs) {
        int count = visitPlanDbs.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createVisitPlan(visitPlanDbs.get(i));
    }

    private void insertProdukToDB(List<Produk> produkList) {
        int count = produkList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createProduk(produkList.get(i));
    }

    private void insertTipeToDB(List<Tipe> tipeList) {
        int count = tipeList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createTipe(tipeList.get(i));
    }

    private void insertTipePhoto(List<TipePhoto> tipeList) {
        int count = tipeList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createTipePhoto(tipeList.get(i));
    }

    private void insertDistributorToDB(List<Distributor> distributorList) {
        int count = distributorList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createDistributor(distributorList.get(i));
    }

    private void insertCityToDB(List<City> cityList) {
        int count = cityList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createCity(cityList.get(i));
    }

    private void insertOutletToDB(List<Outlet> outletList) {
        int count = outletList.size();
        for (int i = 0; i < count; i++)
            databaseHandler.createOutlet(outletList.get(i));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
