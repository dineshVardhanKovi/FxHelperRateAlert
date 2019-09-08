package com.fxHelper.general.objectrepo;

import com.fxHelper.general.common.ActionType;

public class CC_ObjectRepo extends ActionType{
	
	//Login
	public static String MedscapeLogin = "//*[@class='user-links']//a[contains(text(),'Log In')]";
	
	// Header Section
	public static String HeaderSection="//*[@id='headerbox']";
	public static String InlineLanguages="//*[@class='header-edition']";
	public static String Welcomenote="//*[@class='user-links']";
	public static String Pillars="//*[@class='header-tabs']";
	
	// Footer Section
	public static String FooterSection="//*[@class='page-footer']";
	public static String Medscapelogo="//*[@id='footercontents']//img[@class='footer-logo-image']";
	public static String FindUsOn="//*[@class='footer-social']";
	public static String CopyRights="//*[@class='footer_legal-text resp-container']";
	public static String FooterLinks="//*[@class='footer-sections']";
	
	// Ads
	public static String TopAd="//*[@id='ads-pos-101']//iframe";
	public static String BottomAd="//*[@id='ads-pos-141']//iframe";
	public static String RightAd="//*[@id='ads-pos-122']//iframe";
	public static String RightAds="//*[@id='ads-pos-910']//iframe";
	
	// Feature Section
	public static String FeatureTitle="//*[@id='titleblock']/h1";
	public static String FeatureInfo="//*[@class='cc-feat-info']";
	public static String FeatureDate="//*[@class='cc-date']";
	public static String FeatureLocation="//*[@class='cc-location']";
	public static String FeatureThumbail="//*[@id='collectionbody']/div[2]/ul/li/a/img";
	public static String FeatureContent="//*[@id='collectionbody']/div[5]/table/tbody[2]/tr/td[1]";
	public static String FeatureContent1="//*[@id='collectionbody']/div[2]/p";
	public static String FeatureThumbail2="//*[@id='collectionbody']/div[5]/table/tbody[2]/tr/td[2]/img";
	public static String FeatureThumbail3="//*[@id='collectionbody']/div[2]/p/a[1]/img";
	
	// Conference Section
	public static String ConferenceLabel="//*[@id='collectionbody']/div[3]/div/h3";
	public static String ConferenceContent="//*[@id='collectionbody']/div[3]/ul/ul";
	public static String ConferenceLabel1="//*[@id='collectionbody']/div[6]/div/h3";
	public static String ConferenceLabel2="//*[@id='collectionbody']/div[5]/div/h3";
	public static String ConferenceContent1="//*[@id='collectionbody']/div[6]/ul/li";
	public static String ConferenceContent2="//*[@id='collectionbody']/div[3]/ul/li/a";
	public static String ConferenceContent3="//*[@id='collectionbody']/div/p";
	public static String NewsLabel="//*[@id='collectionbody']/div[2]/div/h3";
	public static String NewsContent="//*[@id='collectionbody']/div[2]/ul/li";
	// Popular News Section
	public static String PopularNewsLabel="//*[@id='collectionbody']//h3[contains(text(),'Popular Optometry News')]";
	public static String PopularNewsContent="//*[@id='collectionbody']/div[4]/ul/li/span";
	public static String PerspectiveLabel="//*[@id='collectionbody']/div[3]/div/h3";
	public static String PerspectiveContent="//*[@id='collectionbody']/div/p";
	// Right Bucket
	public static String RightBucketLabel="//*[@class='gencontentrighttitle']";
	public static String RightBucketContent="//*[@class='bucketContent']";
	public static String RightBucketContent1="//*[@id='gencontentrighttop']/p";
	
	
}
