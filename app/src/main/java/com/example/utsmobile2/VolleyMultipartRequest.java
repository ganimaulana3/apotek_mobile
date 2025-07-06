package com.example.utsmobile2;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + UUID.randomUUID().toString();

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private Map<String, DataPart> mByteData;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
        this.mParams = new HashMap<>();
        this.mByteData = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    public void setParams(Map<String, String> params) {
        this.mParams = params;
    }

    public void setByteData(Map<String, DataPart> byteData) {
        this.mByteData = byteData;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Text params
            if (mParams != null && mParams.size() > 0) {
                textParse(bos, mParams, getParamsEncoding());
            }

            // File params
            if (mByteData != null && mByteData.size() > 0) {
                dataParse(bos, mByteData);
            }

            // End boundary
            bos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
            return bos.toByteArray();

        } catch (IOException e) {
            throw new AuthFailureError("Multipart error: " + e.getMessage());
        }
    }

    private void textParse(ByteArrayOutputStream bos, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(bos, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(ByteArrayOutputStream bos, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(bos, entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(ByteArrayOutputStream bos, String parameterName, String parameterValue) throws IOException {
        bos.write((twoHyphens + boundary + lineEnd).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd).getBytes());
        bos.write(("Content-Type: text/plain; charset=UTF-8" + lineEnd).getBytes());
        bos.write(lineEnd.getBytes());
        bos.write(parameterValue.getBytes("UTF-8"));
        bos.write(lineEnd.getBytes());
    }

    private void buildDataPart(ByteArrayOutputStream bos, DataPart dataFile, String inputName) throws IOException {
        bos.write((twoHyphens + boundary + lineEnd).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd).getBytes());
        bos.write(("Content-Type: " + dataFile.getType() + lineEnd).getBytes());
        bos.write(lineEnd.getBytes());

        byte[] fileData = dataFile.getContent();
        bos.write(fileData);

        bos.write(lineEnd.getBytes());
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    public Map<String, DataPart> getByteData() {
        return mByteData;
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(String name, File imageFile) {
            this.fileName = imageFile.getName();
            try {
                this.content = new byte[(int) imageFile.length()];
                FileInputStream fis = new FileInputStream(imageFile);
                fis.read(this.content);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        public DataPart(String name, byte[] data, String type) {
            fileName = name;
            content = data;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type != null ? type : "application/octet-stream";
        }
    }

}

