package com.sp.pulse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthProgrammeDetail extends Activity {
    // Declare Variables
    private String program_name;
    private String program_description;
    private String program_venue;
    private String age_group;
    private String start_date;
    private String end_date;
    private String start_time;
    private String end_time;
    private String health_pulse_reward;
    private String program_remarks;
    private String program_category_icon;
    private String position;
    private ImageLoader imageLoader = new ImageLoader(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from health_programme_detail.xmletail.xml
        setContentView(R.layout.health_programme_detail);

        Intent i = getIntent();
        // Get the result
        program_name = i.getStringExtra("program_name");
        program_description = i.getStringExtra("program_description");
        program_venue = i.getStringExtra("program_venue");
        age_group = i.getStringExtra("age_group");
        start_date = i.getStringExtra("start_date");
        end_date = i.getStringExtra("end_date");
        start_time = i.getStringExtra("start_time");
        end_time = i.getStringExtra("end_time");
        health_pulse_reward = i.getStringExtra("health_pulse_reward");
        program_remarks = i.getStringExtra("remarks");
        program_category_icon = i.getStringExtra("program_category_icon");

        // Locate the TextViews in health_programme_detail.xmletail.xml
        TextView programName = (TextView) findViewById(R.id.program_name);
        TextView programDescription = (TextView) findViewById(R.id.program_description);
        TextView programVenue = (TextView) findViewById(R.id.program_venue);
        TextView ageGroup = (TextView) findViewById(R.id.age_group);
        TextView startDate = (TextView) findViewById(R.id.start_date);
        TextView endDate = (TextView) findViewById(R.id.end_date);
        TextView startTime = (TextView) findViewById(R.id.time_start);
        TextView endTime = (TextView) findViewById(R.id.time_end);
        TextView healthPulseReward = (TextView) findViewById(R.id.health_pulse_rewarded);
        TextView remarks = (TextView) findViewById(R.id.remarks);

        // Locate the ImageView in health_programme_detail.xmletail.xml
        ImageView programCategory = (ImageView) findViewById(R.id.program_category);

        // Set results to the TextViews
        programName.setText(program_name);
        programDescription.setText(program_description);
        programVenue.setText(program_venue);
        ageGroup.setText(age_group);
        startDate.setText(start_date);
        endDate.setText(end_date);
        startTime.setText(start_time);
        endTime.setText(end_time);
        healthPulseReward.setText(health_pulse_reward);
        remarks.setText(program_remarks);

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(program_category_icon, programCategory);
    }
}