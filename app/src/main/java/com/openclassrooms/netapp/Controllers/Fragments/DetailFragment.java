package com.openclassrooms.netapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.netapp.Models.GithubUser;
import com.openclassrooms.netapp.Models.GithubUserInfo;
import com.openclassrooms.netapp.R;
import com.openclassrooms.netapp.Utils.GithubStreams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class DetailFragment extends Fragment {


    @BindView(R.id.text_view_user_name) TextView user_name;
    @BindView(R.id.text_view_following_num) TextView following;
    @BindView(R.id.text_view_followers_num) TextView followers;
    @BindView(R.id.text_view_public_repos_num) TextView public_repos;
    @BindView(R.id.image_view_avatar) ImageView avatar;

    String username;
    private Disposable disposable;
    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.username = bundle.getString("username");
            this.executeHttpRequest(username);
        }
        Log.d("DetailFragment", "in onCreateView()");
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -------------------
    // HTTP (Retrofit & RxJAVA)
    // -------------------

    private void executeHttpRequest(String id) {
        this.disposable = GithubStreams.streamFetchUserInfos(id).subscribeWith(new DisposableObserver<GithubUserInfo>() {
            @Override
            public void onNext(@NonNull GithubUserInfo githubUserInfo) {
                Log.d("DetailFragment", githubUserInfo.getLogin());
                Log.d("DetailFragment", githubUserInfo.getFollowing().toString());
                Log.d("DetailFragment", githubUserInfo.getFollowers().toString());
                Log.d("DetailFragment", githubUserInfo.getPublicRepos().toString());
                Log.d("DetailFragment", githubUserInfo.getAvatarUrl());
                updateUI(githubUserInfo);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                updateUIonError();
            }

            @Override
            public void onComplete() {
                Log.d("DetailFragment", "in Request onComplete()");
            }
        });
        Log.d("DetailFragment", "in executeHttpRequest()");
    }


    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
    // -------------------
    // UPDATE UI
    // -------------------


    private void updateUI(GithubUserInfo userInfo) {
        this.user_name.setText(String.valueOf(userInfo.getLogin()));
        this.following.setText(String.valueOf(userInfo.getFollowing()));
        this.followers.setText(String.valueOf(userInfo.getFollowers()));
        this.public_repos.setText(String.valueOf(userInfo.getPublicRepos()));
        Glide.with(this).load(userInfo.getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(avatar);
        Log.d("DetailFragment", "in updateUI()");

    }

    private void updateUIonError() {
        this.user_name.setText("Error");
        this.following.setText("Error");
        this.followers.setText("Error");
        this.public_repos.setText("Error");
        this.avatar.setImageResource(R.drawable.avatar_default);
    }
}