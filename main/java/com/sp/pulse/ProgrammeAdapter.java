package com.sp.pulse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgrammeAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> programsList;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public ProgrammeAdapter(Activity activity, ArrayList<HashMap<String, String>> programsList) {
        this.activity = activity;
        this.programsList = programsList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return programsList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.program_row, null);
        }

        TextView text=(TextView)vi.findViewById(R.id.program_description);;
        ImageView image=(ImageView)vi.findViewById(R.id.program_icon);
        String programInfo = programsList.get(position).get("program_category") + " - " +
                programsList.get(position).get("program_description") + "\n" +
                programsList.get(position).get("program_name") + "\n" +
                programsList.get(position).get("start_date") + " - " + programsList.get(position).get("end_date") + "\n" +
                programsList.get(position).get("start_time") + " - " + programsList.get(position).get("end_time") + "\n" +
                "@" + programsList.get(position).get("program_venue");
        text.setText(programInfo);
        imageLoader.DisplayImage(programsList.get(position).get("program_category_icon"), image);

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                Intent intent = new Intent(activity, HealthProgrammeDetail.class);
                intent.putExtra("health_program_ID", programsList.get(position).get("health_program_ID"));
                intent.putExtra("program_name", programsList.get(position).get("program_name"));
                intent.putExtra("program_category",programsList.get(position).get("program_category"));
                intent.putExtra("program_category_icon",programsList.get(position).get("program_category_icon"));
                intent.putExtra("program_description", programsList.get(position).get("program_description"));
                intent.putExtra("program_venue", programsList.get(position).get("program_venue"));
                intent.putExtra("start_date", programsList.get(position).get("start_date"));
                intent.putExtra("end_date", programsList.get(position).get("end_date"));
                intent.putExtra("start_time", programsList.get(position).get("start_time"));
                intent.putExtra("end_time", programsList.get(position).get("end_time"));
                intent.putExtra("age_group", programsList.get(position).get("age_group"));
                intent.putExtra("health_pulse_reward", programsList.get(position).get("health_pulse_reward"));
                intent.putExtra("remarks", programsList.get(position).get("remarks"));

                // Start SingleItemView Class
                activity.startActivity(intent);
            }
        });
        //return vi;
        return vi;
    }
}
