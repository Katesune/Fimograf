package com.filmograf.old_filmograf;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.filmograf.old_filmograf.R;

public interface MovieManipulative {
    public default int getMovieId(View v) {
        LinearLayout buttons_layout = (LinearLayout) v.getParent();
        TextView id_text_view = buttons_layout.findViewById(R.id.movie_id);

        String str_id = id_text_view.getText().toString();
        return Integer.parseInt(str_id);
    }

    public default int[] getUserId(UsersDB users_db, String email) {
        final int[] result_id = {0};
        new Thread() {
            @Override
            public void run() {
                result_id[0] = users_db.user_manager().findByMail(email);
            }
        }.start();

        return result_id;
    }

}
