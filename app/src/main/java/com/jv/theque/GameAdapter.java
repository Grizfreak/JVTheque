package com.jv.theque;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder>{
    List<Game> gameList;

    GameAdapter(List<Game> gameList){
        this.gameList=gameList;
    }
    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.display(gameList.get(position));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder{
        private TextView gameName;
        private TextView gamePlat;

        GameViewHolder(View itemView){
            super(itemView);

            gameName= (TextView)itemView.findViewById(R.id.name);
            gamePlat= (TextView)itemView.findViewById(R.id.platform);

        }

        void display(Game game){
            gameName.setText(game.name);
            if(game.platforms !=null){
                gamePlat.setText(game.platforms[0].platform.name);
            }
        }
    }
}

