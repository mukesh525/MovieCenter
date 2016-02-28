package muk.materialdesign.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import muk.materialdesign.Model.Information;
import muk.materialdesign.R;

/**
 * Created by Mukesh on 1/1/2015.
 */
public class RecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  LayoutInflater inflator;
    List<Information> data= Collections.emptyList();
    public static final int TYPE_HEADER=0;
    public static final int TYPE_ITEM=1;
     private Context context;
    private final ArrayList<Integer> selected=new ArrayList<Integer>();
    private ClickedListner clickedListner;


   public RecylerAdapter(Context context,List<Information> data)
    {
        inflator=LayoutInflater.from(context);
        this.data=data;
        this.context=context;

    }
     public void setClickedListner(ClickedListner clickedListner)
     {
          this.clickedListner=clickedListner;
     }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i==TYPE_HEADER) {
            View view = inflator.inflate(R.layout.drawer_header, viewGroup, false);
            HeaderHolder holder = new HeaderHolder(view);
            return holder;
        }
        else
        {
            View view = inflator.inflate(R.layout.custom_row, viewGroup, false);
            ItemHolder holder = new ItemHolder(view);
            selected.add(0);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
       /* return (position == 0 ? TYPE_HEADER:TYPE_ITEM);*/
        if(position==0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof  HeaderHolder)
        {

        }
        else
        {

            ItemHolder holder = (ItemHolder) viewHolder;
            Information current=data.get(i-1);
           // holder.current=data.get(i);
            holder.title.setText(current.title);
            holder.icon.setImageResource(current.itemid);
        }

       /* if(!selected.contains(i))
        {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            viewHolder.itemView.setBackgroundColor(Color.CYAN);
        }*/

    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    public void delete(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }
    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listicon);
            // icon.setOnClickListener(this);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v)
        {
            //  v.setBackgroundColor(Color.CYAN);
            if(selected.isEmpty())
            {
                selected.add(getPosition());
            }
            else
            {
                int oldSelected=selected.get(0);
                selected.clear();
                selected.add(getPosition());
                notifyItemChanged(oldSelected);
            }
            //delete(getPosition());
            if(clickedListner!=null)
            {
                clickedListner.OnItemClick(v,getPosition());
            }
        }


    }
        class HeaderHolder extends RecyclerView.ViewHolder {


            public HeaderHolder(View itemView) {
                super(itemView);

           
                
            }
        }




      public  interface ClickedListner
    {
        public void OnItemClick(View view,int position);
    }


}
