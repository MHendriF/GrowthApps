package fragment;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.ArticleListAdapter;
import master.ArticleClass;
import master.DatabaseHandler;
import network.ConnectionHandler;
import network.JsonCallback;
import network.ArticleNetwork;

/**
 * Created by Hendry on 9/26/2017.
 */

public class ArticleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArticleNetwork articleNetwork;
    ListView listView;
    List<ArticleClass> articleClasses;
    ArticleListAdapter articleListAdapter;
    int width;
    DatabaseHandler databaseHandler;

    private OnFragmentInteractionListener mListener;

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance(String param1, String param2) {
        ArticleFragment fragment = new ArticleFragment();
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
        articleNetwork = new ArticleNetwork(getContext());
        databaseHandler = new DatabaseHandler(getContext());
        articleClasses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        listView = (ListView) view.findViewById(R.id.list_article);
        articleClasses = databaseHandler.getAllArticle();
        Collections.sort(articleClasses);
        if(articleClasses.size() == 0) {
            articleNetwork.getList(new JsonCallback() {
                @Override
                public void Done(JSONObject jsonObject, String message) {
                    if (jsonObject != null && message.equals(ConnectionHandler.response_message_success)) {
                        try {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString(ConnectionHandler.response_data));
                            JSONObject current = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                current = jsonArray.getJSONObject(i);
                                ArticleClass articleclass = new ArticleClass(current.getInt("id"), current.getInt("status"), current.getString("judul")
                                        , current.getString("headline"), current.getString("content"), current.getString("path_image")
                                        , current.getString("date_upload"));
                                articleClasses.add(articleclass);
                                databaseHandler.createArticle(articleclass);
                            }
                            if (articleClasses.size() > 0) {
                                articleListAdapter = new ArticleListAdapter(getActivity(), articleClasses, width);
                                listView.setAdapter(articleListAdapter);
                            } else
                                Toast.makeText(getContext(), "Tidak ada konten yang tersedia", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Terjadi kesalahan, silahkan muat ulang halaman", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else if (articleClasses.size() > 0) {
            articleListAdapter = new ArticleListAdapter(getActivity(), articleClasses, width);
            listView.setAdapter(articleListAdapter);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
