package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.bluetooth.Activity.ShareItem;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;

/**
 * Created by Soumya on 4/18/2015.
 */
//CustomAdapter to display item details on the list view
public class ItemsRowAdapter extends ArrayAdapter<Item> {
    private final List<Item> items;
    CheckBox checkBox;
    static HashSet<Integer> toDeleteSet;
    Button shareItem;

    public ItemsRowAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);

        toDeleteSet = new HashSet<Integer>();
        System.out.println("items recieved count : "+items.size());
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.item_row, null);
        TextView prodName = (TextView) row.findViewById(R.id.textViewProdName);
        TextView itemCount = (TextView) row.findViewById(R.id.textViewItemCount);
        TextView brand = (TextView) row.findViewById(R.id.textViewBrandName);
        TextView category = (TextView) row.findViewById(R.id.textViewCategory);
        shareItem = (Button)row.findViewById(R.id.btmItemShare);
        checkBox = (CheckBox)row.findViewById(R.id.chkProdId);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Item i = items.get(pos);
                if(isChecked){

                    toDeleteSet.add(i.getProductId());
                } else
                {

                    toDeleteSet.remove(i.getProductId());
                }
            }
        });

        prodName.setText(items.get(position).getProductName()==null?"":items.get(position).getProductName().toString());
        itemCount.setText(items.get(position).getProductId()==null?"":items.get(position).getItemCount().toString());
        brand.setText(items.get(position).getBrandName()==null?"":items.get(position).getBrandName().toString());
        category.setText(items.get(position).getCategory()==null?"":items.get(position).getCategory().toString());
        Button editItemBtn = (Button)row.findViewById(R.id.btnItemEdit);
        ImageView favProd = (ImageView) row.findViewById(R.id.imageViewFavorite);
        if(!items.get(position).isFavorite()) {
            favProd.setVisibility(View.INVISIBLE);
        }
        final int prodId = Integer.parseInt(items.get(position).getProductId().toString());
        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),UpdateItem.class);
                SearchList.currentItem = items.get(pos);
                v.getContext().startActivity(myIntent);
            }
        });

        shareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bluetoothIntent = new Intent(v.getContext(),ShareItem.class);
                SearchList.currentItem = items.get(pos);
                v.getContext().startActivity(bluetoothIntent);
            }
        });
        return row;
    }


}