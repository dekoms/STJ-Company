package com.example.merge;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarState {

    private LinearLayout cellViewGroup;

    public CalendarState(LinearLayout cellViewGroup) {
        this.cellViewGroup = cellViewGroup;
    }

    public void create() {
        TextView dynamicTextView = new TextView(cellViewGroup.getContext());

        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.GREEN);
        dynamicTextView.setBackground(shapeDrawable);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                cellViewGroup.getResources().getDisplayMetrics()
        );
        int marginLeft = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                11,
                cellViewGroup.getResources().getDisplayMetrics()
        );
        layoutParams.topMargin = marginTop;
        layoutParams.leftMargin = marginLeft;
        dynamicTextView.setLayoutParams(layoutParams);

        dynamicTextView.setText("     ");

        cellViewGroup.addView(dynamicTextView);
    }
}
