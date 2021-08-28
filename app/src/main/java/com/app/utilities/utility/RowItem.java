package com.app.utilities.utility;

public class RowItem {

    private final String rowTextView;
    private final int rowImageViewId;

    public RowItem(String rowTextView, int rowImageViewId) {
        this.rowTextView = rowTextView;
        this.rowImageViewId = rowImageViewId;
    }

    public String getRowTextView() {
        return rowTextView;
    }

    public int getRowImageViewId() {
        return rowImageViewId;
    }

}
