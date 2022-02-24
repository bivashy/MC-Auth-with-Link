package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractButton implements Button{
	protected final Map<String, String> additionalInfo = new HashMap<>();
	protected final int row, column;
	protected String label = "";
	
	public AbstractButton(int row, int column, String label) {
		this.row = row;
		this.column = column;
		this.label = label;
	}

	public AbstractButton(int row,int column) {
		this(row,column,"");
	}

	@Override
	public Integer getRow() {
		return row;
	}

	@Override
	public Integer getColumn() {
		return column;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Map<String, String> getAdditionalInfo() {
		return Collections.unmodifiableMap(additionalInfo);
	}
	
	
}
