package com.baikaleg.v3.cookingaid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.baikaleg.v3.cookingaid.R;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private Drawable background;
    private Drawable deleteIcon;
    private int iconMargin;
    private int backgroundColor;

    protected SwipeToDeleteCallback(Context context) {
        super(0, ItemTouchHelper.LEFT);
        background = new ColorDrawable();
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.btn_delete);
        deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        iconMargin = (int) context.getResources().getDimension(R.dimen.hor_margin);
        backgroundColor = context.getResources().getColor(R.color.colorAccent);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view = viewHolder.itemView;
        int itemHeight = view.getBottom() - view.getTop();

        ((ColorDrawable) background).setColor(backgroundColor);
        background.setBounds(view.getRight() + (int) dX,
                view.getTop(),
                view.getRight(),
                view.getBottom());
        background.draw(c);


        int iconTop = view.getTop() + iconMargin;
        int iconLeft = view.getRight() - (itemHeight - 2 * iconMargin) - iconMargin;
        int iconRight = view.getRight() - iconMargin;
        int iconBottom = view.getBottom() - iconMargin;

        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        deleteIcon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
