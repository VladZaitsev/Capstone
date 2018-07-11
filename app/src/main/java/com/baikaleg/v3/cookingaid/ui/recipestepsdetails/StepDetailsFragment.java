package com.baikaleg.v3.cookingaid.ui.recipestepsdetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.model.Step;
import com.baikaleg.v3.cookingaid.databinding.FragmentStepDetailsBinding;

import com.baikaleg.v3.cookingaid.util.Constants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String arg_step = "argStep";

    private SimpleExoPlayer exoPlayer;
    private FragmentStepDetailsBinding binding;

    private long playerPosition;
    private boolean playerReady = true;
    private Step step;

    private StepDetailsViewModel viewModel;

    public static StepDetailsFragment newInstance(Step step) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(arg_step, step);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.PLAYER_POSITION)) {
                playerPosition = savedInstanceState.getLong(Constants.PLAYER_POSITION);
            }
            if (savedInstanceState.containsKey(Constants.PLAYER_READY)) {
                playerReady = savedInstanceState.getBoolean(Constants.PLAYER_READY);
            }
        }
        viewModel = new StepDetailsViewModel();
        if (getArguments() != null) {
            step = getArguments().getParcelable(arg_step);
            viewModel.setStep(step);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepDetailsBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }

    public void initializePlayer() {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory videoTrackSelectionFactory
                    = new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector
                    = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            if (getActivity() != null) {
                exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                binding.stepDetailsPlayer.requestFocus();
                binding.stepDetailsPlayer.setPlayer(exoPlayer);

                exoPlayer.addListener(this);

                // Prepare the MediaSource.
                if (!TextUtils.isEmpty(step.getVideoURL())) {
                    String userAgent = Util.getUserAgent(getActivity(), getActivity().getPackageName());
                    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(step.getVideoURL()),
                            new DefaultDataSourceFactory(getActivity(), userAgent),
                            new DefaultExtractorsFactory(), null, null);
                    exoPlayer.prepare(mediaSource, false, true);
               //     exoPlayer.setPlayWhenReady(playerReady);
                    exoPlayer.seekTo(playerPosition);
                }
            }
        }
    }

    public void releasePlayer() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            playerReady = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(Constants.PLAYER_POSITION, playerPosition);
        outState.putBoolean(Constants.PLAYER_READY, playerReady);
        super.onSaveInstanceState(outState);
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
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                break;
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
