package com.wrld.searchproviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wrld.widgets.searchbox.model.ISearchResult;
import com.wrld.widgets.searchbox.view.ISearchResultViewFactory;
import com.wrld.widgets.searchbox.view.ISearchResultViewHolder;

import java.util.HashMap;

public class YelpSearchResultViewFactory implements ISearchResultViewFactory {

    private int m_layoutId;
    private String m_reviewText;
    private Context m_context;

    private HashMap<Double, Integer> m_starCache;

    public YelpSearchResultViewFactory(Context context){
        m_context = context;
        m_layoutId = R.layout.yelp_search_result;
        m_reviewText = context.getString(R.string.yelp_review_text);

        m_starCache = new HashMap<Double, Integer>();

        cacheRatingStarImages();
    }

    private void cacheRatingStarImages(){
        m_starCache.put(0.0, R.drawable.stars_small_0);
        m_starCache.put(1.0, R.drawable.stars_small_1);
        m_starCache.put(1.5, R.drawable.stars_small_1_half);
        m_starCache.put(2.0, R.drawable.stars_small_2);
        m_starCache.put(2.5, R.drawable.stars_small_2_half);
        m_starCache.put(3.0, R.drawable.stars_small_3);
        m_starCache.put(3.5, R.drawable.stars_small_3_half);
        m_starCache.put(4.0, R.drawable.stars_small_4);
        m_starCache.put(4.5, R.drawable.stars_small_4_half);
        m_starCache.put(5.0, R.drawable.stars_small_5);
    }

    @Override
    public View makeSearchResultView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(m_layoutId, parent, false);
    }

    private class SearchResultViewHolderImpl implements ISearchResultViewHolder {
        private TextView m_title;
        private TextView m_subtitle;
        private ImageButton m_rating;
        private ImageButton m_yelpLogo;
        private TextView m_reviewCount;
        private String m_url;

        public SearchResultViewHolderImpl(){}

        public void initialise(View view) {
            m_title = (TextView) view.findViewById(R.id.search_result_title);
            m_subtitle = (TextView) view.findViewById(R.id.search_result_description);
            m_rating = (ImageButton) view.findViewById(R.id.yelp_rating);
            m_yelpLogo = (ImageButton) view.findViewById(R.id.yelp_logo);
            m_reviewCount = (TextView) view.findViewById(R.id.yelp_reviews);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink();
                }
            };

            m_yelpLogo.setOnClickListener(onClickListener);
            m_rating.setOnClickListener(onClickListener);
        }

        public void populate(ISearchResult result) {
            m_title.setText(result.getTitle());
            if(result.hasProperty(YelpSearchResult.AddressKey)) {
                m_subtitle.setText((String)result.getProperty(YelpSearchResult.AddressKey).getValue());
            }

            if(result.hasProperty(YelpSearchResult.RatingKey)) {
                m_rating.setImageResource(m_starCache.get(result.getProperty(YelpSearchResult.RatingKey).getValue()));
            }

            if(result.hasProperty(YelpSearchResult.BusinessLink)) {
                m_url = (String) result.getProperty(YelpSearchResult.BusinessLink).getValue();
            }

            if(result.hasProperty(YelpSearchResult.ReviewCountKey)) {
                String reviewCountText = String.format(m_reviewText, result.getProperty(YelpSearchResult.ReviewCountKey).getValue());
                m_reviewCount.setText(reviewCountText);
            }
        }

        private void openLink(){
            if(!TextUtils.isEmpty(m_url)) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(m_url));
                m_context.startActivity(browse);
            }
        }
    }

    @Override
    public ISearchResultViewHolder makeSearchResultViewHolder() {
        return new YelpSearchResultViewFactory.SearchResultViewHolderImpl();
    }
}
