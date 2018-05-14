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
	private BigDecimal payAmount;
	private BigDecimal incomeAmount;
	private long quantity;
	private String goodsTags = "";
	private String tagsName = "";

	private List<FoodTagList> goodsPackList = new ArrayList<>();

	public String getTagsName() {
		return tagsName;
	}

	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}

//	public List<GoodTagList> goodsPackList = new ArrayList<>();
	public String getGoodsTags() {
		return goodsTags;
	}

	public void setGoodsTags(String goodsTags) {
		this.goodsTags = goodsTags;
	}

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

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}


	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public List<FoodTagList> getGoodsPackList() {
		return goodsPackList;
	}
	public void setGoodPackList(List<FoodTagList> packList) {
		this.goodsPackList.clear();
		this.goodsPackList.addAll(packList);
	}

	public void setGoodsPackList(List<GoodTagList> packList) {
		List<FoodTagList> list = new ArrayList<>();
		for(GoodTagList tag : packList){
			FoodTagList foodTag = new FoodTagList();
			foodTag.setId(tag.id);
			foodTag.setGoodsId(tag.goodsId);
			foodTag.setName(tag.name);
			foodTag.setPackPrice(tag.packPrice);
			foodTag.setPrice(tag.price);
			foodTag.setQuantity(tag.quantity);
			foodTag.setGoodsTags(tag.goodsTags);
			foodTag.setImageUrl(tag.imageUrl);
			foodTag.setPackGroupId(tag.packGroupId);
			foodTag.setSelectId(tag.selectId);
			list.add(foodTag);
		}
		this.goodsPackList = list;
	}

	public  FoodBean getFoodBean(FoodBean foodBean){
		FoodBean bean = new FoodBean();
		bean.setName(foodBean.getName());
		bean.setGoodsId(foodBean.getGoodsId());
		bean.setId(foodBean.getId());
		bean.setQuantity(foodBean.getQuantity());
		bean.setSelectCount(1);
		bean.setIncomeAmount(foodBean.getIncomeAmount());
		bean.setPayAmount(foodBean.getPayAmount());
		bean.setPrice(foodBean.getPrice());
		bean.setType(foodBean.getType());
		bean.setTagsName(foodBean.getTagsName());
		bean.setGoodsTags(foodBean.goodsTags);
		bean.setGoodPackList(foodBean.getGoodsPackList());
		return bean;
	}
}
