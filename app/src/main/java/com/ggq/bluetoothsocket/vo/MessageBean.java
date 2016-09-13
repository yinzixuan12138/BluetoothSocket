package com.ggq.bluetoothsocket.vo;

/**
 * Created by DYK on 2015/11/15.
 */
public class MessageBean {
    public String From;
    public String To;
    public String Message;
    public Boolean IsMy;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getIsMy() {
        return IsMy;
    }

    public void setIsMy(Boolean isMy) {
        IsMy = isMy;
    }
}
