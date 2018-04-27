package com.imovie.mogic.car.bean;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.imovie.mogic.car.utils.ViewUtils;
import com.imovie.mogic.home.model.GoodTagList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FoodBean implements Serializable{

	private int id;
	private String name;//名
	private String sale;//销量
	private String isCommand;//是否推荐
	private BigDecimal price;//价格
	private String cut;//打折
	private String type;//类
	private int icon;//图片
	private long selectCount;
	private String imageUrl;//类
	private int goodsId;
	public List<GoodTagList> goodsPackList = new ArrayList<>();

	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public long getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(long selectCount) {
		this.selectCount = selectCount;
	}

	public int getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getIsCommand() {
		return isCommand;
	}

	public void setIsCommand(String isCommand) {
		this.isCommand = isCommand;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public SpannableString getStrPrice(Context context) {
		String priceStr = String.valueOf(getPrice());
		SpannableString spanString = new SpannableString("¥" + priceStr);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(context, 11));
		spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spanString;
	}

	public SpannableString getStrPrice(Context context, BigDecimal price) {
		String priceStr = String.valueOf(price);
		SpannableString spanString = new SpannableString("¥" + priceStr);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(context, 11));
		spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spanString;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCut() {
		return cut;
	}

	public void setCut(String cut) {
		this.cut = cut;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
