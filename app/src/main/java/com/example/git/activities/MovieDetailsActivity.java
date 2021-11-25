package com.example.git.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.git.R;
import com.example.git.adapters.ImageSliderAdapter;
import com.example.git.databinding.ActivityMovieDetailsBinding;
import com.example.git.models.MovieShow;
import com.example.git.responses.MovieDetailsResponse;
import com.example.git.viewmodels.MovieDetailsViewModel;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding activityMovieDetailsBinding;
    private MovieDetailsViewModel movieDetailsViewModel;
    private MovieShow movieShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        doIntialization();
    }

    private void doIntialization() {
        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        activityMovieDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        movieShow = (MovieShow) getIntent().getSerializableExtra("movieShow");
        getMovieDetails();
    }

    private void getMovieDetails() {
        activityMovieDetailsBinding.setIsLoading(true);
        String movieId = String.valueOf(movieShow.getId());
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
                        activityMovieDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                movieDetailsResponse.getMovieDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityMovieDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityMovieDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityMovieDetailsBinding.textReadMore.setOnClickListener(view->{
                                if (activityMovieDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                    activityMovieDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                    activityMovieDetailsBinding.textDescription.setEllipsize(null);
                                    activityMovieDetailsBinding.textReadMore.setText(R.string.read_less);
                                } else {
                                    activityMovieDetailsBinding.textDescription.setMaxLines(4);
                                    activityMovieDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                    activityMovieDetailsBinding.textReadMore.setText(R.string.read_more);
                                }
                        });
                        activityMovieDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(movieDetailsResponse.getMovieDetails().getRating())
                                )
                        );
                        if (movieDetailsResponse.getMovieDetails().getGenres() != null){
                            activityMovieDetailsBinding.setGenre(movieDetailsResponse.getMovieDetails().getGenres()[0]);
                        }else{
                            activityMovieDetailsBinding.setGenre("N/A");
                            }
                            activityMovieDetailsBinding.setRuntime(movieDetailsResponse.getMovieDetails().getRuntime() +"Min");
                            activityMovieDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                            activityMovieDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                            activityMovieDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                            activityMovieDetailsBinding.imagewatchlist.setOnClickListener(view ->
                                    new CompositeDisposable().add(movieDetailsViewModel.addToWatchlist(movieShow)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(()->{
                                                activityMovieDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_added);
                                                Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                            })
                                    ));
                            activityMovieDetailsBinding.imagewatchlist.setVisibility(View.VISIBLE);
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
        activityMovieDetailsBinding.setMovieName(movieShow.getName());
        activityMovieDetailsBinding.setNetworkCountry(
                movieShow.getNetwork()+ "(" +
                       movieShow.getCountry() + ")"
        );
        activityMovieDetailsBinding.setStatus(movieShow.getStatus());
        activityMovieDetailsBinding.setStartedDate(movieShow.getStartDate() );
        activityMovieDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityMovieDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }
}
