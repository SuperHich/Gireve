package com.dotit.gireve.ihm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.EVSECriteria;
import com.dotit.gireve.entity.EVSEOperator;
import com.dotit.gireve.entity.EVSEProperty;
import com.dotit.gireve.ihm.views.ICriteriaItemListener;
import com.dotit.gireve.utils.GireveFonts;
import com.dotit.gireve.utils.GireveManager;

import java.util.ArrayList;
import java.util.List;

public class CriteriaAdapter extends ArrayAdapter<EVSECriteria> {

    private static final String TAG = CriteriaAdapter.class.getSimpleName();
    GireveManager mManager;
    Context mContext;
    int layoutId;
    LayoutInflater inflater;
    List<EVSECriteria> items;
    ArrayAdapter<String> adapter_params;
    ArrayAdapter<String> adapter_operators;
    ArrayList<String> list_params;
    ArrayList<String> list_operators;
    ICriteriaItemListener listener;
    View edt_selected;

    public View getSelectedEditText (){
        return edt_selected;
    }

    boolean justClearFocus = false;

    public boolean isJustClearFocus() {
        return justClearFocus;
    }

    public void setJustClearFocus(boolean justClearFocus) {
        this.justClearFocus = justClearFocus;
    }

    public CriteriaAdapter(Context context, int resource,
                           List<EVSECriteria> items, ICriteriaItemListener listener) {
        super(context, resource, items);

        this.mContext = context;
        this.layoutId = resource;
        this.items = items;
        this.listener = listener;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mManager = GireveManager.getInstance(context);
        initLists();
    }

    public List<EVSECriteria> getItems() {
        return items;
    }

    public void remove(int position) {
        this.items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.criteria_item, null);
            holder = new ViewHolder();

            holder.item_root            = (LinearLayout)convertView.findViewById(R.id.item_root);
            holder.spinner_params 		= (Spinner)     convertView.findViewById(R.id.spinner_params);
            holder.spinner_operators 	= (Spinner)     convertView.findViewById(R.id.spinner_operators);
            holder.edt_value         	= (EditText)    convertView.findViewById(R.id.edt_value);
            holder.btn_remove           = (ImageButton) convertView.findViewById(R.id.btn_remove);

            holder.edt_title         	= (EditText)    convertView.findViewById(R.id.edt_title);

            holder.edt_value.setTypeface(GireveFonts.getClanProBold());
            holder.edt_title.setTypeface(GireveFonts.getClanProBold());

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.item_root.setVisibility(position > 0?View.VISIBLE:View.GONE);
        holder.edt_title.setVisibility(position == 0&&getCount()>1?View.VISIBLE:View.GONE);

        final EVSECriteria item = items.get(position);
        holder.spinner_params.setAdapter(adapter_params);
        holder.spinner_params.setSelection(getSelectedPropertyPosition(item.getProperty()));
        holder.spinner_params.setTag(item.getId());
        holder.spinner_params.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int criteriaId = (Integer) parent.getTag();
                setProperty(criteriaId, mManager.getEVSEProperties().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinner_operators.setAdapter(adapter_operators);
        holder.spinner_operators.setSelection(getSelectedOperatorPosition(item.getOperator()));
        holder.spinner_operators.setTag(item.getId());
        holder.spinner_operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int criteriaId = (Integer) parent.getTag();
                setOperator(criteriaId, mManager.getEVSEOperators().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.edt_value.setTag(item.getId());
        holder.edt_value.setText(item.getValue());
        holder.edt_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText et = (EditText) v.findViewById(R.id.edt_value);
                    int criteriaId = (Integer) v.getTag();
                    setValue(criteriaId, et.getText().toString().trim());
                }
                edt_selected = hasFocus? v:null;
            }
        });

        holder.btn_remove.setTag(item.getId());
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // REMOVE CRITERIA LINE
                int criteriaId = (Integer) v.getTag();
                listener.onMinusClicked(criteriaId);
            }
        });

        if (isJustClearFocus()) {
            holder.edt_value.clearFocus();
        }

        if(position == getCount() - 1)
            holder.edt_value.requestFocus();

        return convertView;
    }

    class ViewHolder {
        LinearLayout item_root;
        Spinner spinner_params;
        Spinner spinner_operators;
        EditText edt_value;
        EditText edt_title;
        ImageButton btn_remove;
    }

    private void initLists(){
        list_params = new ArrayList<>();
        for(EVSEProperty param : mManager.getEVSEProperties()){
            list_params.add(param.getNameChar());
        }
        adapter_params = new ArrayAdapter<>(mContext, R.layout.spinner_item, list_params);
        adapter_params.setDropDownViewResource(R.layout.dropdown_spinner_item);

        list_operators = new ArrayList<>();
        for(EVSEOperator operator : mManager.getEVSEOperators()){
            list_operators.add(operator.getName());
        }
        adapter_operators = new ArrayAdapter<>(mContext, R.layout.spinner_item, list_operators);
        adapter_operators.setDropDownViewResource(R.layout.dropdown_spinner_item);
    }

    private int getSelectedPropertyPosition(EVSEProperty property){
        if(property != null) {
            int counter = 0;
            for (EVSEProperty param : mManager.getEVSEProperties()) {
                if (param.getNameChar().equals(property.getNameChar()))
                    return counter;
                counter++;
            }
        }

        return -1;
    }

    private int getSelectedOperatorPosition(EVSEOperator operator){
        if(operator != null) {
            int counter = 0;
            for (EVSEOperator op : mManager.getEVSEOperators()) {
                if (op.getName().equals(operator.getName()))
                    return counter;
                counter++;
            }
        }

        return -1;
    }

    private void setProperty(int id, EVSEProperty prop){
        for (int i = 0; i < getCount(); i++) {
            if(getItem(i).getId() == id){
                getItem(i).setProperty(prop);
            }
        }
    }

    private void setOperator(int id, EVSEOperator operator){
        for (int i = 0; i < getCount(); i++) {
            if(getItem(i).getId() == id){
                getItem(i).setOperator(operator);
            }
        }
    }

    private void setValue(int id, String text){
        for (int i = 0; i < getCount(); i++) {
            if(getItem(i).getId() == id){
                getItem(i).setValue(text);
            }
        }
    }

}
