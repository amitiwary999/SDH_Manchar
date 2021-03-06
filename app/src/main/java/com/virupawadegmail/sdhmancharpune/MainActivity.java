package com.virupawadegmail.sdhmancharpune;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList=(RecyclerView)findViewById(R.id.mblog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null) {
            FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                    Blog.class,
                    R.layout.blog_row,
                    BlogViewHolder.class,
                    mDatabase

            ) {
                @Override
                protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                    //  model=new Blog();
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getApplicationContext(), model.getImage());


                }
            };
            mBlogList.setAdapter(firebaseRecyclerAdapter);
        }else{
            loadLoginView();
        }
    }

    public  static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
             mView=itemView;
        }


        public void setTitle(String title){
            TextView post_tile=(TextView)mView.findViewById(R.id.post_title);
            post_tile.setText(title);
        }
        public   void  setDesc(String desc){
            TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }
        public  void setImage( Context ctx,String image){

            ImageView post_image =(ImageView)mView.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).into(post_image);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
        finish();
    }

}
