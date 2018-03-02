/**  
 * Class for search view
 * RK_ID: RK_STOCK
 * @author wgz
 * @date 2010-04-27
 */
package com.lenovo.leos.stocks;

public class StockerCodeItem  {
    private CharSequence code;
    private CharSequence type;
    private CharSequence name;
    
    public StockerCodeItem() {
    	code = "";
    	type = "";
    	name = "";
    }
    
    public StockerCodeItem(CharSequence code, CharSequence type, CharSequence name) {
        this.code = code;
        this.type = type;
        this.name = name;
    }
    
    public CharSequence getCode() {
		return code;
	}

	public void setCode(CharSequence code) {
		this.code = code;
	}

	public CharSequence getType() {
		return type;
	}

	public void setType(CharSequence type) {
		this.type = type;
	}

	public CharSequence getName() {
		return name;
	}

	public void setName(CharSequence name) {
		this.name = name;
	}
}

