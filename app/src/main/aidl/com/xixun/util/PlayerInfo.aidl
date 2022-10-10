
package com.xixun.util;

interface PlayerInfo {     
    String getProgramName();
    String getVersion();
    void setScreenWidth(int w);
    void setScreenHeight(int h);
    void taskScreenshot(String cmdId);
    void setExternalTemperature(float t);
    void setInternalTemperature(float t);
    void setHumidity(float h);
    boolean forcePlayProgram(String pid);
    boolean finishForcePlay();
    String getCurProgramId();
    void setUSBProgramPwd(String pwd);
    String executeJosnCommand(String josn);
    void pausePlayer(boolean b);
    boolean isPause();
    boolean clearTasks();
}