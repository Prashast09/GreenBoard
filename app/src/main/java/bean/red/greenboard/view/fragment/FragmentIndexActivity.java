package bean.red.greenboard.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bean.red.greenboard.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentIndexActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentIndexActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentIndexActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int[] tabIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_rss_feed_white_24dp,
            R.drawable.ic_history_white_24dp
    };
    private Toolbar toolbar;
    public FragmentIndexActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentIndexActivity.
     */
    // TODO: Rename and change types and number of parameters
   /* public static FragmentIndexActivity newInstance(String param1, String param2) {
        FragmentIndexActivity fragment = new FragmentIndexActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        toolbar = (Toolbar)v.findViewById(R.id.my_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
         //initBottomBarLayout(v);
        // Inflate the layout for this fragment
        return v;
    }

//    private void initBottomBarLayout(final View rootView) {
//        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
//        indexTabAdapter = new IndexTabAdapter(getActivity().getSupportFragmentManager());
//        indexTabAdapter.addFragment(new FragmentAnnouncement(), "Announcement");
//        indexTabAdapter.addFragment(new FragmentSubscription(), "Subscriptions");
//        indexTabAdapter.addFragment(new FragmentProfile(), "Profile");
//        viewPager.setAdapter(indexTabAdapter);
//        viewPager.setOffscreenPageLimit(2);
//        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//
//    }

   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
