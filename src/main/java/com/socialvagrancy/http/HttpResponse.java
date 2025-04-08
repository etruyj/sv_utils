//===================================================================
// HttpResponse.java
//      Description:
//          Holds HttpResponse Information
//===================================================================

package com.socialvagrancy.utils.http;

import java.io.IOException;

public class HttpResponse extends IOException {
    private int status_code;
    private String response_message;
    private String body;

    //===========================================
    // Getters
    //===========================================

    public int getStatusCode() { return status_code; }
    public String getResponseMessage() { return response_message; }
    public String getBody() { return body; }

    //===========================================
    // Setters
    //===========================================

    public void setStatusCode(int code) { status_code = code; }
    public void setResponseMessage(String message) { response_message = message; }
    public void setBody(String response) { body = response; }
}
