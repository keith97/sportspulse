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

public class GameAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> gamesList;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public GameAdapter(Activity activity, ArrayList<HashMap<String, String>> gamesList) {
        this.activity = activity;
        this.gamesList = gamesList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return gamesList.size();
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
            vi = inflater.inflate(R.layout.game_row, null);
        }

        TextView text=(TextView)vi.findViewById(R.id.game_description);;
        ImageView image=(ImageView)vi.findViewById(R.id.sport_icon);
        String gameInfo = gamesList.get(position).get("game_category") + " - " +
                gamesList.get(position).get("game_description") + "\n" +
                gamesList.get(position).get("game_name") + "\n" +
                gamesList.get(position).get("game_date") + " " +
                gamesList.get(position).get("game_time") + " @" +
                gamesList.get(position).get("game_venue") + "\n" +
                "Ticket: " + gamesList.get(position).get("ticket_status");
        text.setText(gameInfo);
        imageLoader.DisplayImage(gamesList.get(position).get("game_category_icon"), image);

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                Intent intent = new Intent(activity, CurrentGameDetail.class);
                intent.putExtra("game_ID", gamesList.get(position).get("game_ID"));
                intent.putExtra("game_name", gamesList.get(position).get("game_name"));
                intent.putExtra("game_category",gamesList.get(position).get("game_category"));
                intent.putExtra("game_category_icon",gamesList.get(position).get("game_category_icon"));
                intent.putExtra("game_description", gamesList.get(position).get("game_description"));
                intent.putExtra("game_date", gamesList.get(position).get("game_date"));
                intent.putExtra("game_time", gamesList.get(position).get("game_time"));
                intent.putExtra("game_venue", gamesList.get(position).get("game_venue"));
                intent.putExtra("ticket_status", gamesList.get(position).get("ticket_status"));
                intent.putExtra("ticket_price", gamesList.get(position).get("ticket_price"));
                intent.putExtra("health_pulse_discount", gamesList.get(position).get("health_pulse_discount"));

                // Start SingleItemView Class
                activity.startActivity(intent);

            }
        });
        //return vi;
        return vi;
    }
}