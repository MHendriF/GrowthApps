package layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.trikarya.growth.ProcessOrder;
import android.trikarya.growth.R;
import android.trikarya.growth.SkipOrder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 *   PT Trikarya Teknologi
 */
public class SubmitVisitComplete extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button takeOrder,skipOrder;
    int kd_visit;
    private OnFragmentInteractionListener mListener;

    public static SubmitVisitComplete newInstance(String param1, String param2) {
        SubmitVisitComplete fragment = new SubmitVisitComplete();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubmitVisitComplete() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_visit_complete, container, false);
        kd_visit = getArguments().getInt("kd_visit");
        takeOrder = (Button) view.findViewById(R.id.sv_takeorder);
        takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ProcessOrder.class);
                intent.putExtra("kd_visit",kd_visit);
                startActivity(intent);
                getActivity().finish();
            }
        });
        skipOrder = (Button) view.findViewById(R.id.sv_skiporder);
        skipOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SkipOrder.class);
                intent.putExtra("kd_visit",kd_visit);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    public void goToOrder(View view)
    {
        Intent intent;
        switch (view.getId()){
            case R.id.sv_takeorder:
                intent = new Intent(getActivity(), ProcessOrder.class);
                break;
            default:
                intent = new Intent(getActivity(), SkipOrder.class);
                break;
        }
        startActivity(intent);
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
