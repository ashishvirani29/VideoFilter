package com.veeradeveloper.videofilter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FilterAdapter extends BaseAdapter {
    Bitmap bm;
    Context context;
    Integer[] data1;
    int height;
    int width;

    class ViewHolder {
        ImageView glitchImage;
        ImageView thumbnailImg;

        ViewHolder() {
        }
    }

    public FilterAdapter(Activity context, Integer[] effectsArr, int height, int width, Bitmap thumb) {
        this.context = context;
        this.data1 = effectsArr;
        this.height = height;
        this.width = width;
        this.bm = thumb;
    }

    public int getCount() {
        return this.data1.length;
    }

    public Object getItem(int position) {
        return this.data1[position];
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convert_view, ViewGroup parent) {
        ViewHolder holder;
        if (convert_view == null) {
            convert_view = ((Activity) this.context).getLayoutInflater().inflate(R.layout.filter_single_view, parent, false);
            holder = new ViewHolder();
            holder.glitchImage = (ImageView) convert_view.findViewById(R.id.glitch);
            holder.thumbnailImg = (ImageView) convert_view.findViewById(R.id.thumbnail_view);
            holder.thumbnailImg.setVisibility(0);
            convert_view.setTag(holder);
        } else {
            holder = (ViewHolder) convert_view.getTag();
        }
        holder.thumbnailImg.setImageBitmap(this.bm);
        holder.glitchImage.getLayoutParams().height = (int) (((double) this.width) * 0.2d);
        holder.glitchImage.getLayoutParams().width = (int) (((double) this.width) * 0.2d);
        holder.thumbnailImg.getLayoutParams().height = (int) (((double) this.width) * 0.2d);
        holder.thumbnailImg.getLayoutParams().width = (int) (((double) this.width) * 0.2d);

        Glide.with(this.context)
                .load(this.data1[position].intValue())
                .error((int) R.color.white)
                .placeholder((int) R.color.white)
                .into(holder.glitchImage);

        if (ConstantFlag.selectedEffect == position) {
            holder.thumbnailImg.setBackgroundColor(this.context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.thumbnailImg.setBackgroundColor(0);
        }
        return convert_view;
    }
}
