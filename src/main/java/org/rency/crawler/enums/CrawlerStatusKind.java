package org.rency.crawler.enums;

/**
 * 爬虫状态
 * @author rencaiyu
 *
 */
public enum CrawlerStatusKind {

	INIT("INIT","初始化"),
	START("START","启动中"),
	RUNNING("RUNNING","运行中"),
	STOP("STOP","停止"),
	EXCEPTION("EXCEPTION","异常"),
	;
	
	private String code;
	private String msg;
	
	CrawlerStatusKind(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public static CrawlerStatusKind get(String code){
		for(CrawlerStatusKind ek : CrawlerStatusKind.values()){
			if(ek.getCode().toUpperCase().equals(code.toUpperCase())){
				return ek;
			}
		}
		return null;
	}
	
}