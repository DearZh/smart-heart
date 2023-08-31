package com.ymm;

/**
 * @author Arnold.zhao
 * @version R.java, v 0.1 2022-08-23 20:52 Arnold.zhao Exp $$
 */
public class R {
    String exceptionMsg;
    String msg;

    public R(String exceptionMsg, String msg) {
        this.exceptionMsg = exceptionMsg;
        this.msg = msg;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /*  @Override
        public String toString() {
            return "R{" +
                    "exceptionMsg='" + exceptionMsg + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }*/
}
