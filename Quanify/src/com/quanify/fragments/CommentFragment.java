package com.quanify.fragments;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.quanify.R;
public class CommentFragment extends Fragment {
	TextView showComment;
	EditText commentBox;
	Button clearBox;
	Button addComment;

	public CommentFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		FrameLayout fl = new FrameLayout(getActivity());
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		params.setMargins(margin, margin, margin, margin);
		fl.setLayoutParams(params);
		fl.setBackgroundColor(0xffa9a9a9);
		inflater.inflate(R.layout.fragment_comments, fl);

		showComment = (TextView) fl.findViewById(R.id.showComment);
		commentBox = (EditText) fl.findViewById(R.id.commentBox);
		clearBox = (Button) fl.findViewById(R.id.clearBox);
		addComment = (Button) fl.findViewById(R.id.addComment);
		clearBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commentBox.setText("");
			}
		});

		addComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showComment.setText((new Date()).toString()+"\n"+commentBox.getText());
				commentBox.setText("");
				
			}
		});
		return fl;
	}

}