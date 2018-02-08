package com.wrld.widgets.searchbox.view;


import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.wrld.widgets.R;
import com.wrld.widgets.searchbox.model.IOnSearchListener;
import com.wrld.widgets.searchbox.model.SearchProviderQueryResult;
import com.wrld.widgets.searchbox.model.SearchQuery;
import com.wrld.widgets.searchbox.model.SearchWidgetSearchModel;
import com.wrld.widgets.searchbox.model.SearchWidgetSuggestionModel;

import java.util.List;

public class SearchViewController implements SearchView.OnQueryTextListener, IOnSearchListener {

    private SearchView m_view;
    private SearchWidgetSearchModel m_searchModel;
    private SearchWidgetSuggestionModel m_suggestionModel;

    public SearchViewController(SearchWidgetSearchModel searchModel,
                                SearchWidgetSuggestionModel suggestionModel,
                                SearchView view)
    {
        m_searchModel = searchModel;
        m_suggestionModel = suggestionModel;
        m_searchModel.setSearchListener(this);

        m_view = view;
        m_view.setOnQueryTextListener(this);

        initialiseView();
    }

    private void initialiseView() {
        clearMargins("android:id/search_edit_frame");
        clearMargins("android:id/search_mag_icon");
        clearMargins("android:id/search_plate");

        int searchMicId = m_view.getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView)m_view.findViewById(searchMicId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        ViewGroup.LayoutParams params =  textView.getLayoutParams();

        int textPadding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, m_view.getResources().getDisplayMetrics()));
        params.height = ViewGroup.MarginLayoutParams.MATCH_PARENT;//
        textView.setLayoutParams(params);
        textView.setPadding(0,0,textPadding,0);
        textView.setGravity(Gravity.CENTER_VERTICAL);


        m_view.requestLayout();
    }

    private void clearMargins(String childId) {
        int searchMicId = m_view.getResources().getIdentifier(childId, null, null);
        View view = m_view.findViewById(searchMicId);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        params.setMargins(0,0,0,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginStart(0);
            params.setMarginEnd(0);
        }

        params.height = ViewGroup.MarginLayoutParams.MATCH_PARENT;
        view.setPadding(0,0,0,0);
        view.setLayoutParams(params);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        m_view.clearFocus();
        if(!TextUtils.isEmpty(s)) {
            m_searchModel.doSearch(s);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(!s.isEmpty())
        {
            m_suggestionModel.doSuggestions(s);
        }
        else
        {
            m_suggestionModel.clear();
        }
        return false;
    }

    @Override
    public void onSearchQueryStarted(SearchQuery query) {
        m_view.setQuery(query.getQueryString(), false);
    }

    @Override
    public void onSearchQueryCompleted(SearchQuery query, List<SearchProviderQueryResult> results) {

    }

    @Override
    public void onSearchQueryCancelled(SearchQuery query) {

    }
}