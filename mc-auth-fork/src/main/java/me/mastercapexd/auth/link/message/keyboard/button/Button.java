package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Map;

public interface Button {
	Integer getRow();
	
	Integer getColumn();
	
	String getLabel();
	
	Map<String,String> getAdditionalInfo();
}
