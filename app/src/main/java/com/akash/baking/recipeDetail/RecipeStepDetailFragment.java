package com.akash.baking.recipeDetail;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akash.baking.R;
import com.akash.baking.network.model.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeStepDetailFragment extends Fragment implements Player.EventListener, View.OnClickListener {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";

    private RecipeDetailActivity recipeDetailActivity;

    private List<Steps> stepsList;
    private int recipeStepPosition;
    private boolean isTabView;

    private FrameLayout frameMediaPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private TextView textRecipeStepDescription;
    private ImageView imageStepDetailThumbnail;
    private Button btnNext;
    private Button btnPrev;
    private TextView textNoVideoAvailable;

    private int resumeWindow;
    private long resumePosition;

    private int pixels;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        recipeDetailActivity = (RecipeDetailActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            stepsList = bundle.getParcelableArrayList("steps");
            recipeStepPosition = bundle.getInt("recipe_step_position", 0);
            isTabView = bundle.getBoolean("is_tab_view", false);
        }

        if (savedInstanceState != null) {
            resumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            resumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }

        frameMediaPlayer = view.findViewById(R.id.frame_media_player);
        exoPlayerView = view.findViewById(R.id.recipe_step_detail_media_player);
        textRecipeStepDescription = view.findViewById(R.id.text_recipe_step_description);
        imageStepDetailThumbnail = view.findViewById(R.id.image_step_detail_thumbnail);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        textNoVideoAvailable = view.findViewById(R.id.no_video_available);

        if(!isTabView){
            if (recipeDetailActivity != null) {
                float scale = recipeDetailActivity.getResources().getDisplayMetrics().density;
                pixels = (int) (250 * scale + 0.5f);

                if (recipeDetailActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    frameMediaPlayer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                } else {
                    frameMediaPlayer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
                }
            }
        }



        setupUI();

        if (stepsList.get(recipeStepPosition).getVideoURL() == null || TextUtils.isEmpty(stepsList.get(recipeStepPosition).getVideoURL())){
            textNoVideoAvailable.setVisibility(View.VISIBLE);
        } else {
            textNoVideoAvailable.setVisibility(View.GONE);
            initExoPlayer();

        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, resumeWindow);
        outState.putLong(STATE_RESUME_POSITION, resumePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if(!isTabView){
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                frameMediaPlayer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                frameMediaPlayer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
            }
        }

    }

    private void initExoPlayer() {

            String userAgent = Util.getUserAgent(recipeDetailActivity, recipeDetailActivity
                    .getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory
                    (userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory
                    (recipeDetailActivity, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(stepsList.get(recipeStepPosition).getVideoURL());

            MediaSource mediaSource = new ExtractorMediaSource(daUri, dataSourceFactory, new
                    DefaultExtractorsFactory(), null, null);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory
                    (bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory
                    (recipeDetailActivity), trackSelector, loadControl);
            exoPlayerView.setPlayer(player);

            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;

            if (haveResumePosition) {
                exoPlayerView.getPlayer().seekTo(resumeWindow, resumePosition);
            }

            exoPlayerView.getPlayer().prepare(mediaSource);
            exoPlayerView.getPlayer().setPlayWhenReady(true);
            exoPlayerView.getPlayer().addListener(this);

    }

    private void setupUI() {

        textRecipeStepDescription.setText(stepsList.get(recipeStepPosition).getDescription());

        String thumbnailURL = stepsList.get(recipeStepPosition).getThumbnailURL();
        if (thumbnailURL == null || TextUtils.isEmpty(thumbnailURL)) {
            imageStepDetailThumbnail.setVisibility(View.GONE);
        } else {
            imageStepDetailThumbnail.setVisibility(View.VISIBLE);
            Picasso.with(recipeDetailActivity)
                    .load(thumbnailURL)
                    .into(imageStepDetailThumbnail);
        }


        if (isTabView) {
            btnNext.setVisibility(View.GONE);
            btnPrev.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);

            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onDestroy() {
        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
            exoPlayerView.getPlayer().stop();
            exoPlayerView.getPlayer().release();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }


    @Override
    public void onResume() {
        super.onResume();
        releasePlayer();
        initExoPlayer();
    }

    public void releasePlayer(){
        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
            resumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
            resumePosition = Math.max(0, exoPlayerView.getPlayer().getContentPosition());

            exoPlayerView.getPlayer().stop();
            exoPlayerView.getPlayer().release();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_next:
                if (recipeStepPosition < stepsList.size() - 1) {
                    recipeStepPosition++;
                } else
                    recipeStepPosition = 0;
                releasePlayer();
                recipeDetailActivity.getSupportFragmentManager().popBackStack();
                recipeDetailActivity.launchRecipeStepDetailFragment(recipeStepPosition);
                break;

            case R.id.btn_prev:
                if (recipeStepPosition > 0) {
                    recipeStepPosition--;
                } else
                    recipeStepPosition = stepsList.size() - 1;
                releasePlayer();
                recipeDetailActivity.getSupportFragmentManager().popBackStack();
                recipeDetailActivity.launchRecipeStepDetailFragment(recipeStepPosition);
                break;
        }
    }
}
