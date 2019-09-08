package com.fxHelper.general.objectrepo;

import com.fxHelper.general.common.ActionType;

public class ObjectsRepo extends ActionType {

	// Header objects information
	public static String medscapeHeaderXapth = "//*[@id='headerbox']";
	public static String medscapeLanguageEditionXapthDesktop = "//div[@class='header-edition']";
	public static String medscapeLanguageEditionXapthMobile = "//div[@class='mobile-edition-menu']";
	public static String medscapeFooter = "//*[@id='footercontents']//div[@class='page-footer']";
	public static String hamburgermenu = "//label[@class='mobile-nav-toggle-label']";

	// Article title area
	public static String newsArticlefeaturebar = "//div[@class='toolbar js-toolbar']";
	public static String newsArticleToolBarMobile = "//li[@class='share']";
	public static String newsArticleToolBarMobileshareLayerClose = "//li[@class='social']//li[@class='close-layer']";
	public static String newsArticleTitle = "//div[@class='title-area']//h1[@class='title']";
	public static String newsArticleToolBarFB = "//ul/li[@class='facebook']";
	public static String newsArticleToolBarTwitter = "//ul/li[@class='twitter']";
	public static String newsArticleToolBarLinkedIn = "//ul/li[@class='linkedin']";
	public static String newsArticleToolBarGooglePluse = "//ul/li[@class='googleplus']";
	public static String newsArticleToolBarEmail = "//li[@class='email']";
	public static String newsArticleToolBarPrint = "//li[@class='print']";
	public static String newsArticleToolBarAddEmailAlert = "//li[starts-with(@class,'add-email-alerts')]";
	public static String newsArticleToolBarRemoveEmailAlert = "//*[text()='Erectile Dysfunction']/..//a[text()='Added']";
	public static String newsArticleToolBarAddEmailAlertToolTip = "//li[@class='alert-tooltip']//span[@class='alert-desc']";
	public static String addEmailAlertToolTipSuccessText = "//*[@id='topicAdded'][contains(text(),'successfully added')]";
	public static String addEmailAlertToolTipSuccessLaertTopic = ".//*[@id='topicAdded']/span[@class='patopic']";
	public static String addEmailAlertToolTipManageLink = "//*[@id='palertadexbox']/a[@class='manageLink']";
	public static String addEmailAlertpopupLayerClose = "//*[@id='palertadexbox']//input[@value='Close']";
	public static String newsArticleToolBarCommentstext = "//div[@class='toolbar js-toolbar']//*[starts-with(@class,'comment-text')]";
	public static String newsArticleToolBarCommentscount = "//div[@class='toolbar js-toolbar']//*[starts-with(@class,'comment-number')]";
	public static String newsArticleToolBarCommentsTextBottom = "//div[@class='info-layers']//*[starts-with(@class,'comment-text')]";
	public static String newsArticleToolBarCommentscountBottom = "//div[@class='info-layers']//*[starts-with(@class,'comment-number')]";
	public static String newsArticleTitleAreaAuthorPageLink = "//p[@class='meta-author']/a";

	// Article content footer
	public static String newsArticleCitationText = "//*[@id='citation']";
	public static String newsArticleCommentsCopyRight = ".//*[@id='legal_block']//p[contains(text(),'Medscape Medical News')]";

	// Relater links
	public static String recommendedReadingWidget = "//*[@id='recommended-card']/h6[text()='Recommended Reading']";
	public static String relatedConditionsProceduresWidget = "//*[@id='indications-card']/h6[text()='Related Conditions & Procedures']";
	public static String casesAndQuizesWidget = "//*[@id='quiz-card']/h6[text()='Cases & Quizzes']";
	public static String latestNpNewsWidget = "//*[@id='news-card']/h6[text()='Latest News & Perspective']";
	public static String mostPopularArticlesNewsWidget = "//*[@id='most-popular-card']/h6[text()='Most Popular Articles']";
	public static String mediaBanerWidget = "//*[@id='rel-links']//div[@class='rel-links-media']";
	public static String businessOfMedicine = "//*[@id='bom-card']/h6[text()='Business of Medicine']";
	public static String expertCommentary = "//*[@id='journal-card']/h6[text()='Expert Commentary']";
	public static String slideShowsaWidget = "//*[@id='journal-card']/h6[text()='Cases and Quizes']";
	public static String consultConceptDriver = ".//*[@id='consult-card']/h6[text()='Medscape Consult']";
	public static String moreFromThisJournal = "//*[@id='journal-card']/h6[text()='More from This Journal']";
	public static String otherJournalsinSpecialty = "//*[@id='journal-card']/h6[text()='More from This Journal']";
	public static String drugsRelatedwidget = ".//*[@id='indications-card']/h6[starts-with(text(),'Drugs Related to')]";
	public static String drugsIndicationCheckerwidget = ".//*[@id='tools-card']/a[text()='Drug Interaction Checker']";

	// Email This Layer objects
	public static String emailThisLayerTitleObject = "//*[@id='js-email-this']/div[@class='modal-header']/h6";

	public static String emailThistoAddress = ".//*[@id='emailToEmail']";
	public static String emailThisFromAddress = "//*[@id='emailSenderEmail']";

	public static String emailThisMessage = ".//*[@id='emailOptionalMessage']";
	public static String emailThisSendButtom = ".//*[@id='emailSend']";
	public static String emailThisCopyToMe = ".//*[@id='emailCopyMe']";
	public static String emailThisSubjectLine = ".//*[@id='emailSubject']";
	public static String emailThisRecipientRequired = ".//*[@id='recipientRequired']";
	public static String emailThisSuccessMessage = "//*[@id='js-email-this']/div[@class='email-status']";
	public static String emailThisCloseIcon = ".//*[@id='js-email-this']//div[@class='close-modal']";

}
