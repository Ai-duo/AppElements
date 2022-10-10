package com.kd.appelements.add;

public class SiteSet {
    //标题字体大小
    public int topSize;
    //主体字体大小
    public int midSize;
    //底部字体大小
    public int endSize;
    //是否切换界面
    public boolean isChange;
    //字体
    public int textTypeface;
    //布局id
    public int layoutId;
    //是否显示时间
    public boolean isShowTop = true;
    //是否显示天气
    public boolean isShowWea = true;
    public int elementcounts = 4 ;
    //要素排列方向0是水平方向，1是竖直方向
    public int orientation = 0;
    public static final String EightRightText ="当前温度:--℃\n"
            +  "风向:--\n"
            + "风速:--m/s\n"
            +"最低温度:--℃" ;
    public static final String EightLeftText = "当前湿度:--%\n"
            +  "最小湿度:--%\n"
            + "小时雨量:--mm\n"
            +"最高温度:--℃" ;
    public static final String SixRightText ="风速:--m/s\n"
            +"风向:--\n"
            + "气压:" + "--hPa\n";
    public static final String FiveRightText ="风速:--m/s\n"
            +"风向:--\n"
           ;
    public static final String SixLeftText = "温度:--℃\n"
            +"湿度:--" + "%\n"
            + "雨量:--mm\n";
    public static final String FourRightText ="风速:--m/s\n"
            +"风向:--\n";
    public static final String FourLeftText = "温度:--℃\n"
            + "雨量:--mm\n";
    public SiteSet(int topSize, int midSize, int endSize) {
        this.topSize = topSize;
        this.midSize = midSize;
        this.endSize = endSize;
    }

    public SiteSet(int topSize, int midSize, int endSize, boolean isChange, int textTypeface, int layoutId) {
        this.topSize = topSize;
        this.midSize = midSize;
        this.endSize = endSize;
        this.isChange = isChange;
        this.textTypeface = textTypeface;
        this.layoutId = layoutId;
    }

    public SiteSet(int topSize, int midSize, int endSize, boolean isChange, int textTypeface, int layoutId, boolean isShowTop, boolean isShowWea,int orientation) {
        this.topSize = topSize;
        this.midSize = midSize;
        this.endSize = endSize;
        this.isChange = isChange;
        this.textTypeface = textTypeface;
        this.layoutId = layoutId;
        this.isShowTop = isShowTop;
        this.isShowWea = isShowWea;
        this.orientation = orientation;
    }

    public SiteSet(int topSize, int midSize, int endSize, boolean isChange, int textTypeface, int layoutId, boolean isShowTop, boolean isShowWea, int elementcounts,int orientation) {
        this.topSize = topSize;
        this.midSize = midSize;
        this.endSize = endSize;
        this.isChange = isChange;
        this.textTypeface = textTypeface;
        this.layoutId = layoutId;
        this.isShowTop = isShowTop;
        this.isShowWea = isShowWea;
        this.elementcounts = elementcounts;
        this.orientation = orientation;
    }

    public String getRightText(Elements elements){
        if(elements!=null){
            if(elementcounts==7){
                return elements.getSevenElementRightText();
            }else  if(elementcounts==6){
                return elements.getElementRightText();
            }else if(elementcounts==5){
                return elements.getFiveElementRightText();
            }else if(elementcounts==4){
                return elements.getFourElementRightText();
            }else{
                return elements.getEightElementRightText();
            }
        }else{
            return  "";
        }
    }
    public String getLeftText(Elements elements){
        if(elements!=null){
            if(elementcounts==7){
                return elements.getSevenElementLeftText();
            }else if(elementcounts==6){
                return elements.getElementLeftText();
            }else if(elementcounts==5){
                return elements.getFiveElementLeftText();
            }else if(elementcounts==4){
                return elements.getFourElementLeftText();
            }else{
                return elements.getEightElementLeftText();
            }
        }else{
            return  "";
        }
    }
}
