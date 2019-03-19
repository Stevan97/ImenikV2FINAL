package com.example.imenikv2final.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imenikv2final.R;
import com.example.imenikv2final.db.model.BrojeviTelefona;

import java.util.List;

public class BrojeviTelefonaAdapter extends BaseAdapter {

    private Context context;
    private List<BrojeviTelefona> brojeviTelefona;

    public BrojeviTelefonaAdapter(Context context, List<BrojeviTelefona> brojeviTelefonas) {
        this.context = context;
        this.brojeviTelefona = brojeviTelefonas;
    }

    private Spannable message1 = null;
    private Spannable message2 = null;

    @Override
    public int getCount() {
        return brojeviTelefona.size();
    }

    @Override
    public Object getItem(int position) {
        return brojeviTelefona.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.brojevi_telefona_adapter, null);

        TextView tip = convertView.findViewById(R.id.brojevi_telefona_adapter_tip);
        message1 = new SpannableString("Tip Broja: ");
        message2 = new SpannableString(brojeviTelefona.get(position).getTipBroja());
        spannableStyle();
        tip.setText(message1);
        tip.append(message2);

        TextView broj = convertView.findViewById(R.id.brojevi_telefona_adapter_broj);
        message1 = new SpannableString(" | Broj: ");
        spannableStyle();
        broj.setText(message1);
        broj.append(String.valueOf(brojeviTelefona.get(position).getBroj()));

        return convertView;
    }

    public void refreshList(List<BrojeviTelefona> brojeviTelefonas) {
        this.brojeviTelefona.clear();
        this.brojeviTelefona.addAll(brojeviTelefonas);
        notifyDataSetChanged();
    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
