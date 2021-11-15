package com.example.git.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.git.R;
import com.example.git.adapters.ImageSliderAdapter;
import com.example.git.databinding.ActivityMovieDetailsBinding;
import com.example.git.viewmodels.MovieDetailsViewModel;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding activityMovieDetailsBinding;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        doIntialization();
    }

    private void doIntialization() {
        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        activityMovieDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        getMovieDetails();
    }

    private void getMovieDetails() {
        activityMovieDetailsBinding.setIsLoading(true);
        String movieId = String.valueOf(getIntent().getIntExtra("id", -1));
        movieDetailsViewModel.getMovieDetails(movieId).observe(
                this, movieDetailsResponse -> {
                    activityMovieDetailsBinding.setIsLoading(false);
                    if (movieDetailsResponse.getMovieDetails() != null){
                        if(movieDetailsResponse.getMovieDetails().getPictures() !=null){
                            loadImageSlider(movieDetailsResponse.getMovieDetails().getPictures());
                        }
                        activityMovieDetailsBinding.setMovieImageURL(
                                movieDetailsResponse.getMovieDetails().getImagePath()
                        );
                        activityMovieDetailsBinding.imageMovieShow.setVisibility((View.VISIBLE));
                        loadBasicMovieShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider (String[] sliderImages){
        activityMovieDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityMovieDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityMovieDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityMovieDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators (int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i=0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
               getApplicationContext(),
               R.drawable.background_slider_indicator_active
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityMovieDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityMovieDetailsBinding.layoutSliderIndicators.setVisibility((View.VISIBLE));
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position){
        int childCount = activityMovieDetailsBinding.layoutSliderIndicators.getChildCount();
        for(int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) activityMovieDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if(i==position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }
    private void loadBasicMovieShowDetails(){
        activityMovieDetailsBinding.setMovieName(getIntent().getStringExtra("name"));
        activityMovieDetailsBinding.setNetworkCountry(
                getIntent().getStringExtra("network")+ "(" +
                        getIntent().getStringExtra("country") + ")"
        );
        activityMovieDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityMovieDetailsBinding.setStartedDate(getIntent().getStringExtra("startDate"));
        activityMovieDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.textStatus.setVisibility(View.VISIBLE);
    }
}
