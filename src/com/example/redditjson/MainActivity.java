package com.example.redditjson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.redditjson.R;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.HashMap;

public class MainActivity extends ListActivity
{
	public static HashMap<String, String> Links = new HashMap<String, String>();
	public static ArrayList<String> Titles = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
//		this.setTitle("Reddit.com");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new JSONParserTask(MainActivity.this).execute("");
	}

	protected void fill_ListView()
	{
		try
		{
			setListAdapter(new ArrayAdapter(MainActivity.this,
					android.R.layout.simple_list_item_1, this.Titles));

			final ListView lv = (ListView) findViewById(android.R.id.list);
			lv.setLongClickable(true);
			lv.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				public boolean onItemLongClick(AdapterView parent, View view,
						int position, long id)
				{
					Log.i("A1", "Long clicked on a LinkView item");
					String url = Links.get(lv.getItemAtPosition(position)
							.toString());

					// WebView webview = new WebView(MainActivity.this);
					// setContentView(webview);
					// webview.getSettings().setJavaScriptEnabled(true);
					// webview.requestFocus();
					// webview.setVisibility(View.VISIBLE);
					// webview.setWebViewClient(new WebViewClient()
					// {
					// // @Override
					// public boolean shouldOverrideUrlLoading(WebView view,
					// String url)
					// {
					// System.out.println("webview link: " + url);
					// // view.loadUrl(url);
					// return true;
					// }
					// // public void onPageStarted(WebView view, String url,
					// // android.graphics.Bitmap favicon)
					// // {
					// // System.out.println("webview link: " + url);
					// // view.loadUrl(url);
					// // }
					// });
					// setContentView(webview);
					// webview.loadUrl("http://www.google.com");

					Log.i("A1", "Opening URL: " + url);

					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

					return true;
				}
			});
		}
		catch (Exception e)
		{
			Log.e("A1", "ERROR in onCreate(): " + e.toString());
			e.printStackTrace();
		}
	}

	protected void JSONParser()
	{
		String next;
		String json = "";
		String title = "";
		String redditUrl = "";

		try
		{
			Log.i("A1", "Getting Reddit links...");
			URL url = new URL("http://www.reddit.com/.json");
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()));

			while ((next = bufferedReader.readLine()) != null)
			{
				json += next;
			}

			json = '[' + json + ']'; // Correcting JSON formatting from Reddit

			JsonNode root = readJson(json);
			for (JsonNode node : root)
			{
				JsonNode root2 = readJson(node.path("data").path("children")
						.toString());

				for (JsonNode node2 : root2)
				{
					title = node2.path("data").path("title").toString();
					title = title.substring(0, title.length() - 1);
					title = title.substring(1, title.length());
					Titles.add(title);

					redditUrl = node2.path("data").path("permalink").toString();
					redditUrl = redditUrl.substring(0, redditUrl.length() - 1);
					redditUrl = redditUrl.substring(1, redditUrl.length());
					Links.put(title, "http://www.reddit.com" + redditUrl);
				}
			}
		}
		catch (Exception e)
		{
			Log.e("A1", "ERROR in JSONParser(): " + e.toString());
			e.printStackTrace();
		}

		Log.i("A1", "Number of links on reddit.com: " + Titles.size());
	}

	protected static JsonNode readJson(String json) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(json, JsonNode.class);
		JsonParser jp = mapper.getJsonFactory().createJsonParser(json);
		rootNode = mapper.readTree(jp);
		return rootNode;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class JSONParserTask extends AsyncTask<String, Void, String>
	{
		private ProgressDialog mDialog;
		private ListActivity activity;
		private Context context;

		public JSONParserTask(ListActivity activity)
		{
			this.activity = activity;
			context = activity;
			mDialog = new ProgressDialog(context);
		}

		@Override
		protected String doInBackground(String... params)
		{
			Log.i("A1", "AsyncTask launched");
			try
			{
				JSONParser();
				Log.i("A1", "AsyncTask done");
			}
			catch (Exception e)
			{
				Log.e("A1", "ERROR in AsyncTask: " + e.toString());
				e.printStackTrace();
			}
			return "";
		}

		protected void onPreExecute()
		{
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			mDialog.show();
		}

		protected void onPostExecute(String str)
		{
			Log.i("A1", "onPostExecute launched");
			fill_ListView();
			if (mDialog.isShowing())
				mDialog.dismiss();
		}

	}
}
