package me.modmuss50.csix.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WebHandler {

	public static String weburl = "http://localhost:8000";
	public static Gson GSON = new Gson();

	static HttpClient client = new DefaultHttpClient();

	public static List<CsixItem> listItems() throws IOException {
		HttpGet request = new HttpGet(weburl + "/list");
		HttpResponse response = client.execute(request);

		String responseBody = EntityUtils.toString(response.getEntity());

		System.out.println(responseBody);

		Type listType = new TypeToken<ArrayList<CsixItem>>(){}.getType();
		List<CsixItem> list = new Gson().fromJson(responseBody, listType);

		return list;
	}

	public static CsixItem addItem(ItemStack stack, EntityPlayer player) throws IOException {
		HttpPost post = new HttpPost(weburl + "/addItem");

		CsixItem item = CsixItem.fromStack(stack);

		HttpEntity entity = new ByteArrayEntity(GSON.toJson(item).getBytes("UTF-8"));
		post.setEntity(entity);

		post.setHeader("uuid", player.getUniqueID().toString());
		post.setHeader("username", player.getDisplayNameString());

		HttpResponse response = client.execute(post);

		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);

		CsixItem uploadedItem = GSON.fromJson(responseBody, CsixItem.class);
		return uploadedItem;
	}

	public static RemoveResponse removeItem(String uuid, EntityPlayer player) throws IOException {
		HttpPost post = new HttpPost(weburl + "/removeItem");

		RemoveRequest removeRequest = new RemoveRequest(uuid);

		HttpEntity entity = new ByteArrayEntity(GSON.toJson(removeRequest).getBytes("UTF-8"));
		post.setEntity(entity);

		post.setHeader("uuid", player.getUniqueID().toString());
		post.setHeader("username", player.getDisplayNameString());

		HttpResponse response = client.execute(post);

		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);

		RemoveResponse removeResponse = GSON.fromJson(responseBody, RemoveResponse.class);
		return removeResponse;
	}

	public static int getCoins(EntityPlayer player) throws IOException {
		HttpGet post = new HttpGet(weburl + "/coins");
		post.setHeader("uuid", player.getUniqueID().toString());
		HttpResponse response = client.execute(post);

		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);

		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(responseBody).getAsJsonObject();
		return jsonObject.get("coins").getAsInt();
	}

	public static void main(String[] args) throws IOException {
		listItems();
	}

}
