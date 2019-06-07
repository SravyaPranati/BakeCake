package com.example.bakecake.others;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakecake.R;
import com.example.bakecake.activities.RecipeItemDetailActivity;
import com.example.bakecake.activities.RecipeItemListActivity;
import com.example.bakecake.dummy.DummyContent;
import com.example.bakecake.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecipeItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID ="item" ;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "PlayerActivity";

    private SimpleExoPlayer player;
    private PlayerView playerView;
    ImageView imageView;
    TextView textView;
    String videoURL="";
   // private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    List<Step> steps;
    Step s;
    public RecipeItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            s =getArguments().getParcelable(ARG_ITEM_ID);
            if(s==null)
            Log.i("steps",""+steps.get(0).getVideoURL());

            //Toast.makeText(activity, steps.get(0).getVideoURL(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipeitem_detail, container, false);
        //   componentListener = new ComponentListener();
        playerView = rootView.findViewById(R.id.exo_player);
        imageView = rootView.findViewById(R.id.id_thumbNail);
        textView = rootView.findViewById(R.id.s_description);
        Activity activity=this.getActivity();
        textView.setText(s.getDescription());
        Log.i("ThumbNail",""+s.getThumbnailURL());
        if (!s.getVideoURL().equals("") && s.getVideoURL() != null) {
            videoURL = s.getVideoURL();
            settingVideo();

        }
        else if (s.getThumbnailURL().contains(".mp4")) {
            videoURL = s.getThumbnailURL();
            settingVideo();
        } else if(s.getThumbnailURL() != null && s.getThumbnailURL() != "") {
            //Picasso.with(getActivity()).load(s.getThumbnailURL()).placeholder(R.mipmap.ic_launcher).into(imageView);
            playerView.setVisibility(View.GONE);
        }
        if(savedInstanceState!=null){
            playbackPosition=savedInstanceState.getLong("position");
            player.seekTo(playbackPosition);
            playWhenReady=savedInstanceState.getBoolean("whenReady");
            player.setPlayWhenReady(playWhenReady);

        }
        return rootView;

    }

    private void settingVideo() {

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            Log.i("videoURL", videoURL);
            playerView.setPlayer(player);
            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            playerView.setVisibility(View.VISIBLE);
            player.prepare(mediaSource);
            player.seekTo(playbackPosition);
            player.setPlayWhenReady(true);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
        } catch (Exception e) {
            Log.e("MainAcvtivity", " exoplayer error " + e.toString());
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(player!=null){
            outState.putLong("position",player.getCurrentPosition());
            Log.i("Hello",""+player.getCurrentPosition());
            outState.putBoolean("whenReady",player.getPlayWhenReady());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            player=null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer(){
        if (player != null) {
            player.stop();
            player.release();
        }
    }




}
