package com.wrld.widgets.searchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wrld.widgets.R;

public class DefaultSuggestionViewFactory implements SearchResultViewFactory {

    private int m_layoutId;

    public DefaultSuggestionViewFactory(int layoutId) {
        m_layoutId = layoutId;
    }

    @Override
    public View makeSearchResultView(LayoutInflater inflater, SearchResult searchResult, ViewGroup parent) {
        return inflater.inflate(m_layoutId, parent, false);
    }

    private class SearchResultViewHolder implements com.wrld.widgets.searchbox.SearchResultViewHolder {
        private TextView m_title;

        public SearchResultViewHolder(){}

        public void initialise(View view){
            m_title = (TextView) view.findViewById(R.id.search_result_title);
        }

        public void populate(SearchResult result){
            m_title.setText(result.getTitle());
        }
    }

    @Override
    public com.wrld.widgets.searchbox.SearchResultViewHolder makeSearchResultViewHolder() {
        return new SearchResultViewHolder();
    }

}
