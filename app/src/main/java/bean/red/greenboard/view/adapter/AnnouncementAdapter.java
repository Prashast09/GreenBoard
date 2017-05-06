package bean.red.greenboard.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bean.red.greenboard.R;
import bean.red.greenboard.model.Announcement;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shivam Seth on 4/27/2016.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private static List<Announcement> announcements;
    private Activity context;
    Integer[] image= { R.drawable.events,R.drawable.favours, R.drawable.sports, R.drawable.offers, R.drawable.academics, R.drawable.general,R.drawable.desk};

    public AnnouncementAdapter (List<Announcement> announcements , Activity context)
    {
        this.announcements = announcements;
        this.context = context;

    }
    public void updateList(List<Announcement> data){
        announcements = data;
        this.notifyDataSetChanged();
    }

    @Override
    public AnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent, false);
        AnnouncementViewHolder avh = new AnnouncementViewHolder(v);

        return avh;
    }

    @Override
    public void onBindViewHolder(final AnnouncementViewHolder holder, final int position) {
        holder.name.setText(announcements.get(position).name);
        holder.message.setText(announcements.get(position).message);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime= Calendar.getInstance().getTime();
        Date receivedTime = new Date();
        try {
            receivedTime = df.parse(announcements.get(position).time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = currentTime.getTime()- receivedTime.getTime();
        difference/=1000;
        if(difference<60)
        {
            holder.time.setText("just now");
        }
        else if (difference>=60 && difference <3600)
        {
            long temp;
            temp= difference/60;
            holder.time.setText(temp +"m");
        }
        else if (difference <(3600*24)){
            long temp;
            temp= difference/(60*60);
            holder.time.setText(temp +"h");
        }
        else {
            long temp;
            temp = difference/(60*60*24);
            holder.time.setText(temp+"d");
        }

        //  holder.time.setText(announcements.get(position).time);
        if (announcements.get(position).category.equals("Events")){
            holder.category.setImageResource(image[0]);
        }
        else  if (announcements.get(position).category.equals("Favours")){
            holder.category.setImageResource(image[1]);
        }

        else  if (announcements.get(position).category.equals("Sports")){
            holder.category.setImageResource(image[2]);
        }
        else  if (announcements.get(position).category.equals("Offers")){
            holder.category.setImageResource(image[3]);
        }
        else  if (announcements.get(position).category.equals("Academics")){
            holder.category.setImageResource(image[4]);
        }
        else  if (announcements.get(position).category.equals("General")){
            holder.category.setImageResource(image[5]);
        }
        else  if (announcements.get(position).category.equals("DirectorsDesk")){
            holder.category.setImageResource(image[6]);
        }




        if (announcements.get(position).login_provider.equals("Facebook"))
        {

            Glide.with(context).load("http://graph.facebook.com/v2.6/"+announcements.get(position).login_id+"/picture?height=256").into(holder.profilepic);
        }
        else if (announcements.get(position).login_provider.equals("Promotion"))
        {
            Glide.with(context).load("http://www.getfavours.in/greenboard/"+announcements.get(position).login_id+".jpg").into(holder.profilepic);
        }
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((announcements.get(position).contact_no).equals("")){
                    final   Snackbar snackbar = Snackbar
                            .make(context.findViewById(R.id.coordinatorLayout), "Number not provided", Snackbar.LENGTH_LONG)
                            .setAction("Email", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                    String x[] = new String[1];
                                    x[0]=announcements.get(position).email;
                                    intent.putExtra(Intent.EXTRA_EMAIL, x);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(intent);
                                    }
                                } });
                    snackbar.show();


                }
                else{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + announcements.get(position).contact_no));
                    context.startActivity(intent);
                }
            }
        });
        holder.text_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((announcements.get(position).contact_no).equals("")){
                    final   Snackbar snackbar = Snackbar
                            .make(context.findViewById(R.id.coordinatorLayout), "Number not provided", Snackbar.LENGTH_LONG)
                            .setAction("Email", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                    String x[] = new String[1];
                                    x[0]=announcements.get(position).email;
                                    intent.putExtra(Intent.EXTRA_EMAIL, x);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(intent);
                                    }
                                } });
                    snackbar.show();


                }
                else{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + announcements.get(position).contact_no));
                    context.startActivity(intent);
                }}
        });
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((announcements.get(position).email).equals("")) {
                    Snackbar snackbar = Snackbar
                            .make(context.findViewById(R.id.coordinatorLayout), "Email not provided", Snackbar.LENGTH_LONG)
                            .setAction("Call", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + announcements.get(position).contact_no));
                                    context.startActivity(intent);
                                }
                            });
                    snackbar.show();

                }
                else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    String x[] = new String[1];
                    x[0] = announcements.get(position).email;
                    intent.putExtra(Intent.EXTRA_EMAIL, x);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);

                    }
                }
            }});
        holder.text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((announcements.get(position).email).equals("")) {
                    Snackbar snackbar = Snackbar
                            .make(context.findViewById(R.id.coordinatorLayout), "Email not provided", Snackbar.LENGTH_LONG)
                            .setAction("Call", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + announcements.get(position).contact_no));
                                    context.startActivity(intent);
                                }
                            });
                    snackbar.show();

                }
                else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    String x[] = new String[1];
                    x[0] = announcements.get(position).email;
                    intent.putExtra(Intent.EXTRA_EMAIL, x);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);

                    }
                }
            }});


    }

    @Override
    public int getItemCount() {

        return this.announcements.size();
    }
    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView name;
        TextView time;
        TextView message;
        CircleImageView profilepic;
        ImageView call;
        ImageView email;
        View v;
        TextView text_call;
        TextView text_email;
        CircleImageView category;

        AnnouncementViewHolder (View itemView) {
            super(itemView);
            v= itemView;
            cv = (CardView)itemView.findViewById(R.id.card_view);
            name = (TextView)itemView.findViewById(R.id.text_name);
            profilepic = (CircleImageView)itemView.findViewById(R.id.profile_image);
            time = (TextView)itemView.findViewById(R.id.text_time);
            message = (TextView)itemView.findViewById(R.id.text_message);
            call = (ImageView)itemView.findViewById(R.id.ic_call);
            email = (ImageView)itemView.findViewById(R.id.ic_email);
            text_call = (TextView)itemView.findViewById(R.id.text_call);
            text_email = (TextView)itemView.findViewById(R.id.tv_email);
            category = (CircleImageView) itemView.findViewById(R.id.category);

        }
        @Override
        public void onClick(View v) {


        }

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}