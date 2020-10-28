package Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bet1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class GuesserAdapter extends RecyclerView.Adapter<GuesserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isFargment;

    private FirebaseUser firebaseUser;

    public GuesserAdapter(Context mContext, List<User> mUsers, boolean isFargment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFargment = isFargment;
    }
    @NonNull
    @Override
    public GuesserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.guesser_item , parent , false);
        return new GuesserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GuesserAdapter.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());
        holder.guess_date.setText(user.getDate());
        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView fullname;
        public TextView guess_date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);

            guess_date = itemView.findViewById(R.id.guess_date);
        }
    }
}
