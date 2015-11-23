package interprete;

public class PSValue implements Comparable<PSValue> {  
  
  public static final PSValue NULL = new PSValue();  
  public static final PSValue VOID = new PSValue();  
  
  private Object value;  
  
  private PSValue() {  
    // private constructor: only used for NULL and VOID  
    value = new Object();  
  }  
  
  public PSValue(Object v) {  
    if(v == null) {  
      throw new RuntimeException("v == null");  
    }  
    value = v;  
    // only accept boolean, list, number or string types  
    if(!(isBoolean() || isNumber())) {  
      throw new RuntimeException("invalid type: " + v + " (" + v.getClass() + ")");  
    }  
  }  
  
  public Boolean asBoolean() {  
    return (Boolean)value;  
  }  
  
  public Double asDouble() {  
    return ((Number)value).doubleValue();  
  }  
  

  @Override  
  public int compareTo(PSValue that) {  
    if(this.isNumber() && that.isNumber()) {  
      if(this.equals(that)) {  
        return 0;  
      }  
      else {  
        return this.asDouble().compareTo(that.asDouble());  
      }  
    }   
    else {  
      throw new RuntimeException("illegal expression: can't compare `" +   
          this + "` to `" + that + "`");  
    }  
  }  
  
  @Override  
  public boolean equals(Object o) {  
    if(this == VOID || o == VOID) {  
      throw new RuntimeException("can't use VOID: " + this + " ==/!= " + o);  
    }  
    if(this == o) {  
      return true;  
    }  
    if(o == null || this.getClass() != o.getClass()) {  
      return false;  
    }  
    PSValue that = (PSValue)o;  
    if(this.isNumber() && that.isNumber()) {  
      double diff = Math.abs(this.asDouble() - that.asDouble());  
      return diff < 0.00000000001;  
    }  
    else {  
      return this.value.equals(that.value);  
    }  
  }  
  
  @Override  
  public int hashCode() {  
    return value.hashCode();  
  }  
  
  public boolean isBoolean() {  
    return value instanceof Boolean;  
  }  
  
  public boolean isNumber() {  
    return value instanceof Number;  
  }  

  public boolean isNull() {  
    return this == NULL;  
  }  
  
  public boolean isVoid() {  
    return this == VOID;  
  }  
  
  @Override  
  public String toString() {  
    return isNull() ? "NULL" : isVoid() ? "VOID" : String.valueOf(value);  
  }  
}  