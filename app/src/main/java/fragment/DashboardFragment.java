package fragment;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import adapter.BannerAdapter;
import master.Banner;
import master.City;
import master.Competitor;
import master.DatabaseHandler;
import master.Distributor;
import master.Outlet;
import master.Produk;
import master.Tipe;
import master.TipePhoto;
import master.VisitPlanDb;
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

    DatabaseHandler databaseHandler;
    UserNetwork userNetwork;
    List<Outlet> outletList;
    List<City> cityList;
    List<Distributor> distributorList;
    List<VisitPlanDb> visitPlanDbs;
    List<Tipe> tipeList;
    List<Produk> produkList;
    List<Competitor> competitors;
    List<TipePhoto> tipePhotos;
    TextView namaUser, kd_user, areaUser, kotaUser, warning;
    ProgressBar progressBar;
    View ind1, ind2, ind3, aktif;
    ViewPager viewPager;
    BannerAdapter bannerAdapter;
    List<Bitmap> bitmaps;
    List<Banner> banners;
    ImageHelper imageHelper;
    City city;
    int kd_kota,width ;
    private OnFragmentInteractionListener mListener;

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
        warning = (TextView) view.findViewById(R.id.message);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        NavigationView navigationView = (NavigationView) view.findViewById(android.trikarya.growth.R.id.nav_view);


//        namaUser = (TextView) navigationView.getHeaderView(0).findViewById(android.trikarya.growth.R.id.tvusername);
//        namaUser.setText(databaseHandler.getUser().getNama());
//        kd_user = (TextView) view.findViewById(R.id.user_code);
//        kd_user.setText(databaseHandler.getUser().getNIK());
//        areaUser = (TextView) view.findViewById(R.id.area_code);
//        areaUser.setText(databaseHandler.getUser().getArea_code());
//        kotaUser = (TextView) view.findViewById(R.id.area_name);
//        kd_kota = databaseHandler.getAllOutlet().size() > 0 ? databaseHandler.getAllOutlet().get(0).getKode_kota() : databaseHandler.getUser().getKd_kota();
//        city = databaseHandler.getCity(kd_kota);
//        kotaUser.setText(city == null ? "" : city.getNama());
//        userNetwork = new UserNetwork(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
